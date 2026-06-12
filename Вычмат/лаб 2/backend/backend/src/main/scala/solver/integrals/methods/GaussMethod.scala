package solver.integrals.methods

import solver.integrals.integrals.{IntegralsPack, IntegrationMethod, IntegralsResult}
import solver.core.Message

class GaussMethod extends IntegrationMethod {
  override protected def calculateAlgorithm(pack: IntegralsPack, a: Double, b: Double, eps: Double): IntegralsResult = {
    if (a == b) return IntegralsResult(0.0, 0, Message.BadParameters)
    val t1 = -1.0 / math.sqrt(3.0)
    val t2 = 1.0 / math.sqrt(3.0)

    def calculateGauss(n: Int): Double = {
      val h = (b - a) / n
      var totalSum = 0.0

      for (i <- 0 until n) {
        val x_start = a + i * h
        val x_end = a + (i + 1) * h
        val mid = (x_start + x_end) / 2.0
        val halfH = h / 2.0

        val x1 = mid + halfH * t1
        val x2 = mid + halfH * t2
        totalSum += (pack.f(x1) + pack.f(x2)) * halfH
      }
      totalSum
    }

    var n = 4
    var iPrev = calculateGauss(n)
    var iCurr = 0.0
    val maxN = 1048576

    while (n <= maxN) {
      n *= 2
      iCurr = calculateGauss(n)
      val rungeError = math.abs(iCurr - iPrev) / 15.0

      if (rungeError <= eps) {
        return IntegralsResult(iCurr, n, Message.Success)
      }

      iPrev = iCurr
    }

    IntegralsResult(iCurr, n, Message.MaxPartitionsReached)
  }
}