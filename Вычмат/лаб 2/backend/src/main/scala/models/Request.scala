package models

case class FunctionRequest(
    functionId: Int,
    methodId: Int,
    a: Double, 
    b: Double, 
    epsilon: Double 
)

case class SystemRequest(
    systemId: Int,
    methodId: Int,
    x0: Array[Double],
    epsilon: Double
    
)