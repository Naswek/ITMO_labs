package routes.formats

import spray.json._
import DefaultJsonProtocol._
import solver.nonlinear.models._
import solver.nonlinear.samples._

object JsonFormatsNonLinear extends DefaultJsonProtocol {

  implicit val functionRequestFormat: RootJsonFormat[FunctionRequest] =
    jsonFormat5(FunctionRequest)
  
  implicit val functionResponseFormat: RootJsonFormat[FunctionResponse] =
    jsonFormat5(FunctionResponse)
  
  implicit val systemRequestFormat: RootJsonFormat[SystemRequest] =
    jsonFormat4(SystemRequest)
  
  implicit val systemResponseFormat: RootJsonFormat[SystemResponse] =
    jsonFormat5(SystemResponse)
  
  implicit val methodsResponseFormat: RootJsonFormat[MethodsResponse] =
    jsonFormat2(MethodsResponse)
  
  implicit val samplesResponseFormat: RootJsonFormat[SamplesResponse] =
    jsonFormat3(SamplesResponse)

}