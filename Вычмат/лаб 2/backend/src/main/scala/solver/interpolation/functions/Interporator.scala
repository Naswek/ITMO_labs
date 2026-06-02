package solver.interpolation.functions

import solver.interpolation.models.{Point, InterpolationResult}

trait Interpolator {
  def name: String
  def solve(points: Seq[Point], targetX: Double): InterpolationResult
}