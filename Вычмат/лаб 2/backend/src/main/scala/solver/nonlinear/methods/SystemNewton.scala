package solver.nonlinear.methods

import solver.nonlinear.systems.Utils._
import solver.nonlinear.systems.{SystemMethod, SystemPack, SystemResult}
import solver.core.Message

class SystemNewton extends SystemMethod {

  override def solve(system: SystemPack, x0: Array[Double], eps: Double): SystemResult = {
    
    if (x0.length < 2) {
      return new SystemResult(
        x0,
        Array(0, 0), 
        0,
        Message.BadParameters
      )
    }
    
    val initialFx = system.f(x0)

    
    if (x0.length != initialFx.length) {
      return new SystemResult(
        x0, 
        initialFx, 
        0, 
        Message.BadParameters
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
        return new SystemResult(x, fx, iterations, Message.BadParameters)

      val J = numericalJacobian(system.f, x)
      val dx = solveLinear(J, fx.map(-_))

      for (i <- x.indices) {
        x(i) += dx(i)
      }

      iterations += 1
    }

    fx = system.f(x)

    new SystemResult(x, fx, iterations, if (iterations >= 1000) Message.IterationLimitExceeded else Message.Success)
  }
}