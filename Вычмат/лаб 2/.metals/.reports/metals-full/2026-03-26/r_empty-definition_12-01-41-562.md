error id: file://<WORKSPACE>/backend/src/main/scala/routes/SolverRoutes.scala:
file://<WORKSPACE>/backend/src/main/scala/routes/SolverRoutes.scala
empty definition using pc, found symbol in pc: 
empty definition using semanticdb
empty definition using fallback
non-local guesses:
	 -akka/http/scaladsl/server/Directives.FunctionRequest.
	 -akka/http/scaladsl/server/Directives.FunctionRequest#
	 -akka/http/scaladsl/server/Directives.FunctionRequest().
	 -models/FunctionRequest.
	 -models/FunctionRequest#
	 -models/FunctionRequest().
	 -spray/json/DefaultJsonProtocol.FunctionRequest.
	 -spray/json/DefaultJsonProtocol.FunctionRequest#
	 -spray/json/DefaultJsonProtocol.FunctionRequest().
	 -FunctionRequest.
	 -FunctionRequest#
	 -FunctionRequest().
	 -scala/Predef.FunctionRequest.
	 -scala/Predef.FunctionRequest#
	 -scala/Predef.FunctionRequest().
offset: 321
uri: file://<WORKSPACE>/backend/src/main/scala/routes/SolverRoutes.scala
text:
```scala
package routes

import javax.xml.xpath.XPathFunctionResolver
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route
import solver.library.Library
import models._
import spray.json.DefaultJsonProtocol._



object SolverRoutes {

    implicit val functionRequestFormat = jsonFormat3(FunctionRe@@quest)
    implicit val systemRequestFormat = jsonFormat3(SystemRequest)
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
                                pack <- Lib.functionPack.get(request.functionId)
                                solver <- Lib.functionMethods.get(request.methodId)
                            } yield {
                            val (x, fx, iterations) =
                                solver.solve(pack, request.a, request.b, request.epsilon)

                                ApiResponse(success = true, data = Some(FunctionResponse(x, fx, iterations)), error = None)
                            }).getOrElse(
                                ApiResponse[FunctionResponse](
                                    success = false,
                                    data = None,
                                    error = Some("Неверный id функции или метода")
                              )
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
                                    pack <- Lib.systemPack.get(request.systemId)
                                    solver <- Lib.systemMethods.get(request.methodId)
                                } yield {
                                val (x, fx, iterations) =
                                    solver.solve(pack, request.x0, request.epsilon)

                                ApiResponse(success = true, data = Some(SystemResponse(x, fx, iterations)), error = None)
                                }).getOrElse(
                                    ApiResponse[SystemResponse](
                                        success = false,
                                        data = None,
                                        error = Some("Неверный id системы или метода")
                                    )
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