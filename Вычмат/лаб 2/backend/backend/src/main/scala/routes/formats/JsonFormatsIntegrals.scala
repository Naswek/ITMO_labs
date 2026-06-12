package routes.formats

import spray.json._
import DefaultJsonProtocol._
import solver.integrals.models._
import solver.integrals.samples._

object JsonFormatsIntegrals extends DefaultJsonProtocol {

  implicit val integralRequestFormat: RootJsonFormat[IntegralRequest] =
    jsonFormat5(IntegralRequest)
  
  implicit val integralResponseFormat: RootJsonFormat[IntegralResponse] =
    jsonFormat4(IntegralResponse)
  
  implicit val samplesResponseFormat: RootJsonFormat[SamplesResponse] =
    jsonFormat2(SamplesResponse)
}