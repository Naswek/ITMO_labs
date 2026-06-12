package solver.interpolation.methods

import solver.interpolation.functions.Interpolator
import solver.interpolation.models.{Point, InterpolationResult}
import solver.interpolation.core.DifferenceTable
import solver.core.Message

class GaussFirst extends Interpolator {
  override def name: String = "Многочлен Гаусса (первая формула)"

  override def solve(points: Seq[Point], targetX: Double): InterpolationResult = {
    val n = points.size
    println("GaussFirst")
    if (n < 2) {
      return InterpolationResult(name, 0.0, Seq.empty, Message.NotEnoughPoints)
    }

    val h = points(1).x - points(0).x
    if (math.abs(h) < 1e-9) {
      return InterpolationResult(name, 0.0, Seq.empty, Message.BadParameters)
    }

    if (!points.sliding(2).forall(p => math.abs((p(1).x - p(0).x) - h) < 1e-9))
      return InterpolationResult(name, 0.0, Seq.empty, Message.BadParameters)

    val midIdx = (n - 1) / 2
    val t = (targetX - points(midIdx).x) / h
    val table = DifferenceTable.finite(points.map(_.y))

    var res = table(midIdx)(0)
    var tPart = 1.0
    var factorial = 1.0

    for (k <- 1 until math.min(midIdx + 1, n - midIdx)) {
      tPart *= (t + k - 1)
      factorial *= (2 * k - 1)
      res += (tPart * table(midIdx - k + 1)(2 * k - 1)) / factorial

      tPart *= (t - k)
      factorial *= (2 * k)
      res += (tPart * table(midIdx - k)(2 * k)) / factorial
    }
    InterpolationResult(name, res, table, Message.Success, equation = InterpolationResult.getGaussFirstEquation(points, table))
  }
}