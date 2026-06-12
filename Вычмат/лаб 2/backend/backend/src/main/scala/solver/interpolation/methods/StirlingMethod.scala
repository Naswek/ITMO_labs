package solver.interpolation.methods

import solver.interpolation.functions.Interpolator
import solver.interpolation.models.{Point, InterpolationResult}
import solver.interpolation.core.DifferenceTable
import solver.core.Message

class Stirling extends Interpolator {
  override def name: String = "Схема Стирлинга"

  override def solve(points: Seq[Point], targetX: Double): InterpolationResult = {
    val n = points.size
    if (n < 2) {
      return InterpolationResult(name, 0.0, Seq.empty, Message.NotEnoughPoints)
    }

    val h = points(1).x - points(0).x
    if (math.abs(h) < 1e-9) {
      return InterpolationResult(name, 0.0, Seq.empty, Message.BadParameters)
    }

    val isEquidistant = points.sliding(2).forall(p => math.abs((p(1).x - p(0).x) - h) < 1e-9)
    if (!isEquidistant) {
      return InterpolationResult(name, 0.0, Seq.empty, Message.BadParameters)
    }

    val midIdx = (n - 1) / 2
    val t = (targetX - points(midIdx).x) / h
    val table = DifferenceTable.finite(points.map(_.y))

    var res = table(midIdx)(0)
    var pProd = 1.0
    var factorial = 1.0

    for (k <- 1 until math.min(midIdx + 1, n - midIdx)) {
      factorial *= (2 * k - 1)
      val avgDiff = (table(midIdx - k + 1)(2 * k - 1) + table(midIdx - k)(2 * k - 1)) / 2.0
      res += (t * pProd * avgDiff) / factorial
      
      factorial *= (2 * k)
      res += (t * t * pProd * table(midIdx - k)(2 * k)) / factorial
      
      pProd *= (t * t - k * k)
    }
    
    InterpolationResult(name, res, table, Message.Success, equation = InterpolationResult.getStirlingEquation(points, table))
  }
}