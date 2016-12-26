package com.svitovyda

import akka.actor.ActorSystem
import com.svitovyda.configuration.ConfigurationsComponents
import play.api.{ApplicationLoader, BuiltInComponentsFromContext}
import play.api.ApplicationLoader.Context
import play.api.routing.Router
import com.svitovyda.counter.CounterComponents
import com.svitovyda.router.RouterComponents


class MyComponents(context: Context) {
  import MyComponents._

  lazy val counter = new CounterComponents

  lazy val configurations = new ConfigurationsComponents

  lazy val router = new RouterComponents(counter, configurations)

  lazy val play = new Play(context, () => router.router)
}
object MyComponents {

  val actorSystem = ActorSystem()

  class Play(
      context: ApplicationLoader.Context,
      createRouter: () => Router)
    extends BuiltInComponentsFromContext(context) {

    lazy val router: Router = createRouter()
    override lazy val actorSystem: ActorSystem = MyComponents.actorSystem
  }
}
