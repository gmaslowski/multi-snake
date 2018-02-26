package com.gmaslowski.snake.websocket

import akka.actor.ActorSystem
import akka.testkit.{ImplicitSender, TestKit, TestProbe}
import com.gmaslowski.snake.websocket.APIs.{API_BoardData, BoardData}
import org.scalatest.{BeforeAndAfterAll, Matchers, WordSpecLike}

class ToJsonConverterSpec extends TestKit(ActorSystem("ToJsonConverterSpec")) with ImplicitSender
  with WordSpecLike with Matchers with BeforeAndAfterAll {

  val wsSender = TestProbe()

  override def afterAll {
    TestKit.shutdownActorSystem(system)
  }

  "ToJsonConverter actor" must {
    val toJsonConverter = system.actorOf(ToJsonConverter.props)

    "convert a message to API class" in {
      toJsonConverter ! BoardData(List.empty, List.empty)

      expectMsg(API_BoardData(List.empty, List.empty))
    }

  }
}
