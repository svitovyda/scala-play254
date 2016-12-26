package com.svitovyda.configuration

import akka.actor.ActorRef
import play.api.libs.json._
import play.api.mvc._
import com.svitovyda.MyComponents
import com.svitovyda.counter.{CounterActor, CounterExtension}

case class Info(environment: String, manifests: List[ManifestLoader.JarManifest])
object Info {
  implicit val writes: Writes[Info] = Json.writes[Info]
}

case class Business(strategies: Set[String], someBoolean: Boolean)
object Business {
  implicit val writes: Writes[Business] = Json.writes[Business]
}

class ConfigurationsController extends Controller {

  val configurations = ConfigurationsExtension(MyComponents.actorSystem)
  private val settings = configurations.settings

  val counter: ActorRef = CounterExtension(MyComponents.actorSystem).counterHolder

  def someBusiness = Action {
    counter ! CounterActor.Add
    Ok(Json.toJson(Business(settings.strategies, settings.anyBool)))
  }

  def serverInfo = Action {
    Ok(Json.toJson(
      Info(configurations.environmentOption.getOrElse("undefined"), configurations.manifests)
    ))
  }

}
