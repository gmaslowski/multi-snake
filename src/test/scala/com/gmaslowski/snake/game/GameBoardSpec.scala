package com.gmaslowski.snake.game

import com.gmaslowski.snake.game.BoardItems.Dimension
import com.gmaslowski.snake.game.Border.YES
import com.gmaslowski.snake.game.Difficulty.HARD
import org.scalatest.{FlatSpec, Matchers}

class GameBoardSpec extends FlatSpec with Matchers {

  val borderedConfig = GameBoardConfig(
    difficulty = HARD,
    border = YES,
    dimension = Dimension(3, 3))

  it should "create obstacles on border" in {
    val obstacles  = GameBoard.generateObstacles(borderedConfig)

    obstacles.map(println)
  }
}
