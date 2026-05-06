package solver.integrals.integrals

import solver.integrals.integrals.IntegralsPack
import scala.math._

object Integrals {

  val i1: IntegralsPack = IntegralsPack(
    f = x => -x*x*x - x*x - 2*x + 1
  )

  val i2: IntegralsPack = IntegralsPack(
    f = x => -3*x*x*x - 5*x*x + 4*x - 2
  )

  val i3: IntegralsPack = IntegralsPack(
    f = x => -x*x*x - x*x + x + 3
  )

  val i4: IntegralsPack = IntegralsPack(
    f = x => -2*x*x*x - 4*x*x + 8*x - 4
  )

  val i5: IntegralsPack = IntegralsPack(
    f = x => -2*x*x*x - 3*x*x + x + 5
  )

  val i6: IntegralsPack = IntegralsPack(
    f = x => 3*x*x*x + 5*x*x + 3*x - 6
  )

  val i7: IntegralsPack = IntegralsPack(
    f = x => 4*x*x*x - 5*x*x + 6*x - 7
  )

  val i8: IntegralsPack = IntegralsPack(
    f = x => 3*x*x*x - 2*x*x - 7*x - 8
  )

  val i9: IntegralsPack = IntegralsPack(
    f = x => 2*x*x*x - 3*x*x + 5*x - 9
  )

  val i10: IntegralsPack = IntegralsPack(
    f = x => x*x*x - 3*x*x + 7*x - 10
  )

  val i11: IntegralsPack = IntegralsPack(
    f = x => 2*x*x*x - 9*x*x - 7*x + 11
  )

  val i12: IntegralsPack = IntegralsPack(
    f = x => x*x*x + 2*x*x - 3*x - 12
  )

  val i13: IntegralsPack = IntegralsPack(
    f = x => -2*x*x*x - 5*x*x + 7*x - 13
  )

  val i14: IntegralsPack = IntegralsPack(
    f = x => 2*x*x*x - 2*x*x + 7*x - 14
  )

  val i15: IntegralsPack = IntegralsPack(
    f = x => 5*x*x*x - 2*x*x + 3*x - 15
  )

  val i16: IntegralsPack = IntegralsPack(
    f = x => 3*x*x*x - 4*x*x + 5*x - 16
  )

  val i17: IntegralsPack = IntegralsPack(
    f = x => 3*x*x*x - 4*x*x + 7*x - 17
  )

  val i18: IntegralsPack = IntegralsPack(
    f = x => x*x*x - 5*x*x + 3*x - 16
  )

  val i19: IntegralsPack = IntegralsPack(
    f = x => x*x*x - 3*x*x + 6*x - 19
  )

  val i20: IntegralsPack = IntegralsPack(
    f = x => 4*x*x*x - 3*x*x + 5*x - 20
  )

  val i21: IntegralsPack = IntegralsPack(
    f = x => 2*x*x*x - 5*x*x - 3*x + 21
  )

  val i22: IntegralsPack = IntegralsPack(
    f = x => 2*x*x*x - 3*x*x + 4*x - 22
  )

  val i23: IntegralsPack = IntegralsPack(
    f = x => -x*x*x - 2*x*x + 3*x + 23
  )

  val i24: IntegralsPack = IntegralsPack(
    f = x => x*x*x - 2*x*x - 5*x + 24
  )

  val i25: IntegralsPack = IntegralsPack(
    f = x => 2*x*x*x - 4*x*x + 6*x - 25
  )

  val i26: IntegralsPack = IntegralsPack(
    f = x => 3*x*x*x - 2*x*x + 7*x + 26
  )
  
  val i27: IntegralsPack = IntegralsPack(
    f = x => 2*x*x*x - 5*x*x - 3*x + 21
  )

  val i28: IntegralsPack = IntegralsPack(
    f = x => 1/x
  )
  
  val i29: IntegralsPack = IntegralsPack(
    f = x => 1/sqrt(x)
  )
  
  val i30: IntegralsPack = IntegralsPack(
    f = x => 1/(x*x)
  )
}