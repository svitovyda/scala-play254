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

sealed trait Strategy {
  def description: String
  def name: String = toString
}
object Strategy {
  val values: Set[Strategy] = Set(
    Random, Incremental, Postponed, AlwaysWin, AlwaysLoose, Exponential, Gaussian
  )

  def apply(name: String): Strategy = values find (_.name.toLowerCase == name.toLowerCase) getOrElse {
    sys.error(s"Cannot find the strategy named: $name")
  }

  case object Random extends Strategy {def description = "Simply random"}
  case object Incremental extends Strategy {def description = "Simply one by one"}
  case object Postponed extends Strategy {def description = "Do only once and stop"}
  case object AlwaysWin extends Strategy {def description = "One side always wins"}
  case object AlwaysLoose extends Strategy {def description = "One side always loose"}
  case object Exponential extends Strategy {def description = "Exponential distribution"}
  case object Gaussian extends Strategy {def description = "Gaussian distribution"}

  implicit val format: Format[Strategy] = Format[Strategy](
    Reads[Strategy] { js =>
      values find {
        _.name.toLowerCase == js.as[String].toLowerCase
      } map (JsSuccess(_)) getOrElse JsError(s"Could not read Strategy: $js")
    },
    Writes[Strategy] { s =>
      JsObject(Seq("name" -> Json.toJson(s.name), "description" -> Json.toJson(s.description)))
    }
  )
}

case class Business(strategies: Set[Strategy], someBoolean: Boolean)
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
