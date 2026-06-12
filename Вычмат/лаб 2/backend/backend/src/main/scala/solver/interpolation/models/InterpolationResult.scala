package solver.interpolation.models

import solver.core.Message

case class InterpolationResult(
  methodName: String,
  value: Double,                    
  differenceTable: Seq[Seq[Double]], 
  message: Message,
  isExtrapolated: Boolean = false,
  equation: String = ""
)

object InterpolationResult {
  def formatDouble(d: Double): String = {
    if (d.isInfinite || d.isNaN) "0"
    else {
      val s = f"$d%.15f".replace(",", ".")
      val clean = s.replaceAll("0+$", "")
      if (clean.endsWith(".")) clean.substring(0, clean.length - 1)
      else if (clean.isEmpty) "0"
      else clean
    }
  }

  def getLagrangeEquation(points: Seq[Point]): String = {
    val n = points.size
    if (n == 0) return ""

    def formatXMinus(xj: Double): String = {
      if (xj < 0) s"(x + ${formatDouble(math.abs(xj))})"
      else if (xj == 0) "x"
      else s"(x - ${formatDouble(xj)})"
    }

    val terms = for (i <- 0 until n) yield {
      val num = (0 until n).filter(_ != i).map(j => formatXMinus(points(j).x)).mkString("*")
      val den = (0 until n).filter(_ != i).map(j => points(i).x - points(j).x).product
      val coeff = points(i).y / den
      val coeffStr = formatDouble(coeff)
      val coeffSigned = if (coeffStr.startsWith("-")) coeffStr else "+" + coeffStr

      if (coeff == 0) ""
      else if (num.isEmpty) coeffSigned
      else s"$coeffSigned*$num"
    }
    val filteredTerms = terms.filter(_.nonEmpty)
    if (filteredTerms.isEmpty) "y = 0"
    else {
      val eq = filteredTerms.mkString("")
      val cleanEq = if (eq.startsWith("+")) eq.substring(1) else eq
      "y = " + cleanEq
    }
  }

  def getNewtonDividedEquation(points: Seq[Point], table: Seq[Seq[Double]]): String = {
    val n = points.size
    if (n == 0) return ""

    def formatXMinus(xj: Double): String = {
      if (xj < 0) s"(x + ${formatDouble(math.abs(xj))})"
      else if (xj == 0) "x"
      else s"(x - ${formatDouble(xj)})"
    }

    val terms = for (j <- 0 until n) yield {
      val coeff = table(0)(j)
      val coeffStr = formatDouble(coeff)
      val coeffSigned = if (coeffStr.startsWith("-")) coeffStr else "+" + coeffStr

      if (coeff == 0) ""
      else if (j == 0) coeffSigned
      else {
        val prod = (0 until j).map(k => formatXMinus(points(k).x)).mkString("*")
        s"$coeffSigned*$prod"
      }
    }
    val filteredTerms = terms.filter(_.nonEmpty)
    if (filteredTerms.isEmpty) "y = 0"
    else {
      val eq = filteredTerms.mkString("")
      val cleanEq = if (eq.startsWith("+")) eq.substring(1) else eq
      "y = " + cleanEq
    }
  }

  def getNewtonFiniteForwardEquation(points: Seq[Point], table: Seq[Seq[Double]]): String = {
    val n = points.size
    if (n < 2) return ""
    val h = points(1).x - points(0).x

    def formatXMinus(xj: Double): String = {
      if (xj < 0) s"(x + ${formatDouble(math.abs(xj))})"
      else if (xj == 0) "x"
      else s"(x - ${formatDouble(xj)})"
    }

    var factorial = 1.0
    val terms = for (i <- 0 until n) yield {
      if (i > 0) factorial *= i
      val coeff = if (i == 0) table(0)(0) else table(0)(i) / (factorial * math.pow(h, i))
      val coeffStr = formatDouble(coeff)
      val coeffSigned = if (coeffStr.startsWith("-")) coeffStr else "+" + coeffStr

      if (coeff == 0) ""
      else if (i == 0) coeffSigned
      else {
        val prod = (0 until i).map(k => formatXMinus(points(k).x)).mkString("*")
        s"$coeffSigned*$prod"
      }
    }
    val filteredTerms = terms.filter(_.nonEmpty)
    if (filteredTerms.isEmpty) "y = 0"
    else {
      val eq = filteredTerms.mkString("")
      val cleanEq = if (eq.startsWith("+")) eq.substring(1) else eq
      "y = " + cleanEq
    }
  }

