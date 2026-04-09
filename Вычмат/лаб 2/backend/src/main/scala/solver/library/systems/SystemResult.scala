package solver.library.systems

case class SystemResult(
  x: Array[Double],
  fx: Array[Double],
  iterations: Int,
  message: Option[String]
)