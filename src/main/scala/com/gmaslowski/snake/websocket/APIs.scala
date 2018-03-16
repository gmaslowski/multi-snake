package com.gmaslowski.snake.websocket

import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport
import com.gmaslowski.snake.game.BoardItems.{Dimension, Food, Obstacle, Point}
import spray.json.DefaultJsonProtocol

object APIs {

  case class BoardData(dimension: Dimension, obstacles: List[Obstacle], food: List[Food])
  case class API_BoardData(dimension: Dimension, obstacles: List[Obstacle], food: List[Food])

  trait JsonSupport extends SprayJsonSupport {

    import DefaultJsonProtocol._
    import spray.json._

    implicit val dimensionFormat = jsonFormat(Dimension, "width", "height")
    implicit val pointFormat = jsonFormat2(Point)
    implicit val obstacleFormat = jsonFormat1(Obstacle)
    implicit val foodFormat = jsonFormat1(Food)
    implicit val boardDataFormat = jsonFormat3(API_BoardData)

    def write(api_boardData: API_BoardData) = api_boardData.toJson
  }

}

