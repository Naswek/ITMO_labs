error id: file://<WORKSPACE>/backend/src/main/scala/com/example/solver/methods/SecantMethod.scala:
file://<WORKSPACE>/backend/src/main/scala/com/example/solver/methods/SecantMethod.scala
empty definition using pc, found symbol in pc: 
empty definition using semanticdb
empty definition using fallback
non-local guesses:
	 -Method1D#
	 -scala/Predef.Method1D#
offset: 30
uri: file://<WORKSPACE>/backend/src/main/scala/com/example/solver/methods/SecantMethod.scala
text:
```scala
class SecantMethod extends Met@@hod1D {
  override def solve(functionPack: FunctionPack, a: Double, b: Double, epsilon: Double): Double = {
    val fa = functionPack.f(a)
    val fb = functionPack.f(b)

    var xPrev = a
    var xCurr = b
    var fPrev = functionPack.f(xPrev)
    var fCurr = functionPack.f(xCurr)
    var iterations = 0


    while ((math.abs(fCurr)) > epsilon && iterations < 1000) {

        if (math.abs(fCurr - fPrev) < 1e-12) {
            printWarning("Предупреждение: знаменатель близок к нулю")
            return (xCurr, fCurr, iterations)
        }


        xNext = xCurr - (xCurr - xPrev) / (fCurr - fPrev) * fCurr
        
        xPrev = xCurr
        fPrev = fCurr
        xCurr = xNext
        fCurr = functionPack.f(xCurr)

        iterations += 1
    }
    (xCurr, fCurr, iterations)
  }
}
```


#### Short summary: 

empty definition using pc, found symbol in pc: 