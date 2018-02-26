package com.gmaslowski.snake.game

import akka.actor.ActorSystem
import akka.testkit.{ImplicitSender, TestKit, TestProbe}
import com.gmaslowski.snake.websocket.APIs.BoardData
import org.scalatest.{BeforeAndAfterAll, Matchers, WordSpecLike}

class GameBoardSpec extends TestKit(ActorSystem("GameBoardSpec")) with ImplicitSender
  with WordSpecLike with Matchers with BeforeAndAfterAll {

  val wsSender = TestProbe()

  override def afterAll {
    TestKit.shutdownActorSystem(system)
  }

  "GameBoard actor" must {

    "send board details to front" in {
      // todo: this test is not the best one, because it depend on schedule
      val gameBoard = system.actorOf(GameBoard.props(null, wsSender.testActor))

      wsSender.expectMsgClass(classOf[BoardData])
    }

  }
}
