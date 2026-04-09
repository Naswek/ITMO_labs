package solver.library.systems

import solver.library.systems.Utils._

object Systems {

  val sys1: SystemPack = {
    val f: Array[Double] => Array[Double] = x => Array(
      math.sin(x(0) + 1) - x(1) - 1.2,
      2 * x(0) + math.cos(x(1)) - 2
    )
    SystemPack(f, numericalJacobian(f, _))
  }

  val sys2: SystemPack = {
    val f: Array[Double] => Array[Double] = x => Array(
      math.tan(x(0) * x(1) + 0.1) - x(0) * x(0),
      x(0) * x(0) + 2 * x(1) * x(1) - 1
    )
    SystemPack(f, numericalJacobian(f, _))
  }

  val sys3: SystemPack = {
    val f: Array[Double] => Array[Double] = x => Array(
      math.cos(x(0) - 1) + x(1) - 0.5,
      x(0) - math.cos(x(1)) - 3
    )
    SystemPack(f, numericalJacobian(f, _))
  }

  val sys4: SystemPack = {
    val f: Array[Double] => Array[Double] = x => Array(
      math.sin(x(0) + x(1)) - 1.2 * x(0) - 0.2,
      x(0) * x(0) + 2 * x(1) * x(1) - 1
    )
    SystemPack(f, numericalJacobian(f, _))
  }

  val sys5: SystemPack = {
    val f: Array[Double] => Array[Double] = x => Array(
      math.tan(x(0) * x(1) + 0.3) - x(0) * x(0),
      0.9 * x(0) * x(0) + 2 * x(1) * x(1) - 1
    )
    SystemPack(f, numericalJacobian(f, _))
  }

  val sys6: SystemPack = {
    val f: Array[Double] => Array[Double] = x => Array(
      math.sin(x(0)) + 2 * x(1) - 2,
      x(0) + math.cos(x(1) - 1) - 0.7
    )
    SystemPack(f, numericalJacobian(f, _))
  }

  val sys7: SystemPack = {
    val f: Array[Double] => Array[Double] = x => Array(
      2 * x(0) - math.sin(x(1) - 0.5) - 1,
      x(1) + math.cos(x(0)) - 1.5
    )
    SystemPack(f, numericalJacobian(f, _))
  }

  val sys8: SystemPack = {
    val f: Array[Double] => Array[Double] = x => Array(
      math.tan(x(0) * x(1)) - x(0) * x(0),
      0.8 * x(0) * x(0) + 2 * x(1) * x(1) - 1
    )
    SystemPack(f, numericalJacobian(f, _))
  }

  val sys9: SystemPack = {
    val f: Array[Double] => Array[Double] = x => Array(
      math.sin(x(0) + x(1)) - 1.5 * x(0) + 0.1,
      x(0) * x(0) + 2 * x(1) * x(1) - 1
    )
    SystemPack(f, numericalJacobian(f, _))
  }

  val sys10: SystemPack = {
    val f: Array[Double] => Array[Double] = x => Array(
      math.sin(x(0) + 0.5) - x(1) - 1,
      math.cos(x(1) - 2) + x(0)
    )
    SystemPack(f, numericalJacobian(f, _))
  }

  val sys11: SystemPack = {
    val f: Array[Double] => Array[Double] = x => Array(
      math.tan(x(0) * x(1) + 0.2) - x(0) * x(0),
      x(0) * x(0) + 2 * x(1) * x(1) - 1
    )
    SystemPack(f, numericalJacobian(f, _))
  }

  val sys12: SystemPack = {
    val f: Array[Double] => Array[Double] = x => Array(
      x(0) + math.sin(x(1)) + 0.4,
      2 * x(1) - math.cos(x(0) + 1)
    )
    SystemPack(f, numericalJacobian(f, _))
  }

  val sys13: SystemPack = {
    val f: Array[Double] => Array[Double] = x => Array(
      math.sin(x(1)) + 2 * x(0) - 2,
      x(1) + math.cos(x(0) - 1) - 0.7
    )
    SystemPack(f, numericalJacobian(f, _))
  }

