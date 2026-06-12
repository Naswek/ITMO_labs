package solver.approximation.approximators

import solver.approximation.functions.{Point, ApproximationResult, Approximator}
import solver.core.Message

class Linear extends Approximator {
  override def name: String = "Линейная аппроксимация"

  override def solve(points: Seq[Point]): ApproximationResult = {
    val n = points.size
    if (n < 2) return ApproximationResult(methodName = name, message = Message.NotEnoughPoints)
    
    val sumX = points.map(_.x).sum
    val sumY = points.map(_.y).sum
    val sumX2 = points.map(p => p.x * p.x).sum
    val sumXY = points.map(p => p.x * p.y).sum

    val det = sumX2 * n - sumX * sumX
    if (math.abs(det) < 1e-12) return ApproximationResult(methodName = name, message = Message.SingularMatrix)

    val a = (sumXY * n - sumY * sumX) / det
    val b = (sumX2 * sumY - sumX * sumXY) / det

    val f = (x: Double) => a * x + b
    val mse = math.sqrt(points.map(p => math.pow(f(p.x) - p.y, 2)).sum / n)
    val r = calculatePearson(points)

    ApproximationResult(
      methodName = name,
      equation = f"y = $a%.3fx + $b%.3f",
      coefficients = Seq(a, b),
      mse = mse,
      rSquared = r * r,
      pearson = Some(r), 
      message = Message.Success
    )
  }

  private def calculatePearson(points: Seq[Point]): Double = {
    val n = points.size
    val meanX = points.map(_.x).sum / n
    val meanY = points.map(_.y).sum / n
    val num = points.map(p => (p.x - meanX) * (p.y - meanY)).sum
    val den = math.sqrt(points.map(p => math.pow(p.x - meanX, 2)).sum * 
                        points.map(p => math.pow(p.y - meanY, 2)).sum)
    if (den == 0) 0 else num / den
  }
}