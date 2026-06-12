package solver.interpolation.models

case class InterpolationRequest(
  isGenerate: Boolean,
  functionId: Int,
  a: Double,
  b: Double,
  h: Double,
  targetX: Double,
  x: List[Double],
  y: List[Double]
)

case class SampleRequest(
  interpolationFunctions: List[String],
  methods: List[String]
)
