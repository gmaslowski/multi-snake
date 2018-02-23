package com.gmaslowski.snake

import com.gmaslowski.snake.udp.UdpConnection
import com.typesafe.config.ConfigFactory

object SnakeBootstrap extends App with Akka.AkkaThings {

  implicit val ec = system.dispatcher

  val config = ConfigFactory.load

  system.actorOf(UdpConnection.props(config))
}
