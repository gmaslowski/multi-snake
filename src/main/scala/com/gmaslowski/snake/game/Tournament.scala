package com.gmaslowski.snake.game

import akka.actor.{ActorLogging, FSM, Props, ReceiveTimeout}
import akka.util.ByteString
import   com.gmaslowski.snake.game.Tournament._

import scala.concurrent.ExecutionContext

object Tournament {

  def props = Props(classOf[Tournament])

  // states
  sealed trait TournamentState
  case object WaitingForStart extends TournamentState
  case object Started extends TournamentState
  case object Ended extends TournamentState

  // data
  sealed trait TournamentData
  case object Uninitialized extends TournamentData
  case object RecordedData extends TournamentData

  // api
  case object Start
  case object End
}

// todo: all state transitions do not have sense
class Tournament extends FSM[TournamentState, TournamentData] with ActorLogging {
  startWith(WaitingForStart, Uninitialized)

  when(WaitingForStart) {
    case Event(Start, Uninitialized) =>
      goto(Started) using RecordedData
  }

  when(Started) {
    case Event(packet: ByteString, RecordedData) =>
      stay using RecordedData

    case Event(ReceiveTimeout, RecordedData) =>
      goto(Ended) using RecordedData
  }

  when(Ended) {
    case Event(e, s) =>
      stay
  }

  initialize()
}
