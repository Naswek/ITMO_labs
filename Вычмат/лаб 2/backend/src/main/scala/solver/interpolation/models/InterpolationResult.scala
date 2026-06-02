package solver.interpolation.models

import solver.core.Message

case class InterpolationResult(
  methodName: String,
  value: Double,                    
  differenceTable: Seq[Seq[Double]], 
  message: Message,
  isExtrapolated: Boolean = false
)
