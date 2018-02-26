package com.gmaslowski.snake.game

import com.gmaslowski.snake.game.BoardItems.{Food, Obstacle, Point}
import com.gmaslowski.snake.game.Border.YES
import com.gmaslowski.snake.game.Difficulty.{Difficulty, EASY, HARD, NORMAL}

import scala.util.Random

object BoardGenerator {

  def percentage: Difficulty => Double = {
    case EASY => 50
    case NORMAL => 20
    case HARD => 10
  }

  def generateFood(food: List[Food], config: GameBoardConfig): List[Food] = {
    // todo: percentage name is really something weird
    val desiredFoodCount = config.dimension.maxFields * (percentage(config.difficulty) / 100)
    var generatedFood = food
    if (desiredFoodCount > food.size * 2) {
      val r = new Random()
      for (x <- 0 until desiredFoodCount.toInt - food.size) {
        generatedFood ::= Food(Point(r.nextInt(config.dimension.width), r.nextInt(config.dimension.height)))
      }
    }
    generatedFood.distinct
  }

  def generateObstacles(config: GameBoardConfig): List[Obstacle] = {

    var obstacles: List[Obstacle] = List.empty

    config.border match {
      case YES =>
        for {
          x <- 0 until config.dimension.width
          y <- 0 until config.dimension.height
        } yield (x, y) match {
          case (sidesX@(0 | config.dimension.xLast), _) => obstacles ::= Obstacle(Point(sidesX, y))
          case (_, sidesY@(0 | config.dimension.yLast)) => obstacles ::= Obstacle(Point(x, sidesY))
          case (_, _) =>
        }
    }

    val r = new Random()
    for (i <- 0 to config.dimension.maxFields / percentage(config.difficulty).toInt) {
      obstacles ::= Obstacle(Point(r.nextInt(config.dimension.width), r.nextInt(config.dimension.height)))
    }

    obstacles.distinct
  }

  def generateBoard(config: GameBoardConfig): (List[Food], List[Obstacle]) = {
    val food = generateFood(List.empty[Food], config)
    val obstacles = generateObstacles(config)
    (food.filter(food => obstacles.map(_.p).contains(food.p)), obstacles)
  }
}
