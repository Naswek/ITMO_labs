import Colors._
import IOHelper._

object Main extends App {

  var running = true

  while (running) {
    printInfo(
      """Выберите способ ввода матрицы:
        |1. Клавиатура
        |2. Файл
        |3. Генерация
        |4. Выход""".stripMargin)

    val choice = readInt(1, 4)

    val matrixOpt: Option[(Array[Array[Double]], Option[String])] = choice match {
      case 1 =>
        printInfo("Введите размерность матрицы (количество строк) до 20 включительно:") 
        val dim = readInt(2, 20)
        Some((readMatrixFromKeyboard(dim), None))

      case 2 => 
      readMatrixFromFile().map { case (matrix, fileName) =>
        (matrix, Some(fileName))
      }
      case 3 =>
        printInfo("Введите размерность матрицы (количество строк) до 20 включительно:") 
        val dim = readInt(2, 20)
        Some((readMatrixFromGeneration(dim), None))

      case 4 =>
        running = false
        None
    }

    matrixOpt.foreach { case (matrix, fileNameOpt) =>

      def processMatrix(): Unit = {
        val solver = new MatrixSolver(matrix, fileNameOpt)
        var isDiagonallyDominant = solver.isDiagonallyDominant(matrix)

        if (!isDiagonallyDominant) {
          printWarning("Матрица не является диагонально доминирующей. Попытка перестановки строк...")
          val isSuccess = solver.makeDiagonallyDominant(matrix)
          if (isSuccess) {
            printSuccess("Матрица стала диагонально доминирующей после перестановки строк.")
            isDiagonallyDominant = true
          } else {
            printError("Не удалось сделать матрицу диагонально доминирующей. Пропускаем эту матрицу.")
            return 
          }
        }

        printInfo("Преобразуем матрицу к форме, удобной для метода Якоби...")
        val (c, d) = solver.toJacobiForm(matrix)
        printInfo("Проверяем условие сходимости...")

        if (isDiagonallyDominant && solver.matrixNorm(c)) {
          printSuccess("Начинаем итерации метода Якоби...")
          solver.runJacobiIterationsWithFormula(c, d, fileNameOpt)
        }
      }

      processMatrix()
    }
  }
}