package solver.integrals.models

case class IntegralRequest(
    integralId: Int,
    methodId: Int,
    a: Double, 
    b: Double, 
    epsilon: Double 
)
