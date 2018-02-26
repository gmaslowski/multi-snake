package com.gmaslowski.snake.websocket

import akka.actor.{ActorRef, FSM, Props}
import com.gmaslowski.snake.Akka
import com.gmaslowski.snake.websocket.APIs.API_BoardData
import com.gmaslowski.snake.websocket.FrontendWebSocketClientsSender.RegisterWsClient
import com.gmaslowski.snake.websocket.FrontendWebSocketConnection._

object FrontendWebSocketConnection {

  def props(wsHandler: ActorRef) = Props(classOf[FrontendWebSocketConnection], wsHandler)

  // api
  case class ConnectRequest(outgoing: ActorRef)
  case class IncomingMessage()
  case class OutgoingMessage(api_boardData: API_BoardData)

  // states
  sealed trait State
  case object InactiveState extends State
  case object ConnectedState extends State

  // data
  sealed trait Data
  case object Uninitialized extends Data
  final case class WithReceiver(outgoing: ActorRef) extends Data

}

class FrontendWebSocketConnection(wsHandler: ActorRef) extends FSM[State, Data] with Akka.AkkaThings {

  startWith(InactiveState, Uninitialized)

  when(InactiveState) {
    case Event(ConnectRequest(outgoing), Uninitialized) =>
      log.info("Received connection request.")
      wsHandler ! RegisterWsClient(self)
      goto(ConnectedState) using WithReceiver(outgoing)
  }

  when(ConnectedState) {
    case Event(api_boardData: API_BoardData, WithReceiver(outgoing)) =>
      outgoing ! OutgoingMessage(api_boardData)
      stay using WithReceiver(outgoing)
  }

  initialize()
}