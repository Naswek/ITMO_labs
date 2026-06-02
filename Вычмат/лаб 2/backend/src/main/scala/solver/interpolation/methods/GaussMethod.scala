package solver.interpolation.methods

import solver.interpolation.functions.Interpolator
import solver.interpolation.models.{Point, InterpolationResult}
import solver.interpolation.core.DifferenceTable
import solver.core.Message

class Gauss extends Interpolator {
  override def name: String = "Многочлен Гаусса"

  override def solve(points: Seq[Point], targetX: Double): InterpolationResult = {
    val n = points.size
    if (n < 2) {
      return InterpolationResult(name, 0.0, Seq.empty, Message.NotEnoughPoints)
    }

    val h = points(1).x - points(0).x
    if (math.abs(h) < 1e-9) {
      return InterpolationResult(name, 0.0, Seq.empty, Message.BadParameters)
    }

    if (!points.sliding(2).forall(p => math.abs((p(1).x - p(0).x) - h) < 1e-9))
      return InterpolationResult(name, 0.0, Seq.empty, Message.BadParameters)

    val midIdx = points.zipWithIndex.minBy(p => math.abs(p._1.x - targetX))._2
    val t = (targetX - points(midIdx).x) / h
    val table = DifferenceTable.finite(points.map(_.y))

    var res = points(midIdx).y
    var tPart = 1.0
    var factorial = 1.0

    if (targetX >= points(midIdx).x) {
      for (i <- 1 until math.min(midIdx + 1, n - midIdx)) {
        tPart *= (t - (i - 1))
        factorial *= (2 * i - 1)
        res += (tPart * table(midIdx)(2 * i - 1)) / factorial
        
        tPart *= (t + i)
        factorial *= (2 * i)
        res += (tPart * table(midIdx - i)(2 * i)) / factorial
      }
      InterpolationResult("Гаусс (вперед)", res, table, Message.Success)
    } else {
      for (i <- 1 until math.min(midIdx + 1, n - midIdx)) {
        tPart *= (t + (i - 1))
        factorial *= (2 * i - 1)
        res += (tPart * table(midIdx - i)(2 * i - 1)) / factorial
        
        tPart *= (t - i)
        factorial *= (2 * i)
        res += (tPart * table(midIdx - i)(2 * i)) / factorial
      }
      InterpolationResult("Гаусс (назад)", res, table, Message.Success)
    }
  }
}