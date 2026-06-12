package solver.interpolation.functions

import scala.math.*

object Functions {

  val p1: InterpolationPack = InterpolationPack(
    f = x => 2 * x + 3
  )

  val p2: InterpolationPack = InterpolationPack(
    f = x => x * x - 4 * x + 1
  )

  val p3: InterpolationPack = InterpolationPack(
    f = x => 0.5 * x * x * x - 2 * x * x + x
  )

  val p4: InterpolationPack = InterpolationPack(
    f = x => exp(0.3 * x)
  )

  val p5: InterpolationPack = InterpolationPack(
    f = x => 2 * exp(0.5 * x) + 1
  )

  val p6: InterpolationPack = InterpolationPack(
    f = x => log(x)
  )

  val p7: InterpolationPack = InterpolationPack(
    f = x => 3 * log(x) + 2
  )

  val p8: InterpolationPack = InterpolationPack(
    f = x => pow(x, 0.5)
  )

  val p9: InterpolationPack = InterpolationPack(
    f = x => 2 * pow(x, 1.7) + 1
  )

  val p10: InterpolationPack = InterpolationPack(
    f = x => sin(x)
  )

  val p11: InterpolationPack = InterpolationPack(
    f = x => cos(2 * x)
  )

  val p12: InterpolationPack = InterpolationPack(
    f = x => 1.0 / (1 + x * x)
  )

  val p13: InterpolationPack = InterpolationPack(
    f = x => sqrt(x)
  )

  val p14: InterpolationPack = InterpolationPack(
    f = x => abs(x)
  )

  val p15: InterpolationPack = InterpolationPack(
    f = x => x * x * x - 6 * x * x + 4 * x + 12
  )
}
