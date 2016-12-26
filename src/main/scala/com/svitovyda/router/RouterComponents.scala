package com.svitovyda.router

import com.svitovyda.MyComponents
import org.webjars.RequireJS
import play.api.http.MimeTypes
import play.api.mvc._
import play.api.mvc.Results._
import play.api.routing._
import play.api.routing.sird._
import com.svitovyda.configuration.ConfigurationsComponents
import com.svitovyda.counter.CounterComponents

import scala.concurrent.ExecutionContext

class RouterComponents(
  counter: CounterComponents,
  configurations: ConfigurationsComponents
) {

  implicit val context: ExecutionContext = MyComponents.actorSystem.dispatcher

  lazy val router: Router = Router.from {

    case GET(p"/setup.js")       => Action {
      Ok(RequireJS.getSetupJavaScript("/webjars/")).as(MimeTypes.JAVASCRIPT)
    }
    case GET(p"/assets/$file*")  => controllers.Assets.at(path = "/public", file)
    case GET(p"/webjars/$file*") => controllers.WebJarAssets.at(file)

    case GET(p"/") => Action {
      Found("/assets/index.html")
    }

    case GET(p"/serverInfo") => configurations.controller.serverInfo

    case GET(p"/hello/$to") => Action.async {
      counter.controller.add().map { c => Results.Ok(s"Hello $to, you are visitor #$c today") }
    }

    case GET(p"/counter") => counter.controller.get
    case GET(p"/some")    => configurations.controller.someBusiness
  }

}
