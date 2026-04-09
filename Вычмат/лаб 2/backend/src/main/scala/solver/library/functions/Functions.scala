package solver.library.functions

import solver.library.functions.FunctionPack

object Functions {

  val p1: FunctionPack = FunctionPack(
    f = x => 2.74 * x * x * x - 1.93 * x * x - 15.28 * x - 3.72,
    d1f = x => 3 * 2.74 * x * x - 2 * 1.93 * x - 15.28,
    d2f = x => 6 * 2.74 * x - 2 * 1.93
  )

  val p2: FunctionPack = FunctionPack(
    f = x => -1.38 * x * x * x - 5.42 * x * x + 2.57 * x + 10.95,
    d1f = x => -3 * 1.38 * x * x - 2 * 5.42 * x + 2.57,
    d2f = x => -6 * 1.38 * x - 2 * 5.42
  )

  val p3: FunctionPack = FunctionPack(
    f = x => x * x * x + 2.84 * x * x - 5.606 * x - 14.766,
    d1f = x => 3 * x * x + 2 * 2.84 * x - 5.606,
    d2f = x => 6 * x + 2 * 2.84
  )

  val p4: FunctionPack = FunctionPack(
    f = x => x * x * x - 1.89 * x * x - 2 * x + 1.76,
    d1f = x => 3 * x * x - 2 * 1.89 * x - 2,
    d2f = x => 6 * x - 2 * 1.89
  )

  val p5: FunctionPack = FunctionPack(
    f = x => -2.7 * x * x * x - 1.48 * x * x + 19.23 * x + 6.35,
    d1f = x => -3 * 2.7 * x * x - 2 * 1.48 * x + 19.23,
    d2f = x => -6 * 2.7 * x - 2 * 1.48
  )

  val p6: FunctionPack = FunctionPack(
    f = x => 2 * x * x * x + 3.41 * x * x - 23.74 * x + 2.95,
    d1f = x => 6 * x * x + 2 * 3.41 * x - 23.74,
    d2f = x => 12 * x + 2 * 3.41
  )

  val p7: FunctionPack = FunctionPack(
    f = x => x * x * x + 2.28 * x * x - 1.934 * x - 3.907,
    d1f = x => 3 * x * x + 2 * 2.28 * x - 1.934,
    d2f = x => 6 * x + 2 * 2.28
  )

  val p8: FunctionPack = FunctionPack(
    f = x => 3 * x * x * x + 1.7 * x * x - 15.42 * x + 6.89,
    d1f = x => 9 * x * x + 2 * 1.7 * x - 15.42,
    d2f = x => 18 * x + 2 * 1.7
  )

  val p9: FunctionPack = FunctionPack(
    f = x => -1.8 * x * x * x - 2.94 * x * x + 10.37 * x + 5.38,
    d1f = x => -3 * 1.8 * x * x - 2 * 2.94 * x + 10.37,
    d2f = x => -6 * 1.8 * x - 2 * 2.94
  )

  val p10: FunctionPack = FunctionPack(
    f = x => x * x * x - 3.125 * x * x - 3.5 * x + 2.458,
    d1f = x => 3 * x * x - 2 * 3.125 * x - 3.5,
    d2f = x => 6 * x - 2 * 3.125
  )

  val p11: FunctionPack = FunctionPack(
    f = x => 4.45 * x * x * x + 7.81 * x * x - 9.62 * x - 8.17,
    d1f = x => 3 * 4.45 * x * x + 2 * 7.81 * x - 9.62,
    d2f = x => 6 * 4.45 * x + 2 * 7.81
  )

  val p12: FunctionPack = FunctionPack(
    f = x => x * x * x - 4.5 * x * x - 9.21 * x - 0.383,
    d1f = x => 3 * x * x - 2 * 4.5 * x - 9.21,
    d2f = x => 6 * x - 2 * 4.5
  )

  val p13: FunctionPack = FunctionPack(
    f = x => x * x * x + 4.81 * x * x - 17.37 * x + 5.38,
    d1f = x => 3 * x * x + 2 * 4.81 * x - 17.37,
    d2f = x => 6 * x + 2 * 4.81
  )

