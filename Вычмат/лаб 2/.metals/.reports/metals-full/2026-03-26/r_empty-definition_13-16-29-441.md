error id: file://<WORKSPACE>/backend/src/main/scala/solver/methods/SimpleIterationMethod.scala:
file://<WORKSPACE>/backend/src/main/scala/solver/methods/SimpleIterationMethod.scala
empty definition using pc, found symbol in pc: 
empty definition using semanticdb
empty definition using fallback
non-local guesses:
	 -iter.
	 -iter#
	 -iter().
	 -scala/Predef.iter.
	 -scala/Predef.iter#
	 -scala/Predef.iter().
offset: 551
uri: file://<WORKSPACE>/backend/src/main/scala/solver/methods/SimpleIterationMethod.scala
text:
```scala
package solver.methods
import solver.library.functions.{FunctionPack, Method1D}

class SimpleIterationMethod extends Method1D {
  override def solve(pack: FunctionPack, a: Double, b: Double, eps: Double): (Double, Double, Int, String) = {
    val df = (x: Double) => math.abs(pack.d1f(x))
    val maxDf = (0 to 10).map(i => df(a + (b - a) * i / 10)).max
    
    val lambda = if (pack.d1f(a) > 0) 1.0 / maxDf else -1.0 / maxDf
    
    var x = if (pack.f(a) > 0) a else b
    var fx = pack.f(x)
    var iter = 0
    
    while (math.abs(fx) > eps && i@@ter < 1000) {
      x = x + lambda * fx
      fx = pack.f(x)
      iterations += 1
    }
    
    (x, fx, iterations, if (iterations >= 1000) "Превышено максимальное количество итераций" else "")
  }
}
```


#### Short summary: 

empty definition using pc, found symbol in pc: 