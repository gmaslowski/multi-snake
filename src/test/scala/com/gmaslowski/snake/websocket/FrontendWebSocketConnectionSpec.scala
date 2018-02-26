package com.gmaslowski.snake.websocket

import akka.actor.ActorSystem
import akka.testkit.{ImplicitSender, TestFSMRef, TestKit, TestProbe}
import com.gmaslowski.snake.websocket.FrontendWebSocketClientsSender.RegisterWsClient
import com.gmaslowski.snake.websocket.FrontendWebSocketConnection.{ConnectRequest, ConnectedState}
import org.scalatest.{BeforeAndAfterAll, Matchers, WordSpecLike}

class FrontendWebSocketConnectionSpec extends TestKit(ActorSystem("FrontendWebSocketConnectionSpec")) with ImplicitSender
  with WordSpecLike with Matchers with BeforeAndAfterAll {

  val webSocketSender = TestProbe()

  override def afterAll {
    TestKit.shutdownActorSystem(system)
  }

  "FrontendWebSocketConnection actor" must {
    val client = TestProbe()

    "register himself in the WebSocketSender" in {
      val frontendWebSocketConnection = TestFSMRef(new FrontendWebSocketConnection(webSocketSender.testActor))
      frontendWebSocketConnection ! ConnectRequest(client.testActor)

      webSocketSender.expectMsgClass(classOf[RegisterWsClient])
    }

    "change his state to Connected after receiving a ConnectRequest" in {
      val frontendWebSocketConnection = TestFSMRef(new FrontendWebSocketConnection(webSocketSender.testActor))
      frontendWebSocketConnection ! ConnectRequest(client.testActor)

      frontendWebSocketConnection.stateName should be (ConnectedState)
    }


  }
}
