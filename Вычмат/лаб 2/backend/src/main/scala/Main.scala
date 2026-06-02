import akka.http.scaladsl.Http
import akka.http.scaladsl.model.StatusCodes
import akka.http.scaladsl.server.Directives._
import akka.stream.Materializer
import akka.actor.typed.ActorSystem
import akka.actor.typed.scaladsl.Behaviors
import routes.AppRoutes
import akka.http.scaladsl.server.ExceptionHandler
import ch.megard.akka.http.cors.scaladsl.CorsDirectives._
import ch.megard.akka.http.cors.scaladsl.settings.CorsSettings
import akka.actor.typed.scaladsl.adapter._

object Main extends App {
  implicit val system: ActorSystem[Nothing] =
    ActorSystem(Behaviors.empty, "solver-system")
  implicit val ec: scala.concurrent.ExecutionContext =
      system.executionContext
  val corsSettings = CorsSettings.defaultSettings

  val routesWithLogging =
    cors(corsSettings) {
      handleExceptions(ExceptionHandler {
        case ex: Throwable =>
          println("ERROR: " + ex.getMessage)
          ex.printStackTrace()
          complete(StatusCodes.InternalServerError, "Internal error")
      }) {
        AppRoutes.routes
      }
    }

  Http()(system.classicSystem)
    .newServerAt("0.0.0.0", 8080)
    .bind(routesWithLogging)

  println("Server running at http://localhost:8080/")
}