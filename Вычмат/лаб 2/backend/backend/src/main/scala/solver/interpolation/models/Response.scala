package solver.interpolation.models

import solver.core.Message

case class InterpolationResponse(
  success: Boolean,
  results: Seq[InterpolationResult],
  bestMethod: String,
  sourcePoints: Seq[Point],
  message: String
)

case class SampleResponse(
  functions: List[String],
  methods: List[String]
)
