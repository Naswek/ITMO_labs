package solver.approximation.functions

import solver.core.Message

case class ApproximationResult(
  methodName: String, 
  equation: String = "",         
  coefficients: Seq[Double] = Nil, 
  mse: Double = 0.0,               
  rSquared: Double = 0.0,               
  pearson: Option[Double] = None, 
  message: Message
)

case class Point(x: Double, y: Double)