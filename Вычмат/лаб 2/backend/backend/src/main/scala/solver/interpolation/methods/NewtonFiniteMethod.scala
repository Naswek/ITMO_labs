package solver.interpolation.methods

import solver.interpolation.functions.Interpolator
import solver.interpolation.models.{Point, InterpolationResult}
import solver.interpolation.core.DifferenceTable
import solver.core.Message

class NewtonFinite extends Interpolator {
  override def name: String = "Ньютон (вторая формула)"

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

    val table = DifferenceTable.finite(points.map(_.y))
    val mid = points(n/2).x
    
    if (targetX <= mid) {
      val t = (targetX - points(0).x) / h
      var res = table(0)(0)
      var tPart = 1.0
      var factorial = 1.0
      
      for (i <- 1 until n) {
        tPart *= (t - i + 1)
        factorial *= i
        res += (table(0)(i) * tPart) / factorial
      }
      InterpolationResult(name, res, table, Message.Success, equation = InterpolationResult.getNewtonFiniteForwardEquation(points, table))
    } else {
      val t = (targetX - points.last.x) / h
      var res = table.last(0) 
      var tPart = 1.0
      var factorial = 1.0
      
      for (i <- 1 until n) {
        tPart *= (t + i - 1)
        factorial *= i
        res += (table(n - 1 - i)(i) * tPart) / factorial
      }
      InterpolationResult(name, res, table, Message.Success, equation = InterpolationResult.getNewtonFiniteBackwardEquation(points, table))
    }
  }
}