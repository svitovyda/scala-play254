package com.svitovyda.counter

import akka.actor._

class CounterActor extends Actor
  with ActorLogging {

  var counter: Int = 0

  override def receive: Receive = {
    case CounterActor.Add =>
      counter += 1
      log info s"Counter become: $counter"
      sender ! counter

    case CounterActor.Get =>
      sender ! counter
  }
}
object CounterActor {
  def props: Props = Props[CounterActor]

  sealed trait Incoming
  case object Get extends Incoming
  case object Add extends Incoming
}