  val p14: FunctionPack = FunctionPack(
    f = x => 2.3 * x * x * x + 5.75 * x * x - 7.41 * x - 10.6,
    d1f = x => 3 * 2.3 * x * x + 2 * 5.75 * x - 7.41,
    d2f = x => 6 * 2.3 * x + 2 * 5.75
  )

  val p15: FunctionPack = FunctionPack(
    f = x => -2.4 * x * x * x + 1.27 * x * x + 8.63 * x + 2.31,
    d1f = x => -3 * 2.4 * x * x + 2 * 1.27 * x + 8.63,
    d2f = x => -6 * 2.4 * x + 2 * 1.27
  )

  val p16: FunctionPack = FunctionPack(
    f = x => 5.74 * x * x * x - 2.95 * x * x - 10.28 * x - 3.23,
    d1f = x => 3 * 5.74 * x * x - 2 * 2.95 * x - 10.28,
    d2f = x => 6 * 5.74 * x - 2 * 2.95
  )

  val p17: FunctionPack = FunctionPack(
    f = x => -0.38 * x * x * x - 3.42 * x * x + 2.51 * x + 8.75,
    d1f = x => -3 * 0.38 * x * x - 2 * 3.42 * x + 2.51,
    d2f = x => -6 * 0.38 * x - 2 * 3.42
  )

  val p18: FunctionPack = FunctionPack(
    f = x => x * x * x + 2.64 * x * x - 5.41 * x - 11.76,
    d1f = x => 3 * x * x + 2 * 2.64 * x - 5.41,
    d2f = x => 6 * x + 2 * 2.64
  )

  val p19: FunctionPack = FunctionPack(
    f = x => 2 * x * x * x - 1.89 * x * x - 5 * x + 2.34,
    d1f = x => 6 * x * x - 2 * 1.89 * x - 5,
    d2f = x => 12 * x - 2 * 1.89
  )

  val p20: FunctionPack = FunctionPack(
    f = x => -2.8 * x * x * x - 3.48 * x * x + 10.23 * x + 9.35,
    d1f = x => -3 * 2.8 * x * x - 2 * 3.48 * x + 10.23,
    d2f = x => -6 * 2.8 * x - 2 * 3.48
  )

  val p21: FunctionPack = FunctionPack(
    f = x => 1.8 * x * x * x - 2.47 * x * x - 5.53 * x + 1.539,
    d1f = x => 3 * 1.8 * x * x - 2 * 2.47 * x - 5.53,
    d2f = x => 6 * 1.8 * x - 2 * 2.47
  )

  val p22: FunctionPack = FunctionPack(
    f = x => x * x * x - 3.78 * x * x + 1.25 * x + 3.49,
    d1f = x => 3 * x * x - 2 * 3.78 * x + 1.25,
    d2f = x => 6 * x - 2 * 3.78
  )

  val p23: FunctionPack = FunctionPack(
    f = x => -x * x * x + 5.67 * x * x - 7.12 * x + 1.34,
    d1f = x => -3 * x * x + 2 * 5.67 * x - 7.12,
    d2f = x => -6 * x + 2 * 5.67
  )

  val p24: FunctionPack = FunctionPack(
    f = x => x * x * x - 2.92 * x * x + 1.435 * x + 0.791,
    d1f = x => 3 * x * x - 2 * 2.92 * x + 1.435,
    d2f = x => 6 * x - 2 * 2.92
  )

  val p25: FunctionPack = FunctionPack(
    f = x => x * x * x - 2.56 * x * x - 1.325 * x + 4.395,
    d1f = x => 3 * x * x - 2 * 2.56 * x - 1.325,
    d2f = x => 6 * x - 2 * 2.56
  )

  val p26: FunctionPack = FunctionPack(
    f = x => x * x * x - 2.18 * x * x - 3.27 * x + 1.43,
    d1f = x => 3 * x * x - 2 * 2.18 * x - 3.27,
    d2f = x => 6 * x - 2 * 2.18
  )
}