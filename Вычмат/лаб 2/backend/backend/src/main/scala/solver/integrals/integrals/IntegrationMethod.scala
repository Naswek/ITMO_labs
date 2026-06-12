package solver.integrals.integrals

import solver.core.Message

trait IntegrationMethod {

  protected def calculateAlgorithm(pack: IntegralsPack, a: Double, b: Double, eps: Double): IntegralsResult

  def solve(pack: IntegralsPack, a: Double, b: Double, eps: Double): IntegralsResult = {
    val delta = 1e-5 
    
    val aIsBad = pack.f(a).isInfinite || pack.f(a).isNaN
    val bIsBad = pack.f(b).isInfinite || pack.f(b).isNaN
    val mid = (a + b) / 2.0
    val midIsBad = pack.f(mid).isInfinite || pack.f(mid).isNaN

    try {
      if (midIsBad) {
        val res1 = calculateAlgorithm(pack, a, mid - delta, eps)
        val res2 = calculateAlgorithm(pack, mid + delta, b, eps)
        
        if (res1.value.isNaN || res2.value.isNaN) {
          return IntegralsResult(0, 0, Message.IntegralDoesNotExist)
        }
        
        return IntegralsResult(res1.value + res2.value, res1.n + res2.n, Message. IntervalWasChanged)
      }

      val safeA = if (aIsBad) a + delta else a
      val safeB = if (bIsBad) b - delta else b
      
      if (aIsBad || bIsBad) {
        val test1 = calculateAlgorithm(pack, safeA, safeB, eps).value
        val test2 = calculateAlgorithm(pack, if (aIsBad) a + delta/10 else a, if (bIsBad) b - delta/10 else b, eps).value
        
        if (math.abs(test2 - test1) > 0.5){
          return IntegralsResult(0, 0, Message.IntegralDoesNotExist)
        }
      }

      var finalResult = calculateAlgorithm(pack, safeA, safeB, eps)

      if (finalResult.value.isNaN) {
         return IntegralsResult(0, 0, Message.IntegralDoesNotExist)
      }
      
      val updatedResult = finalResult.copy(message = Message.Success)
      
      return updatedResult
    } catch {
      case _: Throwable => IntegralsResult(0, 0, Message.IntegralDoesNotExist)
    }
  }
}