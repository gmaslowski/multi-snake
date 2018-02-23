package com.gmaslowski.snake.game

object BoardItems {

  case class Point(x: Int, y: Int)

  case class Dimension(width: Int, height: Int) {

    if (width <= 2 || height <= 2) throw new IllegalArgumentException("Really?")

    val xLast = width - 1
    val yLast = height - 1

    def maxFields = width * height
  }

  sealed class BoardItem(p: Point)

  case class Food(p: Point) extends BoardItem(p)

  case class Obstacle(p: Point) extends BoardItem(p)

  case class SnakeOnBoard(snake: Snake, position: List[Point])

}
