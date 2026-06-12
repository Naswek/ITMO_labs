package solver.approximation.approximators

import solver.approximation.functions.{Point, ApproximationResult, Approximator}
import solver.core.{MatrixSolver, Message} 

class Quadratic extends Approximator {
  override def name: String = "Квадратичная аппроксимация"

  override def solve(points: Seq[Point]): ApproximationResult = {
    val n = points.size.toDouble

    if (n < 3) return ApproximationResult(
      methodName = name, 
      message = Message.NotEnoughPoints
    )
    
    val sx = points.map(_.x).sum
    val sx2 = points.map(p => math.pow(p.x, 2)).sum
    val sx3 = points.map(p => math.pow(p.x, 3)).sum
    val sx4 = points.map(p => math.pow(p.x, 4)).sum
    val sy = points.map(_.y).sum
    val sxy = points.map(p => p.x * p.y).sum
    val sx2y = points.map(p => math.pow(p.x, 2) * p.y).sum

    val matrix = Array(
      Array(sx4, sx3, sx2, sx2y),
      Array(sx3, sx2, sx,  sxy),
      Array(sx2, sx,  n,   sy)
    )

    try {
      val coeffs = MatrixSolver.solve(matrix) 
      val a = coeffs(0)
      val b = coeffs(1)
      val c = coeffs(2)

      val f = (x: Double) => a * x * x + b * x + c
      val mse = math.sqrt(points.map(p => math.pow(f(p.x) - p.y, 2)).sum / n)

      ApproximationResult(
        methodName = name,
        equation = f"y = $a%.3fx² + $b%.3fx + $c%.3f",
        coefficients = Seq(a, b, c),
        mse = mse,
        rSquared = calculateRSquared(points, f),
        message = Message.Success
      )
    } catch {
      case _: Exception => ApproximationResult(
        methodName = name,             
        message = Message.SingularMatrix
      )
    }
  }
}