  def getNewtonFiniteBackwardEquation(points: Seq[Point], table: Seq[Seq[Double]]): String = {
    val n = points.size
    if (n < 2) return ""
    val h = points(1).x - points(0).x

    def formatXMinus(xj: Double): String = {
      if (xj < 0) s"(x + ${formatDouble(math.abs(xj))})"
      else if (xj == 0) "x"
      else s"(x - ${formatDouble(xj)})"
    }

    var factorial = 1.0
    val terms = for (i <- 0 until n) yield {
      if (i > 0) factorial *= i
      val diff = if (i == 0) table(n - 1)(0) else table(n - 1 - i)(i)
      val coeff = diff / (factorial * math.pow(h, i))
      val coeffStr = formatDouble(coeff)
      val coeffSigned = if (coeffStr.startsWith("-")) coeffStr else "+" + coeffStr

      if (coeff == 0) ""
      else if (i == 0) coeffSigned
      else {
        val prod = (0 until i).map(k => formatXMinus(points(n - 1 - k).x)).mkString("*")
        s"$coeffSigned*$prod"
      }
    }
    val filteredTerms = terms.filter(_.nonEmpty)
    if (filteredTerms.isEmpty) "y = 0"
    else {
      val eq = filteredTerms.mkString("")
      val cleanEq = if (eq.startsWith("+")) eq.substring(1) else eq
      "y = " + cleanEq
    }
  }

  def getStirlingEquation(points: Seq[Point], table: Seq[Seq[Double]]): String = {
    val n = points.size
    if (n < 2) return ""
    val h = points(1).x - points(0).x
    val midIdx = (n - 1) / 2
    val x0 = points(midIdx).x

    def formatXMinus(xj: Double): String = {
      if (xj < 0) s"(x + ${formatDouble(math.abs(xj))})"
      else if (xj == 0) "x"
      else s"(x - ${formatDouble(xj)})"
    }

    val terms = scala.collection.mutable.ListBuffer[String]()

    val y0 = table(midIdx)(0)
    val y0Str = formatDouble(y0)
    if (y0 != 0) terms += (if (y0Str.startsWith("-")) y0Str else "+" + y0Str)

    var pProd = 1.0
    var factorial = 1.0

    for (k <- 1 until math.min(midIdx + 1, n - midIdx)) {
      factorial *= (2 * k - 1)
      val avgDiff = (table(midIdx - k + 1)(2 * k - 1) + table(midIdx - k)(2 * k - 1)) / 2.0
      val coeffOdd = avgDiff / (factorial * math.pow(h, 2 * k - 1))
      val coeffOddStr = formatDouble(coeffOdd)
      if (coeffOdd != 0) {
        val prodOdd = (Seq(x0) ++ (1 until k).flatMap(j => Seq(points(midIdx + j).x, points(midIdx - j).x)))
          .map(formatXMinus).mkString("*")
        terms += (if (coeffOddStr.startsWith("-")) coeffOddStr else "+" + coeffOddStr) + "*" + prodOdd
      }

      factorial *= (2 * k)
      val diffEven = table(midIdx - k)(2 * k)
      val coeffEven = diffEven / (factorial * math.pow(h, 2 * k))
      val coeffEvenStr = formatDouble(coeffEven)
      if (coeffEven != 0) {
        val prodEven = (Seq(x0, x0) ++ (1 until k).flatMap(j => Seq(points(midIdx + j).x, points(midIdx - j).x)))
          .map(formatXMinus).mkString("*")
        terms += (if (coeffEvenStr.startsWith("-")) coeffEvenStr else "+" + coeffEvenStr) + "*" + prodEven
      }
    }

    if (terms.isEmpty) "y = 0"
    else {
      val eq = terms.mkString("")
      val cleanEq = if (eq.startsWith("+")) eq.substring(1) else eq
      "y = " + cleanEq
    }
  }

