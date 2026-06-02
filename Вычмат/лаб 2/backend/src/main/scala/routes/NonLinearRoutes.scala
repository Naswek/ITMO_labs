package routes

import spray.json.RootJsonFormat
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route
import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport._
import spray.json._
import DefaultJsonProtocol._
import solver.nonlinear.Library
import solver.nonlinear.samples.Samples
import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport._
import solver.nonlinear.models._
import solver.core.Message
import routes.formats.JsonFormatsNonLinear._

object NonLinearRoutes {

  val routes: Route =
    pathPrefix("nonlinear") {
      concat(
        path("function") {
          post {
            entity(as[FunctionRequest]) { request =>

              val resultOpt =
                for {
                  pack <- Library.functionPack.get(request.functionId)
                  solver <- Library.functionMethods.get(request.methodId)
                } yield solver.solve(pack, request.a, request.b, request.epsilon)

              resultOpt match {

                case Some(result) =>
                  complete(FunctionResponse(
                    success = true,
                    x = result.x,
                    fx = result.fx,
                    iterations = result.iterations,
                    message = result.message.toString
                  ))
              
                case None =>
                  complete(FunctionResponse(
                    success = false, 
                    x = 0, 
                    fx = 0, 
                    iterations = 0, 
                    message = Message.BadId.toString
                  ))
              }
            }
          }
        },
        path("system") {
          post {
            entity(as[SystemRequest]) { request =>

              val resultOpt =
                for {
                  pack <- Library.systemPack.get(request.systemId)
                  solver <- Library.systemMethods.get(request.methodId)
                } yield solver.solve(pack, request.x0, request.epsilon)

              resultOpt match {

              case Some(result) =>
                complete(SystemResponse(
                  success = true,
                  x = result.x,
                  fx = result.fx,
                  iterations = result.iterations,
                  message = result.message.toString
                ))
            
              case None =>
                complete(SystemResponse(
                  success = false, 
                  x = Array.empty[Double], 
                  fx = Array.empty[Double], 
                  iterations = 0, 
                  message = Message.BadId.toString
                ))
              }
            }
          }
        },
        path("samples") {
          get {
            
            val response = SamplesResponse(
              functions = Samples.functions,
              systems = Samples.systems, 
              methods = MethodsResponse(
                function = Samples.methodsFunctions,
                system = Samples.methodsSystems
              )
            )
            complete(response)
          }
        }
      )
    }
}