  val sys14: SystemPack = {
    val f: Array[Double] => Array[Double] = x => Array(
      math.sin(x(0) + x(1)) - 1.4 * x(0),
      x(0) * x(0) + x(1) * x(1) - 1
    )
    SystemPack(f, numericalJacobian(f, _))
  }

  val sys15: SystemPack = {
    val f: Array[Double] => Array[Double] = x => Array(
      math.sin(x(0) - 1) + x(1) - 1.5,
      x(0) - math.sin(x(1) + 1) - 1
    )
    SystemPack(f, numericalJacobian(f, _))
  }

  val sys16: SystemPack = {
    val f: Array[Double] => Array[Double] = x => Array(
      x(1) - math.cos(x(0)) - 2,
      x(0) + math.cos(x(1) - 1) - 0.8
    )
    SystemPack(f, numericalJacobian(f, _))
  }

  val sys17: SystemPack = {
    val f: Array[Double] => Array[Double] = x => Array(
      math.tan(x(0) * x(1)) - x(0) * x(0),
      0.5 * x(0) * x(0) + 2 * x(1) * x(1) - 1
    )
    SystemPack(f, numericalJacobian(f, _))
  }

  val sys18: SystemPack = {
    val f: Array[Double] => Array[Double] = x => Array(
      math.sin(x(1) + 2) - x(0) - 1.5,
      x(1) + math.cos(x(0) - 2) - 0.5
    )
    SystemPack(f, numericalJacobian(f, _))
  }

  val sys19: SystemPack = {
    val f: Array[Double] => Array[Double] = x => Array(
      math.sin(x(1) - 1) + x(0) - 1.3,
      x(1) - math.sin(x(0) + 1) - 0.8
    )
    SystemPack(f, numericalJacobian(f, _))
  }

  val sys20: SystemPack = {
    val f: Array[Double] => Array[Double] = x => Array(
      math.sin(x(0) + x(1)) - 1.1 * x(0) - 0.1,
      x(0) * x(0) + x(1) * x(1) - 1
    )
    SystemPack(f, numericalJacobian(f, _))
  }

  val sys21: SystemPack = {
    val f: Array[Double] => Array[Double] = x => Array(
      math.cos(x(1)) + x(0) - 1.5,
      2 * x(1) - math.sin(x(0) - 0.5) - 1
    )
    SystemPack(f, numericalJacobian(f, _))
  }

  val sys22: SystemPack = {
    val f: Array[Double] => Array[Double] = x => Array(
      math.tan(x(0) * x(1) + 0.3) - x(0) * x(0),
      0.5 * x(0) * x(0) + 2 * x(1) * x(1) - 1
    )
    SystemPack(f, numericalJacobian(f, _))
  }

  val sys23: SystemPack = {
    val f: Array[Double] => Array[Double] = x => Array(
      math.sin(x(1) + 0.5) - x(0) - 1,
      x(1) + math.cos(x(0) - 2)
    )
    SystemPack(f, numericalJacobian(f, _))
  }

  val sys24: SystemPack = {
    val f: Array[Double] => Array[Double] = x => Array(
      math.sin(x(0) - x(1)) - x(0) * x(1) + 1,
      0.3 * x(0) * x(0) + x(1) * x(1) - 2
    )
    SystemPack(f, numericalJacobian(f, _))
  }

  val sys25: SystemPack = {
    val f: Array[Double] => Array[Double] = x => Array(
      math.cos(x(0) + 0.5) + x(1) - 1,
      math.sin(x(1)) - 2 * x(0) - 2
    )
    SystemPack(f, numericalJacobian(f, _))
  }

  val sys26: SystemPack = {
    val f: Array[Double] => Array[Double] = x => Array(
      math.cos(x(0) + 0.5) + x(1) - 0.7,
      math.sin(x(1)) - 0.5 * x(0) - 2
    )
    SystemPack(f, numericalJacobian(f, _))
  }
}