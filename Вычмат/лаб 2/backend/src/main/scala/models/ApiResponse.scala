package models

case class FunctionApiResponse(
  success: Boolean,
  data: Option[FunctionResponse],
  error: Option[String]
)

case class SystemApiResponse(
  success: Boolean,
  data: Option[SystemResponse],
  error: Option[String]
)