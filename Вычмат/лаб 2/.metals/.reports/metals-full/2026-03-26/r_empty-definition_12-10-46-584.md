error id: file://<WORKSPACE>/backend/src/main/scala/solver/methods/SimpleIteration.scala:functions.
file://<WORKSPACE>/backend/src/main/scala/solver/methods/SimpleIteration.scala
empty definition using pc, found symbol in pc: functions.
empty definition using semanticdb
empty definition using fallback
non-local guesses:
	 -solver/library/functions.
	 -scala/Predef.solver.library.functions.
offset: 49
uri: file://<WORKSPACE>/backend/src/main/scala/solver/methods/SimpleIteration.scala
text:
```scala
package solver.methods
import solver.library.func@@tions.{FunctionPack, Method1D}

class SimpleIteration extends Method1D {
  override def solve(pack: FunctionPack, a: Double, b: Double, eps: Double): Double = {
    val df = (x: Double) => math.abs(pack.d1f(x))
    val maxDf = (0 to 10).map(i => df(a + (b - a) * i / 10)).max
    
    val lambda = if (pack.d1f(a) > 0) 1.0 / maxDf else -1.0 / maxDf
    
    var x = if (pack.f(a) > 0) a else b
    var fx = pack.f(x)
    var iter = 0
    
    while (math.abs(fx) > eps && iter < 1000) {
      x = x + lambda * fx
      fx = pack.f(x)
      iter += 1
    }
    
    (x, fx, iter)
  }
}
```


#### Short summary: 

empty definition using pc, found symbol in pc: functions.