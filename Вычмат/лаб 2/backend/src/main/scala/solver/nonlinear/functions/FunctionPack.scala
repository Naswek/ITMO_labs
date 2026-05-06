package solver.nonlinear.functions 

case class  FunctionPack (
    f: Double => Double,
    d1f: Double => Double,
    d2f: Double => Double
)