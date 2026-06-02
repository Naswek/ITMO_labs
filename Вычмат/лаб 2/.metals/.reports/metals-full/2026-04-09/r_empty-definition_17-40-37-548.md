error id: file://<WORKSPACE>/backend/src/main/scala/solver/methods/SystemNewton.scala:SystemPack#
file://<WORKSPACE>/backend/src/main/scala/solver/methods/SystemNewton.scala
empty definition using pc, found symbol in pc: 
empty definition using semanticdb
empty definition using fallback
non-local guesses:
	 -solver/library/systems/Utils.SystemPack#
	 -solver/library/systems/SystemPack#
	 -SystemPack#
	 -scala/Predef.SystemPack#
offset: 213
uri: file://<WORKSPACE>/backend/src/main/scala/solver/methods/SystemNewton.scala
text:
```scala
package solver.methods

import solver.library.systems.Utils._
import solver.library.systems.{SystemMethod, SystemPack, SystemResult}

class SystemNewton extends SystemMethod {

  override def solve(system: SystemP@@ack, x0: Array[Double], eps: Double): SystemResult = {
    val initialFx = system.f(x0)

    
    if (x0.length != initialFx.length) {
      return new SystemResult(
        x0, 
        initialFx, 
        0, 
        Some(s"Ошибка ввода: система состоит из ${initialFx.length} уравнений, а вы ввели ${x0.length} начальных приближений.")
      )
    }
 
    var x = x0.clone()
    var iterations = 0
    var fx = system.f(x)


    def norm(v: Array[Double]) =
      math.sqrt(v.map(x => x * x).sum)

    while (iterations < 1000) {

      fx = system.f(x)

      if (norm(fx) < eps)
        return new SystemResult(x, fx, iterations, None)

      val J = numericalJacobian(system.f, x)
      val dx = solveLinear(J, fx.map(-_))

      for (i <- x.indices) {
        x(i) += dx(i)
      }

      iterations += 1
    }

    fx = system.f(x)

    new SystemResult(x, fx, iterations, if (iterations >= 1000) Some("Превышено максимальное количество итераций") else None)
  }
}
```


#### Short summary: 

empty definition using pc, found symbol in pc: 