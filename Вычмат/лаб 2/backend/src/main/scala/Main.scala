import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.model.StatusCodes
import akka.http.scaladsl.server.Directives._
import akka.stream.Materializer
import routes.AppRoutes
import akka.http.scaladsl.server.ExceptionHandler
import ch.megard.akka.http.cors.scaladsl.CorsDirectives._
import ch.megard.akka.http.cors.scaladsl.settings.CorsSettings


object Main extends App {
  implicit val system = ActorSystem("solver-system")
  implicit val ec = system.dispatcher
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

  Http()
    .newServerAt("0.0.0.0", 8080)
    .bind(routesWithLogging)

  println("Server running at http://localhost:8080/")
}