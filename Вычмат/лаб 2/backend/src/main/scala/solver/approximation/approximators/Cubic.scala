package solver.approximation.approximators

import solver.approximation.functions.{Point, ApproximationResult, Approximator}
import solver.core.{MatrixSolver, Message}

class Cubic extends Approximator {
  override def name: String = "Кубическая аппроксимация"

  override def solve(points: Seq[Point]): ApproximationResult = {
    val n = points.size.toDouble
    if (n < 4) return ApproximationResult(methodName = name, message = Message.NotEnoughPoints)
    
    val sx = (1 to 6).map(p => points.map(pt => math.pow(pt.x, p)).sum)
    val sy  = points.map(_.y).sum
    val sxy = points.map(p => p.x * p.y).sum
    val sx2y = points.map(p => math.pow(p.x, 2) * p.y).sum
    val sx3y = points.map(p => math.pow(p.x, 3) * p.y).sum

    val finalMatrix = Array(
      Array(sx(5), sx(4), sx(3), sx(2), sx3y),
      Array(sx(4), sx(3), sx(2), sx(1), sx2y),
      Array(sx(3), sx(2), sx(1), sx(0), sxy),
      Array(sx(2), sx(1), sx(0), n,     sy)
    )

    try {
      val coeffs = MatrixSolver.solve(finalMatrix)
      val (a, b, c, d) = (coeffs(0), coeffs(1), coeffs(2), coeffs(3))
      val f = (x: Double) => a*math.pow(x,3) + b*math.pow(x,2) + c*x + d
      
      val mse = math.sqrt(points.map(p => math.pow(f(p.x) - p.y, 2)).sum / n)

      ApproximationResult(
        methodName = name,
        equation = f"y = $a%.3fx³ + $b%.3fx² + $c%.3fx + $d%.3f",
        coefficients = Seq(a, b, c, d),
        mse = mse,
        rSquared = calculateRSquared(points, f),
        message = Message.Success
      )
    } catch {
      case _: Exception => ApproximationResult(methodName = name, message = Message.SingularMatrix)
    }
  }
}