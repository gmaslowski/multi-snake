package com.gmaslowski.snake.game

import akka.actor.{Actor, ActorLogging, ActorRef, Props}
import com.gmaslowski.snake.game
import com.gmaslowski.snake.game.BoardItems.Dimension
import com.gmaslowski.snake.game.Border.{Border, YES}
import com.gmaslowski.snake.game.Difficulty.{Difficulty, EASY, HARD, NORMAL}
import com.gmaslowski.snake.game.GameBoard.{MoveSnake, NextMove, SendToFront}
import com.gmaslowski.snake.game.Move.Move
import com.gmaslowski.snake.websocket.APIs.BoardData
import com.typesafe.config.Config

object Move extends Enumeration {
  type Move = Value
  val LEFT, RIGHT, NONE = Value
}

object Difficulty extends Enumeration {
  type Difficulty = Value

  val EASY: game.Difficulty.Value = Value("easy")
  val NORMAL: game.Difficulty.Value = Value("normal")
  val HARD: game.Difficulty.Value = Value("hard")
}

object Border extends Enumeration {
  type Border = Value
  val YES: game.Border.Value = Value("yes")
  val NO: game.Border.Value = Value("no")
}

case class GameBoardConfig(difficulty: Difficulty, border: Border, dimension: Dimension)

case class Snake(gamerId: String)

object GameBoard {
  def props(plainConfig: Config, wsSender: ActorRef) = Props(classOf[GameBoard], plainConfig, wsSender)

  // api
  case object NextMove

  case class MoveSnake(snake: Snake, move: Move)

  case object SendToFront

}

class GameBoard(val plainConfig: Config, val wsSender: ActorRef) extends Actor with ActorLogging {

  // todo: this config should be created based on the plainConfig
  val config = new GameBoardConfig(NORMAL, YES, Dimension(70, 70))

  var nextMoves: Map[Snake, Move] = Map.empty
  var move: Int = 0

  var (food, obstacles) = BoardGenerator.generateBoard(config)

  import scala.concurrent.duration._

  implicit val ec = context.dispatcher
  context.system.scheduler.schedule(1 second, 200 milliseconds, self, SendToFront)

  override def receive: Receive = {

    case MoveSnake(snake, move) =>
      log.info(s"Snake=${snake} direction will be ${move}")
      nextMoves = nextMoves + (snake -> move)

    case NextMove =>
      move += 1

    case SendToFront =>
      val (a, b) = BoardGenerator.generateBoard(config)
      wsSender ! BoardData(config.dimension, b, a)
  }
}
