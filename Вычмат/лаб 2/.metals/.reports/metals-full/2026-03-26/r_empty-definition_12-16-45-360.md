error id: file://<WORKSPACE>/backend/src/main/scala/solver/methods/ChordMethod.scala:FunctionPack#
file://<WORKSPACE>/backend/src/main/scala/solver/methods/ChordMethod.scala
empty definition using pc, found symbol in pc: 
empty definition using semanticdb
empty definition using fallback
non-local guesses:
	 -solver/library/functions/FunctionPack#
	 -FunctionPack#
	 -scala/Predef.FunctionPack#
offset: 160
uri: file://<WORKSPACE>/backend/src/main/scala/solver/methods/ChordMethod.scala
text:
```scala
package solver.methods
import solver.library.functions.{FunctionPack, Method1D}

class ChordMethod extends Method1D {
  override def solve(functionPack: Functio@@nPack, a: Double, b: Double, epsilon: Double): (Double, Double, Int, String) = {
    val fa = functionPack.f(a)
    val fb = functionPack.f(b)

    if (fa * fb < 0) {
      printWarning(f"В данном промежутке {a} до {b} нет корня.")
      return (0, 0, 0, "В данном промежутке нет корня.")
    }

    val f2a = functionPack.d2f(a)
    val (fix, x0) = if (fa * f2a > 0) {
      (a, b)
    } else {
      (b, a)
    }
    
    val fFix = functionPack.f(fix) 
    var x = x0
    var fx = functionPack.f(x)
    var iterations = 0

    while ((math.abs(fx)) > epsilon && iterations < 1000) {
        x = x - (fix - x) /( fFix - fx) * fx
        fx = functionPack.f(x)
        iterations += 1
    }
    (x, fx, iterations)
  }
}
```


#### Short summary: 

empty definition using pc, found symbol in pc: 