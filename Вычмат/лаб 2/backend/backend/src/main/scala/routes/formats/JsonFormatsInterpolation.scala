package routes.formats

import spray.json._
import DefaultJsonProtocol._
import solver.interpolation.models._
import solver.interpolation.samples._
import solver.core.Message

object JsonFormatsInterpolation extends DefaultJsonProtocol {

  implicit val messageFormat: RootJsonFormat[Message] = new RootJsonFormat[Message] {
    def write(obj: Message): JsValue = JsString(obj.toString)
    def read(json: JsValue): Message = json match {
      case JsString(value) => Message.valueOf(value)
      case _ => throw DeserializationException("Сообщение должно быть стринг")
    }
  }

  implicit val pointFormat: RootJsonFormat[Point] = 
    jsonFormat2(Point.apply)

  implicit val interpolationResultFormat: RootJsonFormat[InterpolationResult] = 
    jsonFormat6(InterpolationResult.apply)
      
  implicit val interpolationRequestFormat: RootJsonFormat[InterpolationRequest] =
    jsonFormat8(InterpolationRequest.apply)
  
  implicit val interpolationResponseFormat: RootJsonFormat[InterpolationResponse] =
    jsonFormat5(InterpolationResponse.apply)
  
  implicit val samplesResponseFormat: RootJsonFormat[SampleResponse] =
    jsonFormat2(SampleResponse.apply)
}
