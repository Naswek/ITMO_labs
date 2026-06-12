package solver.interpolation.methods

import solver.interpolation.functions.Interpolator
import solver.interpolation.models.{Point, InterpolationResult}
import solver.interpolation.core.DifferenceTable
import solver.core.Message

class NewtonDivided extends Interpolator {
  override def name: String = "Ньютон (первая формула)"

  override def solve(points: Seq[Point], targetX: Double): InterpolationResult = {
    val n = points.size
    if (n < 2) return InterpolationResult(name, 0, Seq.empty, Message.NotEnoughPoints)

    val table = DifferenceTable.divided(points)
    
    var result = table(0)(0)
    var xPart = 1.0

    for (j <- 1 until n) {
      xPart *= (targetX - points(j - 1).x)
      result += xPart * table(0)(j)
    }

    InterpolationResult(
      methodName = name,
      value = result,
      differenceTable = table,
      message = Message.Success,
      equation = InterpolationResult.getNewtonDividedEquation(points, table)
    )
  }
}