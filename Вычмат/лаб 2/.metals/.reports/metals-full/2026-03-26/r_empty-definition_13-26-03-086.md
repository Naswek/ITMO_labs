error id: file://<WORKSPACE>/backend/src/main/scala/routes/SolverRoutes.scala:sprayjson.
file://<WORKSPACE>/backend/src/main/scala/routes/SolverRoutes.scala
empty definition using pc, found symbol in pc: 
empty definition using semanticdb
empty definition using fallback
non-local guesses:
	 -akka/http/scaladsl/server/Directives.akka.http.scaladsl.marshallers.sprayjson.
	 -solver/library/Library.akka.http.scaladsl.marshallers.sprayjson.
	 -solver/library/Samples.akka.http.scaladsl.marshallers.sprayjson.
	 -models/akka/http/scaladsl/marshallers/sprayjson.
	 -spray/json/DefaultJsonProtocol.akka.http.scaladsl.marshallers.sprayjson.
	 -akka/http/scaladsl/marshallers/sprayjson/SprayJsonSupport.akka.http.scaladsl.marshallers.sprayjson.
	 -akka/http/scaladsl/marshallers/sprayjson.
	 -scala/Predef.akka.http.scaladsl.marshallers.sprayjson.
offset: 308
uri: file://<WORKSPACE>/backend/src/main/scala/routes/SolverRoutes.scala
text:
```scala
package routes

import javax.xml.xpath.XPathFunctionResolver
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route
import solver.library.Library._
import solver.library.Samples._
import models._
import spray.json.DefaultJsonProtocol._

import akka.http.scaladsl.marshallers.spr@@ayjson.SprayJsonSupport._
import spray.json.DefaultJsonProtocol._


class SolverRoutes {

    implicit val functionRequestFormat = jsonFormat5(FunctionRequest)
    implicit val systemRequestFormat = jsonFormat4(SystemRequest)
    implicit val functionResponseFormat = jsonFormat3(FunctionResponse)
    implicit val systemResponseFormat = jsonFormat3(SystemResponse)

    val routes: Route = 
        path("solve") {
            concat(
                path("function") {
                    post {
                        entity(as[FunctionRequest]) { request =>
                        val response =
                            (for {
                                pack <- Library.functionPack.get(request.functionId)
                                solver <- Library.functionMethods.get(request.methodId)
                            } yield {
                            val (x, fx, iterations, message) =
                                solver.solve(pack, request.a, request.b, request.epsilon)

                                ApiResponse(success = true, data = Some(FunctionResponse(x, fx, iterations)), error = message)
                            }).getOrElse(
                                ApiResponse[FunctionResponse](success = false, data = None, error = Some("Неверный id функции или метода"))
                            )

                        complete(response)
                        
                        }
                    }
                },
                path("system") {
                    post {
                        entity(as[SystemRequest]) { request =>
                            val response =
                                (for {
                                    pack <- Library.systemPack.get(request.systemId)
                                    solver <- Library.systemMethods.get(request.methodId)
                                } yield {
                                val (x, fx, iterations, message) =
                                    solver.solve(pack, request.x0, request.epsilon)
                                ApiResponse(success = true, data = Some(SystemResponse(x, fx, iterations)), error = message)
                                }).getOrElse(
                                    ApiResponse[SystemResponse](success = false, data = None, error = Some("Неверный id системы или метода"))
                                )
                               
                               
                               
                            complete(response)
                        }
                            
                        }
                    },
                path("samples") {
                    get {
                        val samples = List(
                            Samples.functions,
                            Samples.systems
                        )
                        complete(samples)
                    }
                }
            )
        }

}
```


#### Short summary: 

empty definition using pc, found symbol in pc: 