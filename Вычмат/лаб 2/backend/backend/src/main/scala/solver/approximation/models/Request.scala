package solver.approximation.models

case class ApproximationRequest(
  isGenerate: Boolean,
  functionId: Int, 
  a: Double,
  b: Double,
  h: Double,
  x: List[Double],
  y: List[Double]
  
)

case class SampleRequest (
    approxFunctions: List[String],
    methods: List[String]
)