  def getBesselEquation(points: Seq[Point], table: Seq[Seq[Double]]): String = {
    val n = points.size
    if (n < 2) return ""
    val h = points(1).x - points(0).x
    val i0 = (n - 2) / 2
    val x_i0 = points(i0).x
    val x_i0_plus_1 = points(i0 + 1).x
    val x_mid = 0.5 * (x_i0 + x_i0_plus_1)

    def formatXMinus(xj: Double): String = {
      if (xj < 0) s"(x + ${formatDouble(math.abs(xj))})"
      else if (xj == 0) "x"
      else s"(x - ${formatDouble(xj)})"
    }

    val terms = scala.collection.mutable.ListBuffer[String]()

    val constCoeff = (table(i0)(0) + table(i0 + 1)(0)) / 2.0
    val constCoeffStr = formatDouble(constCoeff)
    if (constCoeff != 0) terms += (if (constCoeffStr.startsWith("-")) constCoeffStr else "+" + constCoeffStr)

    val firstCoeff = table(i0)(1) / h
    val firstCoeffStr = formatDouble(firstCoeff)
    if (firstCoeff != 0) {
      terms += (if (firstCoeffStr.startsWith("-")) firstCoeffStr else "+" + firstCoeffStr) + "*" + formatXMinus(x_mid)
    }

    var factorial = 1.0
    for (k <- 1 until n / 2) {
      val evenOrder = 2 * k
      if (i0 - k >= 0 && i0 - k + 1 < n - evenOrder) {
        factorial *= evenOrder
        val avgEvenDiff = (table(i0 - k)(evenOrder) + table(i0 - k + 1)(evenOrder)) / 2.0
        val coeffEven = avgEvenDiff / (factorial * math.pow(h, evenOrder))
        val coeffEvenStr = formatDouble(coeffEven)
        if (coeffEven != 0) {
          val prodEven = (0 until k).flatMap(j => Seq(points(i0 - j).x, points(i0 + 1 + j).x)).map(formatXMinus).mkString("*")
          terms += (if (coeffEvenStr.startsWith("-")) coeffEvenStr else "+" + coeffEvenStr) + "*" + prodEven
        }

        val oddOrder = 2 * k + 1
        if (i0 - k >= 0 && i0 - k < n - oddOrder) {
          factorial *= oddOrder
          val diffOdd = table(i0 - k)(oddOrder)
          val coeffOdd = diffOdd / (factorial * math.pow(h, oddOrder))
          val coeffOddStr = formatDouble(coeffOdd)
          if (coeffOdd != 0) {
            val prodOdd = (Seq(x_mid) ++ (0 until k).flatMap(j => Seq(points(i0 - j).x, points(i0 + 1 + j).x)))
              .map(formatXMinus).mkString("*")
            terms += (if (coeffOddStr.startsWith("-")) coeffOddStr else "+" + coeffOddStr) + "*" + prodOdd
          }
        }
      }
    }

    if (terms.isEmpty) "y = 0"
    else {
      val eq = terms.mkString("")
      val cleanEq = if (eq.startsWith("+")) eq.substring(1) else eq
      "y = " + cleanEq
    }
  }

