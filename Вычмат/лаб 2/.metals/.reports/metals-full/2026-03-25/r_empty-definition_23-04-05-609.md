error id: file://<WORKSPACE>/backend/src/main/scala/com/example/routes/SolverRoutes.scala:
file://<WORKSPACE>/backend/src/main/scala/com/example/routes/SolverRoutes.scala
empty definition using pc, found symbol in pc: 
empty definition using semanticdb
empty definition using fallback
non-local guesses:

offset: 1133
uri: file://<WORKSPACE>/backend/src/main/scala/com/example/routes/SolverRoutes.scala
text:
```scala
import javax.xml.xpath.XPathFunctionResolver
object SolverRoutes {

    implicit val functionRequestFormat = jsonFormat3(FunctionRequest)
    implicit val systemRequestFormat = jsonFormat3(SystemRequest)
    implicit val functionResponseFormat = jsonFormat3(FunctionResponse)
    implicit val systemResponseFormat = jsonFormat3(SystemResponse)

    val routes: Route = 
        path("solve") {
            concat(
                path("function") {
                    post {
                        entity(as[FunctionRequest]) { request =>
                            val pack = FunctionPack(request.f, request.d1f, request.d2f)
                            val solver = new NewtonMethod()
                            val (x, fx, iterations) = solver.solve(pack, request.a, request.b, request.eps)
                            complete(FunctionResponse(x, fx, iterations))
                        }
                    }
                },
                path("system") {
                    post {
                        entity(as[SystemRequest]) { request =>
                            val pack = SystemPack(request.f, request.d1@@f)
            )
        }

}
```


#### Short summary: 

empty definition using pc, found symbol in pc: 