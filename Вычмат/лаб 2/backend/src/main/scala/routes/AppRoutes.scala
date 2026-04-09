package routes

import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route
import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport._
import spray.json._
import DefaultJsonProtocol._
import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport._


object AppRoutes {
    val routes: Route = pathPrefix("api" / "v1") {
        concat(
            NonLinearRoutes.routes,

            path("test") {
                get {
                    complete("Ит воркс!")
                }
            }
        )
    }
}