  def getGaussFirstEquation(points: Seq[Point], table: Seq[Seq[Double]]): String = {
    val n = points.size
    if (n < 2) return ""
    val h = points(1).x - points(0).x
    val midIdx = (n - 1) / 2
    val x0 = points(midIdx).x

    def formatXMinus(xj: Double): String = {
      if (xj < 0) s"(x + ${formatDouble(math.abs(xj))})"
      else if (xj == 0) "x"
      else s"(x - ${formatDouble(xj)})"
    }

    val terms = scala.collection.mutable.ListBuffer[String]()

    val y0 = table(midIdx)(0)
    val y0Str = formatDouble(y0)
    if (y0 != 0) terms += (if (y0Str.startsWith("-")) y0Str else "+" + y0Str)

    var factorial = 1.0
    for (k <- 1 until math.min(midIdx + 1, n - midIdx)) {
      factorial *= (2 * k - 1)
      val diffOdd = table(midIdx - k + 1)(2 * k - 1)
      val coeffOdd = diffOdd / (factorial * math.pow(h, 2 * k - 1))
      val coeffOddStr = formatDouble(coeffOdd)
      if (coeffOdd != 0) {
        val prodOdd = (Seq(x0) ++ (1 until k).flatMap(j => Seq(points(midIdx + j).x, points(midIdx - j).x)))
          .map(formatXMinus).mkString("*")
        terms += (if (coeffOddStr.startsWith("-")) coeffOddStr else "+" + coeffOddStr) + "*" + prodOdd
      }

      factorial *= (2 * k)
      val diffEven = table(midIdx - k)(2 * k)
      val coeffEven = diffEven / (factorial * math.pow(h, 2 * k))
      val coeffEvenStr = formatDouble(coeffEven)
      if (coeffEven != 0) {
        val prodEven = (Seq(x0, points(midIdx + k).x) ++ (1 until k).flatMap(j => Seq(points(midIdx + j).x, points(midIdx - j).x)))
          .map(formatXMinus).mkString("*")
        terms += (if (coeffEvenStr.startsWith("-")) coeffEvenStr else "+" + coeffEvenStr) + "*" + prodEven
      }
    }

    if (terms.isEmpty) "y = 0"
    else {
      val eq = terms.mkString("")
      val cleanEq = if (eq.startsWith("+")) eq.substring(1) else eq
      "y = " + cleanEq
    }
  }

  def getGaussSecondEquation(points: Seq[Point], table: Seq[Seq[Double]]): String = {
    val n = points.size
    if (n < 2) return ""
    val h = points(1).x - points(0).x
    val midIdx = (n - 1) / 2
    val x0 = points(midIdx).x

    def formatXMinus(xj: Double): String = {
      if (xj < 0) s"(x + ${formatDouble(math.abs(xj))})"
      else if (xj == 0) "x"
      else s"(x - ${formatDouble(xj)})"
    }

    val terms = scala.collection.mutable.ListBuffer[String]()

    val y0 = table(midIdx)(0)
    val y0Str = formatDouble(y0)
    if (y0 != 0) terms += (if (y0Str.startsWith("-")) y0Str else "+" + y0Str)

    var factorial = 1.0
    for (k <- 1 until math.min(midIdx + 1, n - midIdx)) {
      factorial *= (2 * k - 1)
      val diffOdd = table(midIdx - k)(2 * k - 1)
      val coeffOdd = diffOdd / (factorial * math.pow(h, 2 * k - 1))
      val coeffOddStr = formatDouble(coeffOdd)
      if (coeffOdd != 0) {
        val prodOdd = (Seq(x0) ++ (1 until k).flatMap(j => Seq(points(midIdx + j).x, points(midIdx - j).x)))
          .map(formatXMinus).mkString("*")
        terms += (if (coeffOddStr.startsWith("-")) coeffOddStr else "+" + coeffOddStr) + "*" + prodOdd
      }

      factorial *= (2 * k)
      val diffEven = table(midIdx - k)(2 * k)
      val coeffEven = diffEven / (factorial * math.pow(h, 2 * k))
      val coeffEvenStr = formatDouble(coeffEven)
      if (coeffEven != 0) {
        val prodEven = (Seq(x0, points(midIdx - k).x) ++ (1 until k).flatMap(j => Seq(points(midIdx + j).x, points(midIdx - j).x)))
          .map(formatXMinus).mkString("*")
        terms += (if (coeffEvenStr.startsWith("-")) coeffEvenStr else "+" + coeffEvenStr) + "*" + prodEven
      }
    }

    if (terms.isEmpty) "y = 0"
    else {
      val eq = terms.mkString("")
      val cleanEq = if (eq.startsWith("+")) eq.substring(1) else eq
      "y = " + cleanEq
    }
  }
}

