package com.gmaslowski.snake.websocket

import akka.NotUsed
import akka.actor.PoisonPill
import akka.http.scaladsl.model.ws.{Message, TextMessage}
import akka.stream.OverflowStrategy
import akka.stream.scaladsl.{Flow, Sink, Source}
import akka.util.Timeout
import com.gmaslowski.snake.websocket.APIs.JsonSupport
import com.gmaslowski.snake.websocket.FrontendWebSocketConnection.OutgoingMessage
import com.gmaslowski.snake.{Akka, SnakeBootstrap}

import scala.concurrent.ExecutionContext
import scala.concurrent.duration._


object FrontendWebSocketHandler extends Akka.AkkaThings with JsonSupport {

  def handleFrontendWebSocket: Flow[Message, Message, NotUsed] = {

    implicit val ec: ExecutionContext = system.dispatcher
    implicit val timeout: Timeout = 5 seconds

    val wsConnection = system.actorOf(FrontendWebSocketConnection.props(SnakeBootstrap.wsSender))

    val incomingMessages: Sink[Message, NotUsed] = Flow[Message]
      .map {
        case TextMessage.Strict(text) => FrontendWebSocketConnection.IncomingMessage()
      }
      .to(Sink.actorRef[FrontendWebSocketConnection.IncomingMessage](wsConnection, PoisonPill))

    val outgoingMessages: Source[Message, NotUsed] = Source.actorRef[OutgoingMessage](10, OverflowStrategy.fail)
      .mapMaterializedValue { outActor =>
        wsConnection ! FrontendWebSocketConnection.ConnectRequest(outActor)
        NotUsed
      }
      .map((msg: OutgoingMessage) => TextMessage(write(msg.api_boardData).compactPrint))

    Flow.fromSinkAndSource(incomingMessages, outgoingMessages)
  }
}

