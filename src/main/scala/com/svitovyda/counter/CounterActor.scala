package com.svitovyda.counter

import akka.actor._
import com.svitovyda.counter.CounterActor._

class CounterActor extends Actor
  with ActorLogging {

  override def receive: Receive = holdNewCounter(0)

  def holdNewCounter(counter: Int): Receive = {
    case Request.Add =>
      val newCounter = counter + 1
      context.become(holdNewCounter(newCounter))
      log info s"Counter become: $newCounter"
      sender ! Response.Counter(newCounter)

    case Request.Get =>
      sender ! Response.Counter(counter)
  }
}
object CounterActor {
  def props: Props = Props[CounterActor]

  sealed trait Request
  object Request {
    case object Get extends Request
    case object Add extends Request
  }

  sealed trait Response
  object Response {
    case class Counter(counter: Int) extends Response
  }
}
