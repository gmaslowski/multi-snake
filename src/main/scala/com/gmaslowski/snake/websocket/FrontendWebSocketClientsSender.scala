package com.gmaslowski.snake.websocket

import akka.actor.{Actor, ActorLogging, ActorRef, Props}
import akka.pattern.{ask, pipe}
import akka.util.Timeout
import com.gmaslowski.snake.websocket.APIs.{API_BoardData, BoardData}
import com.gmaslowski.snake.websocket.FrontendWebSocketClientsSender.RegisterWsClient

import scala.concurrent.ExecutionContext
import scala.concurrent.duration._

object FrontendWebSocketClientsSender {
  def props = Props(classOf[FrontendWebSocketClientsSender])

  case class RegisterWsClient(client: ActorRef)
}

class FrontendWebSocketClientsSender extends Actor with ActorLogging {

  implicit val ec: ExecutionContext = context.dispatcher
  implicit val timeout: Timeout = 10 milliseconds

  var clients: List[ActorRef] = List.empty
  val toJsonConverter: ActorRef = context.actorOf(ToJsonConverter.props)

  override def receive = {
    case RegisterWsClient(client) =>
      log.info(s"Registering new WS client $client")
      clients ::= client

    case boardData: BoardData =>
      toJsonConverter ? boardData pipeTo self

    case api_boardData: API_BoardData =>
      clients.foreach(_ ! api_boardData)
  }
}