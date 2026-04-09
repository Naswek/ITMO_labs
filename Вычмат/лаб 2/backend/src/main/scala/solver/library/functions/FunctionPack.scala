package solver.library.functions 

case class  FunctionPack (
    f: Double => Double,
    d1f: Double => Double,
    d2f: Double => Double
)