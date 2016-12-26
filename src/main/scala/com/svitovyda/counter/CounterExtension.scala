package com.svitovyda.counter

import akka.actor._

class CounterExtensionImpl(system: ExtendedActorSystem) extends Extension {

  val counterHolder: ActorRef = system.actorOf(CounterActor.props, "counter")
}

object CounterExtension
  extends ExtensionId[CounterExtensionImpl]
    with ExtensionIdProvider {

  override def lookup() = CounterExtension

  override def createExtension(system: ExtendedActorSystem) = new CounterExtensionImpl(system)

  override def get(system: ActorSystem): CounterExtensionImpl = super.get(system)
}
