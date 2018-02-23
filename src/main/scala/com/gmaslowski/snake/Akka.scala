package com.gmaslowski.snake

import akka.actor.ActorSystem
import akka.stream.ActorMaterializer

object Akka {

  trait AkkaThings {
    implicit val system = ActorSystem("multi-snake")
    implicit val materializer = ActorMaterializer()
  }

}
