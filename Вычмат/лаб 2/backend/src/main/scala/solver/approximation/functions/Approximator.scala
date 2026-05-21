package solver.approximation.functions

import solver.approximation.functions.{Point, ApproximationResult}

trait Approximator {
  def name: String

  def solve(points: Seq[Point]): ApproximationResult

  protected def calculateRSquared(points: Seq[Point], f: Double => Double): Double = {
    val meanY = points.map(_.y).sum / points.size
    val ssRes = points.map(p => math.pow(p.y - f(p.x), 2)).sum
    val ssTot = points.map(p => math.pow(p.y - meanY, 2)).sum
    if (ssTot == 0) 1.0 else 1 - (ssRes / ssTot)
  }
}