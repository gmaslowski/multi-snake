package com.gmaslowski.snake

import akka.actor.ActorRef
import akka.http.scaladsl.Http
import com.gmaslowski.snake.game.GameBoard
import com.gmaslowski.snake.udp.UdpConnection
import com.gmaslowski.snake.websocket.FrontendWebSocketClientsSender
import com.typesafe.config.ConfigFactory

object SnakeBootstrap extends App with Akka.AkkaThings {

  implicit val ec = system.dispatcher

  val config = ConfigFactory.load

  system.actorOf(UdpConnection.props(config))
  val wsSender: ActorRef = system.actorOf(FrontendWebSocketClientsSender.props)

  system.actorOf(GameBoard.props(config, wsSender))

  import com.gmaslowski.snake.websocket.WebSocketRoutes._

  Http().bindAndHandle(route, config.getString("ws.listen.host"), config.getInt("ws.listen.port"))
}
