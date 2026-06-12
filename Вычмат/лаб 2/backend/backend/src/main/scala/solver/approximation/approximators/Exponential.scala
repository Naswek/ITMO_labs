package solver.approximation.approximators

import solver.approximation.functions.{Point, ApproximationResult, Approximator}
import solver.core.Message

class Exponential extends Approximator {
  override def name: String = "Экспоненциальная аппроксимация"

  override def solve(points: Seq[Point]): ApproximationResult = {
    if (points.exists(_.y <= 0)) return ApproximationResult(methodName = name, message = Message.InvalidDataForModel)
    val n = points.size
    if (n < 2) return ApproximationResult(methodName = name, message = Message.NotEnoughPoints)
    
    val transformed = points.map(p => Point(p.x, math.log(p.y)))
    val sumX = transformed.map(_.x).sum
    val sumY = transformed.map(_.y).sum
    val sumX2 = transformed.map(p => p.x * p.x).sum
    val sumXY = transformed.map(p => p.x * p.y).sum

    val det = sumX2 * n - sumX * sumX
    if (math.abs(det) < 1e-12) return ApproximationResult(methodName = name, message = Message.SingularMatrix)

    val B = (sumXY * n - sumY * sumX) / det 
    val A = (sumX2 * sumY - sumX * sumXY) / det
    
    val a = math.exp(A)
    val b = B
    val f = (x: Double) => a * math.exp(b * x)
    val mse = math.sqrt(points.map(p => math.pow(f(p.x) - p.y, 2)).sum / n)

    ApproximationResult(
      methodName = name, 
      equation = f"y = $a%.3f * e^{(${b}%.3f * x)}",
      coefficients = Seq(a, b),
      mse = mse,
      rSquared = calculateRSquared(points, f),
      message = Message.Success
    )
  }
}