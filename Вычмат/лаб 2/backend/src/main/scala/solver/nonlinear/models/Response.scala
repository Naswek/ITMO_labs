package solver.nonlinear.models

case class FunctionResponse(
    success: Boolean,
    x: Double, 
    fx: Double, 
    iterations: Int,
    message: String
)

case class SystemResponse(
    success: Boolean,
    x: Array[Double], 
    fx: Array[Double], 
    iterations: Int,
    message: String 
)

case class SamplesResponse(
  functions: List[String],
  systems: List[List[String]],
  methods: MethodsResponse
)

case class MethodsResponse(
  function: List[String],
  system: List[String]
)