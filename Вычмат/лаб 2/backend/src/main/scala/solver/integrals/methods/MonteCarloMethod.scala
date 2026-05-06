package solver.integrals.methods

import solver.integrals.integrals.{IntegralsPack, IntegrationMethod, IntegralsResult}
import solver.core.Message
import scala.util.Random

class MonteCarloMethod extends IntegrationMethod {
  
  override protected def calculateAlgorithm(pack: IntegralsPack, a: Double, b: Double, eps: Double): IntegralsResult = {
    val N = 1000000
    var sum = 0.0
    val random = new Random()

    for (_ <- 1 to N) {
      val randomX = a + (b - a) * random.nextDouble()
      sum += pack.f(randomX)
    }
    
    val resultValue = (sum / N) * (b - a)

    IntegralsResult(resultValue, 0, Message.Success)
  }
}