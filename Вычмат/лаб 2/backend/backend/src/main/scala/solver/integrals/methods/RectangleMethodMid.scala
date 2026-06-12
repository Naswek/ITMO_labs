package solver.integrals.methods

import solver.integrals.integrals.{IntegralsPack, IntegrationMethod, IntegralsResult}
import solver.core.Message

class RectangleMethodMid extends IntegrationMethod {
override protected def calculateAlgorithm(pack: IntegralsPack, a: Double, b: Double, eps: Double): IntegralsResult = {
  if (a == b) return IntegralsResult(0.0, 0, Message.Success)

  def calculateRight(n: Int): Double = {
    val h = (b - a) / n
    var sum = 0.0

    for (i <- 1 to n) {
      val xMid = a + (i - 0.5) * h 
      sum += pack.f(xMid)
    }
    sum * h
  }

  var n = 4
  var iPrev = calculateRight(n)
  var iCurr = 0.0
  val maxN = 1048576

  while (n <= maxN) {
    n *= 2
    iCurr = calculateRight(n)

    if (math.abs(iCurr - iPrev) / 3.0 <= eps) {
      return IntegralsResult(iCurr, n, Message.Success)
    }

    iPrev = iCurr
  }

  IntegralsResult(iCurr, n, Message.MaxPartitionsReached)
}
}