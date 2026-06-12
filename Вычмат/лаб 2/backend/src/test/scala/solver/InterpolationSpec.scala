package solver.interpolation

import org.scalatest.funsuite.AnyFunSuite
import solver.interpolation.methods._
import solver.interpolation.models.Point
import solver.core.Message

class InterpolationSpec extends AnyFunSuite {
  test("Метод Стирлинга с одной точкой, сгенерированной при a=0, b=0, h=0.2") {
    val points = Seq(Point(0.0, 3.0)) // одна точка
    val solver = new Stirling()
    val result = solver.solve(points, 1.0)
    assert(result.message == Message.NotEnoughPoints)
    println(s"Случай с одной точкой: значение = ${result.value}, сообщение = ${result.message}")
  }

  test("Метод Стирлинга с точками на отрезке [0, 2], h=0.2, targetX = 1.0 (точное совпадение)") {
    // f(x) = 2x + 3. При 0.0 -> 3.0, 0.2 -> 3.4, ..., 1.0 -> 5.0, ..., 2.0 -> 7.0
    val points = (0 to 10).map(i => Point(i * 0.2, 2.0 * (i * 0.2) + 3))
    val solver = new Stirling()
    val result = solver.solve(points, 1.0)
    assert(result.value == 5.0)
    assert(result.message == Message.Success)
    println(s"Точное совпадение (targetX = 1.0): значение = ${result.value}, сообщение = ${result.message}")
  }

  test("Метод Стирлинга с точками на отрезке [0, 2], h=0.2, targetX = 1.1 (неточное совпадение)") {
    val points = (0 to 10).map(i => Point(i * 0.2, 2.0 * (i * 0.2) + 3))
    val solver = new Stirling()
    val result = solver.solve(points, 1.1)
    assert(result.value == 5.2)
    assert(result.message == Message.Success)
    println(s"Неточное совпадение (targetX = 1.1): значение = ${result.value}, сообщение = ${result.message}")
  }

  test("Проверка логики определения экстраполяции") {
    val points = (0 to 5).map(i => Point(i.toDouble, i.toDouble * 2))
    
    val targetXInside = 2.5
    val targetXOutsideLeft = -0.5
    val targetXOutsideRight = 5.5
    
    val isExtrapolatedInside = targetXInside < points.head.x || targetXInside > points.last.x
    val isExtrapolatedOutsideLeft = targetXOutsideLeft < points.head.x || targetXOutsideLeft > points.last.x
    val isExtrapolatedOutsideRight = targetXOutsideRight < points.head.x || targetXOutsideRight > points.last.x
    
    assert(!isExtrapolatedInside)
    assert(isExtrapolatedOutsideLeft)
    assert(isExtrapolatedOutsideRight)
  }

  test("Проверка определения неопределённых точек (например, ln(x) на [-1, 1])") {
    val f: Double => Double = math.log
    
    def hasUndefinedPoints(func: Double => Double, a: Double, b: Double, h: Double): Boolean = {
      if (a > b || h <= 0) return false
      (BigDecimal(a) to BigDecimal(b) by BigDecimal(h)).exists { x =>
        val yVal = func(x.toDouble)
        yVal.isNaN || yVal.isInfinite
      }
    }
    
    val undefinedRes = hasUndefinedPoints(f, -1.0, 1.0, 0.2)
    val definedRes = hasUndefinedPoints(f, 0.1, 1.1, 0.2)
    
    assert(undefinedRes) // log(-1.0) даёт NaN, поэтому неопределённая точка должна быть обнаружена
    assert(!definedRes) // log(x) определён для x на [0.1, 1.1]
  }
}
