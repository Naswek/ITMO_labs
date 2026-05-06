package solver.nonlinear.methods

import solver.nonlinear.functions.{FunctionPack, Method1D, FunctionResult}
import solver.core.Message

class SimpleIterationMethod extends Method1D {
  override def solve(pack: FunctionPack, a: Double, b: Double, eps: Double): FunctionResult = {
    val df = (x: Double) => math.abs(pack.d1f(x))
    
    val maxDf = (0 to 10).map(i => df(a + (b - a) * i / 10)).max
    
    val lambda = if (pack.d1f(a) > 0) -1.0 / maxDf else 1.0 / maxDf
    
    var x = if (pack.f(a) > 0) a else b
    var fx = pack.f(x)
    var iterations = 0
    var xPrev = x
    

    while (math.abs(fx) > eps && iterations < 1000) {
      xPrev = x
      x = x + lambda * fx
      fx = pack.f(x)
      iterations += 1
      
      if (x.isInfinite || x.isNaN) {
         return new FunctionResult(xPrev, pack.f(xPrev), iterations, Message.MethodDoesNotConverge) 
      }
    }
    
    new FunctionResult(x, fx, iterations, if (iterations >= 1000) Message.IterationLimitExceeded else Message.Success)
  }
}