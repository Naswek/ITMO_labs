package routes

import akka.http.scaladsl.model.StatusCodes
import spray.json.RootJsonFormat
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route
import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport._
import spray.json._
import DefaultJsonProtocol._
import solver.interpolation.Library
import solver.interpolation.models._
import solver.core.Message
import solver.interpolation.samples._
import routes.formats.JsonFormatsInterpolation._

object InterpolationRoutes {
  val routes: Route =
    pathPrefix("interpolation") {
      concat(
        path("function") {
          post {
            entity(as[InterpolationRequest]) { request =>
              val points = if (!request.isGenerate) {
                request.x.zip(request.y).map { case (xi, yi) => Point(xi, yi) }
              } else {
                Library.functions.get(request.functionId) match {
                  case Some(pack) => generatePoints(pack.f, request.a, request.b, request.h)
                  case None => Seq.empty
                }
              }
              val finalPoints = points.distinctBy(_.x).sortBy(_.x)
              
              val isValid = if (request.isGenerate) Library.functions.contains(request.functionId) else true
              
              val response = if (!isValid) {
                InterpolationResponse(
                  success = false,
                  results = Seq.empty,
                  bestMethod = "",
                  sourcePoints = Seq.empty,
                  message = Message.BadId.toString
                )
              } else {
                val hasIntervalErrors = request.isGenerate && (request.a > request.b || request.h <= 0)
                val isUndefined = request.isGenerate && !hasIntervalErrors && Library.functions.get(request.functionId).exists { pack =>
                  hasUndefinedPoints(pack.f, request.a, request.b, request.h)
                }

                if (finalPoints.size > 150) {
                  InterpolationResponse(
                    success = false,
                    results = Seq.empty,
                    bestMethod = "",
                    sourcePoints = Seq.empty,
                    message = Message.TooManyPoints.toString
                  )
                } else if (hasIntervalErrors) {
                  InterpolationResponse(
                    success = false,
                    results = Seq.empty,
                    bestMethod = "",
                    sourcePoints = Seq.empty,
                    message = Message.InvalidInterval.toString
                  )
                } else if (isUndefined) {
                  InterpolationResponse(
                    success = false,
                    results = Seq.empty,
                    bestMethod = "",
                    sourcePoints = Seq.empty,
                    message = Message.FunctionUndefined.toString
                  )
                } else {
                  val isExtrapolated = finalPoints.nonEmpty && (request.targetX < finalPoints.head.x || request.targetX > finalPoints.last.x)
                  
                  val allResults = Library.interpolationFunctions.values.map { solver =>
                    val res = solver.solve(finalPoints, request.targetX)
                    res.copy(isExtrapolated = isExtrapolated)
                  }.toSeq

                  val bestMethodName = allResults.find(_.message == Message.Success).map(_.methodName).getOrElse("Нет подходящего метода")
                  
                  InterpolationResponse(
                    success = true,
                    results = allResults,
                    bestMethod = bestMethodName,
                    sourcePoints = points,
                    message = Message.Success.toString
                  )
                }
              }

              complete(response)
            }
          }
        },
        path("samples") {
          get {
            val response = SampleResponse(
              functions = Samples.functions,
              methods = Samples.interpolators
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

  private def hasUndefinedPoints(f: Double => Double, a: Double, b: Double, h: Double): Boolean = {
    if (a > b || h <= 0) return false
    (BigDecimal(a) to BigDecimal(b) by BigDecimal(h)).exists { x =>
      val yVal = f(x.toDouble)
      yVal.isNaN || yVal.isInfinite
    }
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
