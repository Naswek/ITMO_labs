package solver.integrals.methods

import solver.integrals.integrals.{IntegralsPack, IntegrationMethod, IntegralsResult}
import solver.core.Message

class SimpsonMethod extends IntegrationMethod {
  
  override protected def calculateAlgorithm(pack: IntegralsPack, a: Double, b: Double, eps: Double): IntegralsResult = {
  
    if (a == b) {
      return IntegralsResult(0.0, 0, Message.BadParameters) 
    }

    def calculateSimpson(n: Int): Double = {
      val h = (b - a) / n
      var sum = pack.f(a) + pack.f(b) 

      for (i <- 1 until n) {
        val x = a + i * h
        if (i % 2 == 0) {
          sum += 2 * pack.f(x)
        } else {
          sum += 4 * pack.f(x) 
        }
      }
      sum * h / 3.0
    }
    
    var n = 4 
    var iPrev = calculateSimpson(n)
    var iCurr = 0.0
    var rungeError = Double.MaxValue
    val maxN = 1048576 

    while (n <= maxN) {
      n *= 2 
      iCurr = calculateSimpson(n)

      rungeError = math.abs(iCurr - iPrev) / 15.0

      if (rungeError <= eps) {
        return IntegralsResult(iCurr, n,  Message.Success) 
      }

      iPrev = iCurr
    }
    
    IntegralsResult(iCurr, n, Message.MaxPartitionsReached)
  }
}