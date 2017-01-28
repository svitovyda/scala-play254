package com.svitovyda.counter

import akka.actor.ActorSystem
import akka.testkit.{ImplicitSender, TestKit}
import com.svitovyda.counter.CounterActor._
import org.scalatest.{BeforeAndAfterAll, WordSpecLike}

class CounterActorSpec extends TestKit(ActorSystem("MySpec"))
  with ImplicitSender
  with WordSpecLike
  with BeforeAndAfterAll {

  override def afterAll {
    TestKit.shutdownActorSystem(system)
  }

  "CounterActor" must {

    "process correctly data" in {

      val actor = system.actorOf(CounterActor.props)

      actor ! Request.Get
      expectMsg(Response.Counter(0))

      actor ! Request.Add
      expectMsg(Response.Counter(1))

      actor ! Request.Get
      expectMsg(Response.Counter(1))

      actor ! Request.Add
      expectMsg(Response.Counter(2))
    }
  }
}
