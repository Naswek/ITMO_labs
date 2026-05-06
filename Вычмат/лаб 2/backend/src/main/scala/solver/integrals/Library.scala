package solver.integrals

import solver.integrals.integrals.{IntegralsPack, Integrals, IntegrationMethod}
import solver.integrals.methods.{RectangleMethodRight, RectangleMethodLeft, RectangleMethodMid, SimpsonMethod, TrapezoidMethod, MonteCarloMethod}

object Library {
    val integralsPack: Map[Int, IntegralsPack] = Map(
      0 -> Integrals.i1,
      1 -> Integrals.i2,
      2 -> Integrals.i3,
      3 -> Integrals.i4,
      4 -> Integrals.i5,
      5 -> Integrals.i6,
      6 -> Integrals.i7,
      7 -> Integrals.i8,
      8 -> Integrals.i9,
      9 -> Integrals.i10,
      10 -> Integrals.i11,
      11 -> Integrals.i12,
      12 -> Integrals.i13,
      13 -> Integrals.i14,
      14 -> Integrals.i15,
      15 -> Integrals.i16,
      16 -> Integrals.i17,
      17 -> Integrals.i18,
      18 -> Integrals.i19,
      19 -> Integrals.i20,
      20 -> Integrals.i21,
      21 -> Integrals.i22,
      22 -> Integrals.i23,
      23 -> Integrals.i24,
      24 -> Integrals.i25,
      25 -> Integrals.i26,
      26 -> Integrals.i27,
      27 -> Integrals.i28,
      28 -> Integrals.i29,
      29 -> Integrals.i30
    )


    val integralsMethods: Map[Int, IntegrationMethod] = Map(
        0 -> new TrapezoidMethod,
        1 -> new SimpsonMethod,
        2 -> new MonteCarloMethod,
        3 -> new RectangleMethodLeft,
        4 -> new RectangleMethodMid,
        5 -> new RectangleMethodRight
    )
}