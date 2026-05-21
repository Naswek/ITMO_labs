package solver.approximation

import solver.approximation.functions.{FunctionPack, Functions, Approximator}
import solver.approximation.approximators.{Cubic, Exponential, Grade, Linear, Logarithmic, Quadratic}

object Library {
    val functions: Map[Int, FunctionPack] = Map(
      0 -> Functions.p1,
      1 -> Functions.p2,
      2 -> Functions.p3,
      3 -> Functions.p4,
      4 -> Functions.p5,
      5 -> Functions.p6,
      6 -> Functions.p7,
      7 -> Functions.p8,
      8 -> Functions.p9,
      9 -> Functions.p10,
      10 -> Functions.p11,
      11 -> Functions.p12,
      12 -> Functions.p13,
      13 -> Functions.p14,
      14 -> Functions.p15
    )


    val approximationFunctions: Map[Int, Approximator] = Map(
        0 -> new Cubic,
        1 -> new Exponential,
        2 -> new Grade,
        3 -> new Linear,
        4 -> new Logarithmic,
        5 -> new Quadratic
    )
}