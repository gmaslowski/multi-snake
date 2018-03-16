package com.gmaslowski.snake.websocket

import akka.actor.{Actor, ActorLogging, Props}
import com.gmaslowski.snake.websocket.APIs.{API_BoardData, BoardData}

object ToJsonConverter {
  def props = Props(classOf[ToJsonConverter])
}

class ToJsonConverter extends Actor with ActorLogging {
  override def receive = {
    case data@BoardData(dimension, obstacles, food) =>
      sender() ! API_BoardData(dimension, obstacles, food)
  }
}
