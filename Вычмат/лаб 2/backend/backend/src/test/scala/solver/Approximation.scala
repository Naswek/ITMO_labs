package solver.approximation

import org.scalatest.propspec.AnyPropSpec
import org.scalatestplus.scalacheck.Checkers
import org.scalacheck.Prop.forAll
import org.scalacheck.Gen
import solver.approximation.approximators._
import solver.approximation.functions._
import solver.core.Message

class Approximation extends AnyPropSpec with Checkers {

  val allModels = Seq(
    new Linear(), new Quadratic(), new Cubic(),
    new Exponential(), new Logarithmic(), new Grade()
  )

  val pointsGen: Gen[Seq[Point]] = Gen.choose(5, 15).flatMap { n =>
    Gen.listOfN(n, for {
      x <- Gen.choose(-50.0, 50.0)
      y <- Gen.choose(-50.0, 50.0)
    } yield Point(x, y))
  }

  property("Методы никогда не должны выбрасывать исключения") {
    check(forAll(pointsGen) { points =>
      allModels.forall { model =>
        try { model.solve(points); true } catch { case _: Exception => false }
      }
    })
  }

  property("MSE всегда неотрицательно") {
    check(forAll(pointsGen) { points =>
      allModels.forall { model =>
        val res = model.solve(points)
        if (res.message == Message.Success) res.mse >= 0 else true
      }
    })
  }

  property("Коэффициент детерминации R^2 не должен превышать 1") {
    check(forAll(pointsGen) { points =>
      allModels.forall { model =>
        val res = model.solve(points)
        if (res.message == Message.Success) {
          res.rSquared <= 1.0000001 
        } else true
      }
    })
  }

  property("Log и Grade должны корректно обрабатывать невалидные данные") {
    val negativeDataGen = Gen.choose(5, 10).flatMap { n =>
      Gen.listOfN(n, Gen.choose(-10.0, -0.1).map(v => Point(v, v)))
    }
    check(forAll(negativeDataGen) { points =>
      val logRes = new Logarithmic().solve(points)
      val gradeRes = new Grade().solve(points)
      Seq(Message.InvalidDataForModel, Message.NotEnoughPoints).contains(logRes.message) &&
      Seq(Message.InvalidDataForModel, Message.NotEnoughPoints).contains(gradeRes.message)
    })
  }

  property("Методы должны возвращать ошибку, если данные вырожденные (все X одинаковы)") {
    val sameXGen = for {
      x <- Gen.choose(-10.0, 10.0)
      n <- Gen.choose(5, 10)
    } yield Seq.fill(n)(Point(x, 1.0))

    check(forAll(sameXGen) { points =>
      val linearRes = new Linear().solve(points)
      Seq(Message.SingularMatrix, Message.DivisionByZero, Message.NotEnoughPoints).contains(linearRes.message)
    })
  }
}