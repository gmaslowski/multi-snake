package com.gmaslowski.snake.game

import com.gmaslowski.snake.game.BoardGenerator.{generateFood, generateObstacles}
import com.gmaslowski.snake.game.BoardItems.{Dimension, Food, Obstacle}
import com.gmaslowski.snake.game.Border.YES
import com.gmaslowski.snake.game.Difficulty.{EASY, HARD}
import com.gmaslowski.snake.game.BoardGeneratorSpec._
import org.scalatest.{FlatSpec, Matchers}

object BoardGeneratorSpec {
  val WIDTH_DIM = 12
  val HEIGHT_DIM = 7

  def onlyBorder: Obstacle => Boolean = o => o.p.x == 0 || o.p.x == WIDTH_DIM - 1 || o.p.y == 0 || o.p.y == HEIGHT_DIM - 1
  def onBoard: Obstacle => Boolean = o => 0 <= o.p.x && o.p.x < WIDTH_DIM && 0 <= o.p.y && o.p.y < WIDTH_DIM
  def borderSize: (Int, Int) => Int = (width, height) => 2 * width + 2 * (height - 2)
}

class BoardGeneratorSpec extends FlatSpec with Matchers {

  val hardBorderedConfig = GameBoardConfig(
    difficulty = HARD,
    border = YES,
    dimension = Dimension(WIDTH_DIM, HEIGHT_DIM))

  val easyBorderedConfig = GameBoardConfig(
    difficulty = EASY,
    border = YES,
    dimension = Dimension(WIDTH_DIM, HEIGHT_DIM))

  it should "create obstacles on border" in {
    generateObstacles(hardBorderedConfig).count(onlyBorder) shouldEqual borderSize(WIDTH_DIM, HEIGHT_DIM)
  }

  it should "create obstacles only inside board" in {
    generateObstacles(hardBorderedConfig).filterNot(onBoard) shouldEqual List.empty
  }

  it should "create less obstacles on EASY difficulty than on HARD" in {
    generateObstacles(hardBorderedConfig).size should be > generateObstacles(easyBorderedConfig).size
  }

  it should "create obstacles not only on border" in {
    generateObstacles(hardBorderedConfig).filterNot(onlyBorder).size should be > 0
  }

  it should "create more food on EASY difficulty than on HARD " in {
    generateFood(List.empty[Food], easyBorderedConfig).size should be > generateFood(List.empty[Food], hardBorderedConfig).size
  }

  it should "obstacles take precedence if generated on the same point as food" in {
    val board = BoardGenerator.generateBoard(easyBorderedConfig)
    board._1.map(_.p) should not contain board._2.map(_.p)
  }


}
