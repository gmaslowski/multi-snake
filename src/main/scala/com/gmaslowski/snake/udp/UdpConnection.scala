package com.gmaslowski.snake.udp

import java.net.InetSocketAddress

import akka.actor.{Actor, ActorLogging, ActorRef, Props, actorRef2Scala}
import akka.io.{IO, Udp}
import com.typesafe.config.Config

object UdpConnection {
  def props(config: Config) = Props(classOf[UdpConnection], config)
}

class UdpConnection(val config: Config) extends Actor with ActorLogging {

  import context.system

  val udpAddress = new InetSocketAddress(config.getString("udp.listen.host"), config.getInt("udp.listen.port"))

  IO(Udp) ! Udp.Bind(self, udpAddress)

  def receive = {
    case Udp.Bound(_) =>
      context.become(ready(sender()))
  }

  def ready(socket: ActorRef): Receive = {

    case Udp.Received(data, _) =>

    case Udp.Unbind =>
      socket ! Udp.Unbind

    case Udp.Unbound =>
      context.stop(self)
  }
}
