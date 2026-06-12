package solver.approximation.models

import solver.approximation.functions.{ApproximationResult, Point}
import solver.core.Message

case class ApproximationResponse(
  success: Boolean,  
  results: Seq[ApproximationResult], 
  bestMethod: String,
  sourcePoints: Seq[Point],   
  message: String       
)

case class SampleResponse (
  functions: List[String],
  methods: List[String]
)