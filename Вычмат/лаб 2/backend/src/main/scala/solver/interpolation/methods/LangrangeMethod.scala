package solver.interpolation.methods

import solver.interpolation.functions.Interpolator
import solver.interpolation.models.{Point, InterpolationResult}
import solver.core.Message

class Lagrange extends Interpolator {
  override def name: String = "Многочлен Лагранжа"

  override def solve(points: Seq[Point], targetX: Double): InterpolationResult = {
    val n = points.size
    
    if (n < 2) {
      return InterpolationResult(name, 0, Seq.empty, Message.NotEnoughPoints)
    }

    
    if (points.map(_.x).distinct.size != n) {
      return InterpolationResult(name, 0, Seq.empty, Message.BadParameters)
    }

    var result = 0.0

    for (i <- 0 until n) {
      var basicsPol = 1.0
      for (j <- 0 until n) {
        if (i != j) {
          basicsPol *= (targetX - points(j).x) / (points(i).x - points(j).x)
        }
      }
      result += basicsPol * points(i).y
    }

    InterpolationResult(
      methodName = name,
      value = result,
      differenceTable = Seq(points.map(_.y)), 
      message = Message.Success
    )
  }
}