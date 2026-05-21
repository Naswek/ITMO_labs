package routes

import akka.http.scaladsl.model.StatusCodes
import spray.json.RootJsonFormat
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route
import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport._
import spray.json._
import DefaultJsonProtocol._
import solver.approximation.Library
import solver.approximation.models._
import solver.core.Message
import solver.approximation.samples._
import solver.approximation.functions.Point
import routes.formats.JsonFormatsApproximation._

object ApproximationRoutes {
  val routes: Route =
    pathPrefix("approximation") {
      concat(
      path("function") {
        post {
          entity(as[ApproximationRequest]) { request =>
            
            val response = Library.functions.get(request.functionId) match {
              case Some(functionPack) =>
                println(Library.functions.get(request.functionId).toString)    
                val points = generatePoints(functionPack.f, request.a, request.b, request.h)
                val allResults = Library.approximationFunctions.values.map(solver => solver.solve(points)).toSeq
                
                val successfulResults = allResults.filter(_.message == Message.Success)
                val bestMethodName = if (successfulResults.nonEmpty) 
                                       successfulResults.minBy(_.mse).methodName else "Нет подходящего метода"
                
                ApproximationResponse(
                  success = true,
                  results = allResults,
                  bestMethod = bestMethodName,
                  sourcePoints = points,
                  message = Message.Success.toString
                )
      
              case None =>
                ApproximationResponse(
                  success = false,
                  results = Seq.empty,
                  bestMethod = "",
                  sourcePoints = Seq.empty,
                  message = Message.BadId.toString 
                )
            }

            complete(response)
          }
        }
      },
        path("samples") {
          get {
            val response = SampleResponse(
              functions = Samples.functions,
              methods = Samples.approximators
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

  private def generatePoints(f: Double => Double, a: Double, b: Double, h: Double): Seq[Point] = {
  (BigDecimal(a) to BigDecimal(b) by BigDecimal(h)).flatMap { x =>
    val xVal = x.toDouble
    val yVal = f(xVal)
    if (yVal.isFinite && !yVal.isNaN) Some(Point(xVal, yVal))
    else None
  }.toSeq
  }
}