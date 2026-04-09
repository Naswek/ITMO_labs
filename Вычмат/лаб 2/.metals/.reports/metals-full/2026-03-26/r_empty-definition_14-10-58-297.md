error id: file://<WORKSPACE>/backend/src/main/scala/routes/SolverRoutes.scala:concat.
file://<WORKSPACE>/backend/src/main/scala/routes/SolverRoutes.scala
empty definition using pc, found symbol in pc: 
empty definition using semanticdb
empty definition using fallback
non-local guesses:
	 -akka/http/scaladsl/server/Directives.concat.
	 -akka/http/scaladsl/server/Directives.concat#
	 -akka/http/scaladsl/server/Directives.concat().
	 -models/concat.
	 -models/concat#
	 -models/concat().
	 -spray/json/DefaultJsonProtocol.concat.
	 -spray/json/DefaultJsonProtocol.concat#
	 -spray/json/DefaultJsonProtocol.concat().
	 -akka/http/scaladsl/marshallers/sprayjson/SprayJsonSupport.concat.
	 -akka/http/scaladsl/marshallers/sprayjson/SprayJsonSupport.concat#
	 -akka/http/scaladsl/marshallers/sprayjson/SprayJsonSupport.concat().
	 -concat.
	 -concat#
	 -concat().
	 -scala/Predef.concat.
	 -scala/Predef.concat#
	 -scala/Predef.concat().
offset: 936
uri: file://<WORKSPACE>/backend/src/main/scala/routes/SolverRoutes.scala
text:
```scala
package routes

import javax.xml.xpath.XPathFunctionResolver
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route
import solver.library.Library
import solve.library.samples.Samples
import models._
import spray.json.DefaultJsonProtocol._
import solver.library.functions.{FunctionPack, FunctionResult}
import solver.library.systems.{SystemPack, SystemResult}
import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport._
import spray.json.DefaultJsonProtocol._


class SolverRoutes {

    implicit val functionRequestFormat = jsonFormat5(FunctionRequest)
    implicit val systemRequestFormat = jsonFormat4(SystemRequest)
    implicit val functionApiResponseFormat = jsonFormat3(FunctionApiResponse)
    implicit val systemApiResponseFormat = jsonFormat3(SystemApiResponse)
    implicit val samplesFormat = jsonFormat2(SamplesResponse)

    val routes: Route = 
        path("solve") {
            conc@@at(
                path("function") {
                    post {
                        entity(as[FunctionRequest]) { request =>
                        val response =
                            (for {
                                pack <- Library.functionPack.get(request.functionId)
                                solver <- Library.functionMethods.get(request.methodId)
                            } yield {
                            val result =
                                solver.solve(pack, request.a, request.b, request.epsilon)

                                FunctionApiResponse(success = true, data = Some(FunctionResponse(result.x, result.fx, result.iterations)), error = Some(result.message.getOrElse("Неизвестная ошибка")))
                            }).getOrElse(
                                FunctionApiResponse(success = false, data = None, error = Some("Неверный id функции или метода"))
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
                                val result =
                                    solver.solve(pack, request.x0, request.epsilon)
                                SystemApiResponse(success = true, data = Some(SystemResponse(result.x, result.fx, result.iterations)), error = Some(result.message.getOrElse("Неизвестная ошибка")))
                                }).getOrElse(
                                    SystemApiResponse(success = false, data = None, error = Some("Неверный id системы или метода"))
                                )
                               
                               
                               
                            complete(response)
                        }
                            
                        }
                    },
                path("samples") {
                    get {
                        val samples = SamplesResponse(
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