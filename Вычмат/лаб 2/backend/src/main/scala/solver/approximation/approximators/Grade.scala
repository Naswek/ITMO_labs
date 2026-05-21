package solver.approximation.approximators

import solver.approximation.functions.{Point, ApproximationResult, Approximator}
import solver.core.Message

class Grade extends Approximator {

  override def name: String = "Степенная аппроксимация"
  
  override def solve(points: Seq[Point]): ApproximationResult = {
    val validPoints = points.filter(p => p.x > 0 && p.y > 0)
    val n = validPoints.size
  
    if (n < 2) return ApproximationResult(methodName = name, message = Message.NotEnoughPoints)
    
    val transformedPoints = validPoints.map(p => Point(math.log(p.x), math.log(p.y)))
    
    val sumX = transformedPoints.map(_.x).sum
    val sumY = transformedPoints.map(_.y).sum
    val sumX2 = transformedPoints.map(p => p.x * p.x).sum
    val sumXY = transformedPoints.map(p => p.x * p.y).sum
  
    val det = sumX2 * n - sumX * sumX
    if (math.abs(det) < 1e-12) return ApproximationResult(methodName = name, message = Message.SingularMatrix)
  
    val b = (sumXY * n - sumY * sumX) / det
    val A = (sumX2 * sumY - sumX * sumXY) / det
    val a = math.exp(A)
    
    val f = (x: Double) => if (x <= 0) 0.0 else a * math.pow(x, b) 
    val mse = math.sqrt(validPoints.map(p => math.pow(f(p.x) - p.y, 2)).sum / n)
  
    ApproximationResult(
      methodName = name,
      equation = f"y = $a%.3f * x^{$b%.3f}", 
      coefficients = Seq(a, b),
      mse = mse,
      rSquared = calculateRSquared(validPoints, f),
      message = Message.Success
    )
  }
}