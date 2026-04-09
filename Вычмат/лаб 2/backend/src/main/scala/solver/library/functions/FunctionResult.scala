package solver.library.functions

case class FunctionResult(
  x: Double,
  fx: Double,
  iterations: Int,
  message: Option[String]
)