package solver.nonlinear.functions

import solver.core.Message

case class FunctionResult(
  x: Double,
  fx: Double,
  iterations: Int,
  message: Message
)