package routes.formats

import spray.json._
import DefaultJsonProtocol._
import solver.approximation.models._
import solver.approximation.samples._
import solver.core.Message
import solver.approximation.functions.{ApproximationResult, Point}

object JsonFormatsApproximation extends DefaultJsonProtocol {

  implicit val messageFormat: RootJsonFormat[Message] = new RootJsonFormat[Message] {
    def write(obj: Message): JsValue = JsString(obj.toString)
    def read(json: JsValue): Message = json match {
      case JsString(value) => Message.valueOf(value)
      case _ => throw DeserializationException("Сообщение должно быть стринг")
      }
    }

  implicit val pointFormat: RootJsonFormat[Point] = 
    jsonFormat2(Point)

  implicit val approximationResultFormat: RootJsonFormat[ApproximationResult] = 
    jsonFormat7(ApproximationResult)
      
  implicit val approximationRequestFormat: RootJsonFormat[ApproximationRequest] =
    jsonFormat7(ApproximationRequest)
  
  implicit val approximationResponseFormat: RootJsonFormat[ApproximationResponse] =
    jsonFormat5(ApproximationResponse)
  
  implicit val samplesResponseFormat: RootJsonFormat[SampleResponse] =
    jsonFormat2(SampleResponse)






}