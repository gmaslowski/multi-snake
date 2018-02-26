package com.gmaslowski.snake.websocket

import akka.http.scaladsl.server.Directives._
import com.gmaslowski.snake.Akka
import com.gmaslowski.snake.websocket.FrontendWebSocketHandler.handleFrontendWebSocket

object WebSocketRoutes extends Akka.AkkaThings {

  val route =
    get {
      path("frontend" / "ws") {
        handleWebSocketMessages(handleFrontendWebSocket)
      }
    }
}
