package solver.approximation.approximators

import solver.approximation.functions.{Point, ApproximationResult, Approximator}
import solver.core.Message

class Logarithmic extends Approximator {
  override def name: String = "Логарифмическая аппроксимация"

  override def solve(points: Seq[Point]): ApproximationResult = {
    if (points.exists(_.x <= 0)) return ApproximationResult(methodName = name, message = Message.InvalidDataForModel)
    val n = points.size
    if (n < 2) return ApproximationResult(methodName = name, message = Message.NotEnoughPoints)
    
    val transformed = points.map(p => Point(math.log(p.x), p.y))
    val sumX = transformed.map(_.x).sum
    val sumY = transformed.map(_.y).sum
    val sumX2 = transformed.map(p => p.x * p.x).sum
    val sumXY = transformed.map(p => p.x * p.y).sum

    val det = sumX2 * n - sumX * sumX
    if (math.abs(det) < 1e-12) return ApproximationResult(
            methodName = name, 
            message = Message.SingularMatrix
        )

    val a = (sumXY * n - sumY * sumX) / det
    val b = (sumX2 * sumY - sumX * sumXY) / det
    
    val f = (x: Double) => if (x <= 0) 0 else a * math.log(x) + b
    val mse = math.sqrt(points.map(p => math.pow(f(p.x) - p.y, 2)).sum / n)

    ApproximationResult(
      methodName = name,
      equation = f"y = $a%.3f * ln(x) + $b%.3f",
      coefficients = Seq(a, b),
      mse = mse,
      rSquared = calculateRSquared(points, f),
      message = Message.Success
    )
  }
}