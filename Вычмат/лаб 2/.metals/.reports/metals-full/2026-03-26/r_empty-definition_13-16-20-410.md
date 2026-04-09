error id: file://<WORKSPACE>/backend/src/main/scala/solver/methods/SystemNewton.scala:f.
file://<WORKSPACE>/backend/src/main/scala/solver/methods/SystemNewton.scala
empty definition using pc, found symbol in pc: f.
empty definition using semanticdb
empty definition using fallback
non-local guesses:
	 -solver/library/systems/Utils.system.f.
	 -solver/library/systems/Utils.system.f#
	 -solver/library/systems/Utils.system.f().
	 -system/f.
	 -system/f#
	 -system/f().
	 -scala/Predef.system.f.
	 -scala/Predef.system.f#
	 -scala/Predef.system.f().
offset: 471
uri: file://<WORKSPACE>/backend/src/main/scala/solver/methods/SystemNewton.scala
text:
```scala
package solver.methods

import solver.library.systems.Utils._
import solver.library.systems.{SystemMethod, SystemPack}

class SystemNewton extends SystemMethod {

  override def solve(system: SystemPack, x0: Array[Double], eps: Double): (Array[Double], Array[Double], Int, String) = {

    var x = x0.clone()
    var iter = 0
    var fx = system.f(x)

    def norm(v: Array[Double]) =
      math.sqrt(v.map(x => x * x).sum)

    while (iter < 1000) {

      fx = system.f@@(x)

      if (norm(fx) < eps)
        return (x, fx, iterations, "")

      val J = numericalJacobian(system.f, x)
      val dx = solveLinear(J, fx.map(-_))

      for (i <- x.indices) {
        x(i) += dx(i)
      }

      iterations += 1
    }

    fx = system.f(x)

    (x, fx, iterations, if (iterations >= 1000) "Превышено максимальное количество итераций" else "")
  }
}
```


#### Short summary: 

empty definition using pc, found symbol in pc: f.