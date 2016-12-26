package com.svitovyda.counter

import akka.actor.ActorRef
import akka.pattern.ask
import akka.util.Timeout
import com.svitovyda.MyComponents
import play.api.libs.json.{Json, Writes}
import play.api.mvc.{Action, Controller, _}

import scala.concurrent.duration._
import scala.concurrent.{ExecutionContext, Future}

case class CounterResponse(views: Int)
object CounterResponse {
  implicit val writes: Writes[CounterResponse] = Json.writes[CounterResponse]
}


class CounterController extends Controller {

  val holder: ActorRef = CounterExtension(MyComponents.actorSystem).counterHolder

  implicit val timeout: Timeout = 5.seconds
  implicit val context: ExecutionContext = MyComponents.actorSystem.dispatcher

  def get: Action[AnyContent] = Action.async {

    (holder ? CounterActor.Get).map {
      case c: Int => Ok(Json.toJson(CounterResponse(c)))
      case _      => ExpectationFailed
    }
  }

  def add(): Future[Int] = (holder ? CounterActor.Add).mapTo[Int]
}
