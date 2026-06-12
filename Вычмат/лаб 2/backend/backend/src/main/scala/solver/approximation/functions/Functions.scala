package solver.approximation.functions

import solver.approximation.functions.FunctionPack
import scala.math.*

object Functions {

  val p1: FunctionPack = FunctionPack(
    f = x => 2 * x + 3
  )

  val p2: FunctionPack = FunctionPack(
    f = x => x * x - 4 * x + 1
  )

  val p3: FunctionPack = FunctionPack(
    f = x => 0.5 * x * x * x - 2 * x * x + x
  )

  val p4: FunctionPack = FunctionPack(
    f = x => exp(0.3 * x)
  )

  val p5: FunctionPack = FunctionPack(
    f = x => 2 * exp(0.5 * x) + 1
  )

  val p6: FunctionPack = FunctionPack(
    f = x => log(x)
  )

  val p7: FunctionPack = FunctionPack(
    f = x => 3 * log(x) + 2
  )

  val p8: FunctionPack = FunctionPack(
    f = x => pow(x, 0.5)
  )

  val p9: FunctionPack = FunctionPack(
    f = x => 2 * pow(x, 1.7) + 1
  )

  val p10: FunctionPack = FunctionPack(
    f = x => sin(x)
  )

  val p11: FunctionPack = FunctionPack(
    f = x => cos(2 * x)
  )

  val p12: FunctionPack = FunctionPack(
    f = x => 1.0 / (1 + x * x)
  )

  val p13: FunctionPack = FunctionPack(
    f = x => sqrt(x)
  )

  val p14: FunctionPack = FunctionPack(
    f = x => abs(x)
  )

  val p15: FunctionPack = FunctionPack(
    f = x => x * x * x - 6 * x * x + 4 * x + 12
  )
}