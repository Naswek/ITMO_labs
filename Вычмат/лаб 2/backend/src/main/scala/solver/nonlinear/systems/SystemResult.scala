package solver.nonlinear.systems  

import solver.core.Message

case class SystemResult(
  x: Array[Double],
  fx: Array[Double],
  iterations: Int,
  message: Message
)