package com.gmaslowski.snake.game

import akka.actor.{Actor, ActorLogging, Props}
import com.gmaslowski.snake.game
import com.gmaslowski.snake.game.BoardItems.Dimension
import com.gmaslowski.snake.game.Border.Border
import com.gmaslowski.snake.game.Difficulty.Difficulty
import com.gmaslowski.snake.game.GameBoard.{MoveSnake, NextMove}
import com.gmaslowski.snake.game.Move.Move
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
  def props(plainConfig: Config) = Props(classOf[GameBoard], plainConfig)

  // api
  case object NextMove

  case class MoveSnake(snake: Snake, move: Move)

}

class GameBoard(val plainConfig: Config) extends Actor with ActorLogging {

  val config = null

  var nextMoves: Map[Snake, Move] = Map.empty
  var move: Int = 0

  var food, obstacles = BoardGenerator.generateBoard(config)

  override def receive: Receive = {

    case MoveSnake(snake, move) =>
      log.info(s"Snake=${snake} direction will be ${move}")
      nextMoves = nextMoves + (snake -> move)

    case NextMove =>
      move += 1

  }
}
