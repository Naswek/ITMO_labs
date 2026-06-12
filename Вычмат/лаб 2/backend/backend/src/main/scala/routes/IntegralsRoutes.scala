package routes

import spray.json.RootJsonFormat
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route
import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport._
import spray.json._
import DefaultJsonProtocol._
import solver.integrals.Library
import solver.integrals.samples.Samples
import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport._
import solver.integrals.models._
import solver.core.Message
import routes.formats.JsonFormatsIntegrals._

object IntegralsRoutes {
  println("APP ROUTES INIT")
  val routes: Route =
  pathPrefix("integrals") {
  concat(
        path("function") {
          post {
            entity(as[IntegralRequest]) { request =>

              val resultOpt =
                for {
                  pack <- Library.integralsPack.get(request.integralId)
                  solver <- Library.integralsMethods.get(request.methodId)
                } yield solver.solve(pack, request.a, request.b, request.epsilon)

              resultOpt match {

                case Some(result) =>
                  complete(IntegralResponse(
                    success = true,
                    value = result.value,
                    n = result.n,
                    message = result.message.toString
                  ))
              
                case None =>
                  complete(IntegralResponse(
                    success = false, 
                    value = 0, 
                    n = 0, 
                    message = Message.BadId.toString
                  ))
              }
            }
          }
        },
        
        path("samples") {
          get {
            val response = SamplesResponse(
              integrals = Samples.integrals,
              methods = Samples.integralsMethods
            )
            complete(response)
          }
        },
        path("test") {
            get {
                complete("Ит воркс!")
            }
        }
        )
        }
}