class MatrixSolver(matrix: Array[Array[Double]], fileName: Option[String] = None) {

    import Colors._
    import IOHelper._

    def checkConvergence(matrix: Array[Array[Double]]): Boolean = {
        val n = matrix.length
        for (i <- 0 until n) {
            var diag = math.abs(matrix(i)(i))
            val others = (i until n)
                .filter(_ != i)
                .map(j => math.abs(matrix(i)(j)))
                .sum
            if (diag <= others) return false;
        }   
        true
    }

    def isDiagonallyDominant(matrix: Array[Array[Double]]): Boolean = {
    matrix.indices.forall { i =>
        val diag = math.abs(matrix(i)(i))
        val rowSum = matrix(i).zipWithIndex.filter(_._2 != i).map(_._1.abs).sum
        diag >= rowSum
    }
    }

    def makeDiagonallyDominant(matrix: Array[Array[Double]]): Boolean = {
    val n = matrix.length

    for (i <- 0 until n) {
        val maxRow = (i until n).maxBy { r =>
        math.abs(matrix(r)(i)) - matrix(r).zipWithIndex.filter(_._2 != i).map(_._1.abs).sum
        }

        val temp = matrix(i)
        matrix(i) = matrix(maxRow)
        matrix(maxRow) = temp
    }

    if (isDiagonallyDominant(matrix)) {
        true
    } else {
        false
    }
    }

    def toJacobiForm(matrix: Array[Array[Double]]): (Array[Array[Double]], Array[Double]) = {
        val n = matrix.length
        val C = Array.fill(n, n)(0.0)
        val d = Array.fill(n)(0.0)

        
            for (i <- 0 until n) {
                val a_ii = matrix(i)(i)


                for (j <- 0 until n) {
                if (i != j) {
                    C(i)(j) = -matrix(i)(j) / a_ii
                }
                }
                
                d(i) = matrix(i)(n) / a_ii 
            }
        
        

        printSuccess("Получившаяся матрица C:")
        C.foreach(row => printInfo(row.mkString(" ")))
        printSuccess(s"Вектор d: ")
        printInfo(d.mkString(" "))
        (C, d)
    }

    def matrixNorm(C: Array[Array[Double]]): Boolean = {
        val norm = C.map(row => row.map(math.abs).sum).max
        if (norm < 1) {
            printSuccess(s"Условие сходимости выполняется, норма: $norm") 
            true
        } else {
            printError(s"Условие сходимости не выполняется, норма: $norm")
            false
        }
    }

    def getApproximationVector(dimension: Int, fileName: Option[String]): (Double, Array[Double]) = {
        if (fileName.isDefined) {
            printInfo("Хотите ли вы ввести эпсилон и начальное приближение? Иначе будут использованы значения из файла (y/n)")
            if (readLine().toLowerCase() == "y") {
            return (requestEpsilon, requestInitialApproximation(dimension))
            } else {
                printInfo("Используем эпсилон и начальное приближение из файла")
                val lines = scala.io.Source.fromFile(fileName.get).getLines().toSeq

                val epsilon = lines.drop(dimension + 1).headOption
                .flatMap { line =>
                    try Some(line.replace(",", ".").toDouble)
                    catch {
                    case _: NumberFormatException => None
                    }
                }
                .getOrElse {
                    printError("Эпсилон не найден или некорректный, используем 0.001")
                    0.001
                }

                val rowX0 = lines.drop(dimension + 2).headOption
                .flatMap { line =>
                    try {
                    Some(line.split("\\s+").map(_.replace(",", ".").toDouble))
                    } catch {
                    case _: NumberFormatException => None
                    }
                }
                .getOrElse {
                    printError("Начальное приближение не найдено или некорректно, используем (0.0, ...)")
                    Array.fill(dimension)(0.0)
                }
                return (epsilon, rowX0)
            }
        } else {
            return (requestEpsilon, requestInitialApproximation(dimension))
        }
    }

    def requestEpsilon(): Double = {
        printInfo("Напишите эпсилон: ")
        readDouble()
    }

    def requestInitialApproximation(dimension: Int): Array[Double] = {
        printInfo(s"Напишите начальное приближение c размерностью $dimension, формат ввода: x1 x2 x3 ... xn")
        readNumbers(Some(dimension))
    }

    def runJacobiIterationsWithFormula(C: Array[Array[Double]], d: Array[Double], fileName: Option[String]): Unit = {
        val n = C.length

        def idx(num: Int): String = {
            val sub = "₀₁₂₃₄₅₆₇₈₉"
            num.toString.map(c => sub(c.asDigit)).mkString
        }

        for (i <- 0 until n) {
            val parts = (0 until n).flatMap { j =>
            if (C(i)(j) != 0) {
                val sign = if (C(i)(j) >= 0) "+" else "-"
                Some(s" $sign ${math.abs(C(i)(j))}·x${idx(j+1)}^k")
            } else None
            }.mkString

            val signC = if (d(i) >= 0) "+" else "-"
            val formula = s"x${idx(i+1)}^(k+1) =$parts $signC ${math.abs(d(i))}"
            printSuccess(formula)
        }

        val (epsilon, x0) = getApproximationVector(n, fileName)
        var row0 = x0
        var newRow = Array.fill(n)(0.0)
        var diff = epsilon + 1
        var count = 0

        while (diff > epsilon) {
            newRow = iteration(C, row0, d)
            count += 1
            diff = (row0 zip newRow).map { case (a, b) => math.abs(a - b) }.max

            
            val diffVector = (row0 zip newRow).map { case (a, b) => math.abs(a - b) }
            printInfo(s"Погрешности на итерации $count: ${diffVector.mkString(", ")}")
            
            row0 = newRow
        }

        printSuccess(
            s"""Введенный эпсилон: $epsilon
            |Полученная строка: ${newRow.mkString(", ")}
            |Количество итераций: $count
            |Полученный max: ${newRow.max}""".stripMargin)
    }

    def iteration(C: Array[Array[Double]], x0: Array[Double], d: Array[Double]): Array[Double] = {
        Array.tabulate(C.length){ i =>
            (0 until C.length).map(j => C(i)(j) * x0(j)).sum + d(i)
        }
    }
}
