package solver.approximation.models

case class ApproximationRequest(
  functionId: Int, 
  a: Double,
  b: Double,
  h: Double
)

case class SampleRequest (
    approxFunctions: List[String],
    methods: List[String]
)