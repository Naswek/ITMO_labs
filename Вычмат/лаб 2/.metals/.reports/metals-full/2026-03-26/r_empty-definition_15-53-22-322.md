error id: file://<WORKSPACE>/backend/src/main/scala/Main.scala:
file://<WORKSPACE>/backend/src/main/scala/Main.scala
empty definition using pc, found symbol in pc: 
empty definition using semanticdb
empty definition using fallback
non-local guesses:
	 -akka/http/scaladsl/server/Directives.SolverRoutes.
	 -routes/SolverRoutes.
	 -SolverRoutes.
	 -scala/Predef.SolverRoutes.
offset: 749
uri: file://<WORKSPACE>/backend/src/main/scala/Main.scala
text:
```scala

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.model.StatusCodes
import akka.http.scaladsl.server.Directives._
import akka.stream.Materializer
import routes.SolverRoutes
import akka.http.scaladsl.server.ExceptionHandler


object Main extends App {
  implicit val system = ActorSystem("solver-system")
  implicit val materializer = Materializer(system)
  implicit val ec = system.dispatcher

  val routesWithLogging =
  handleExceptions(ExceptionHandler {
    case ex: Throwable =>
      println("🔥 ERROR: " + ex.getMessage)
      ex.printStackTrace()
      complete(StatusCodes.InternalServerError, "Internal error")
  }) {
    new SolverRoutes().routes
  }

  Http().newServerAt("0.0.0.0", 8080).bind(Solve@@rRoutes.routes)

  println("Server running at http://localhost:8080/")
}
```


#### Short summary: 

empty definition using pc, found symbol in pc: 