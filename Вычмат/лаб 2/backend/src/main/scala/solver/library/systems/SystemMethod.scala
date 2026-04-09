package solver.library.systems

trait SystemMethod {
  def solve(system: SystemPack, x0: Array[Double], eps: Double): SystemResult
}