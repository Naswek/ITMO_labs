package solver.interpolation

import solver.interpolation.functions.{InterpolationPack, Functions, Interpolator}
import solver.interpolation.methods.{Lagrange, NewtonDivided, NewtonFinite, Gauss, Stirling, Bessel}

object Library {
    val functions: Map[Int, InterpolationPack] = Map(
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

    val interpolationFunctions: Map[Int, Interpolator] = Map(
      0 -> new Lagrange,
      1 -> new NewtonDivided,
      2 -> new NewtonFinite,
      3 -> new Gauss,
      4 -> new Stirling,
      5 -> new Bessel
    )
}
