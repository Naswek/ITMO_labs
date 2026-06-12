package solver.integrals.methods

import solver.integrals.integrals.{IntegralsPack, IntegrationMethod, IntegralsResult}
import solver.core.Message

class TrapezoidMethod extends IntegrationMethod {
override protected def calculateAlgorithm(pack: IntegralsPack, a: Double, b: Double, eps: Double): IntegralsResult = {
  if (a == b) return IntegralsResult(0.0, 0, Message.Success)

  def calculateTrapezoidal(n: Int): Double = {
    val h = (b - a) / n
    var sum = (pack.f(a) + pack.f(b)) / 2.0 

    for (i <- 1 until n) {
      sum += pack.f(a + i * h)
    }
    sum * h
  }

  var n = 4
  var iPrev = calculateTrapezoidal(n)
  var iCurr = 0.0
  var rungeError = Double.MaxValue
  val maxN = 1048576

  while (n <= maxN) {
    n *= 2
    iCurr = calculateTrapezoidal(n)

    rungeError = math.abs(iCurr - iPrev) / 3.0

    if (rungeError <= eps) {
      return IntegralsResult(iCurr, n, Message.Success)
    }

    iPrev = iCurr
  }

  IntegralsResult(iCurr, n, Message.MaxPartitionsReached)
}
}