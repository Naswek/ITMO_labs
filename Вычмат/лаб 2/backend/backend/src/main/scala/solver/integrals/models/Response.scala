package solver.integrals.models

import solver.core.Message

case class IntegralResponse(
    success: Boolean,
    value: Double, 
    n: Int, 
    message: String
)

case class SamplesResponse(
  integrals: List[String],
  methods: List[String]
)
