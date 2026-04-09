error id: file://<WORKSPACE>/backend/src/main/scala/solver/methods/SystemNewton.scala:SystemMethod#
file://<WORKSPACE>/backend/src/main/scala/solver/methods/SystemNewton.scala
empty definition using pc, found symbol in pc: 
empty definition using semanticdb
empty definition using fallback
non-local guesses:
	 -solver/systems/Utils.SystemMethod#
	 -SystemMethod#
	 -scala/Predef.SystemMethod#
offset: 94
uri: file://<WORKSPACE>/backend/src/main/scala/solver/methods/SystemNewton.scala
text:
```scala
package solver.methods
import solver.systems.Utils._


class SystemNewton extends SystemMethod@@ {

  override def solve(
    system: SystemPack,
    x0: Array[Double],
    eps: Double
  ): (Array[Double], Int) = {



    var x = x0.clone()
    var iter = 0

    def norm(v: Array[Double]) =
      math.sqrt(v.map(x => x*x).sum)

    while (iter < 100) {
      val fx = F(x)

      if (norm(fx) < eps)
        return (x, iter)

      val J = numericalJacobian(F, x)
      val dx = solveLinear(J, fx.map(-_))

      for (i <- x.indices) {
        x(i) += dx(i)
      }

      iter += 1
    }

    (x, iter)
  }
}
```


#### Short summary: 

empty definition using pc, found symbol in pc: 