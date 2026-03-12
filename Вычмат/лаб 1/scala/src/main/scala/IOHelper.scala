import scala.io.StdIn.readLine
import java.io.File
import Colors._
import scala.util.Random

object IOHelper {

  def readDouble(min: Double = Double.MinValue, max: Double = Double.MaxValue): Double = {
    val input = readLine()
    try {
      val cleanInput = input.replace(',', '.')
      val value = cleanInput.toDouble
      if (value >= min && value <= max) value
      else {
        printError(s"Введите число от $min до $max")
        readDouble(min, max)
      }
    } catch {
      case _: NumberFormatException =>
        printError("Введите корректное число")
        readDouble(min, max)
    }
  }

  def readInt(min: Int = 2, max: Int = 20): Int = {
    val input = readLine()
    try {
      val value = input.toInt
      if (value >= min && value <= max) value
      else {
        printError(s"Введите целое число от $min до $max")
        readInt(min, max)
      }
    } catch {
      case _: NumberFormatException =>
        printError("Введите корректное целое число")
        readInt(min, max)
    }
  }

  def readNumbers(expectedLength: Option[Int] = None): Array[Double] = {
    while (true) {
      val input = readLine()
      try {
        val row = input.split("\\s+").map(_.toDouble).toArray
        if (expectedLength.isEmpty || row.length == expectedLength.get) return row
        else printError(s"Нужно ввести ровно ${expectedLength.get} чисел")
      } catch {
        case _: NumberFormatException => printError("Введите только числа через пробел")
      }
    }
    Array.empty[Double]
  }

  def readMatrixFromKeyboard(dimension: Int): Array[Array[Double]] = {
    val matrix = Array.ofDim[Double](dimension, dimension + 1)
    printInfo("Введите строки матрицы и свободные коэффициенты, разделяя их пробелом:")
    for (i <- 0 until dimension) {
      matrix(i) = readNumbers(Some(dimension + 1)).toArray
    }
    matrix
  }

import scala.io.StdIn.readLine
import java.io.File

def readMatrixFromFile(): Option[(Array[Array[Double]], String)] = {
  printInfo("Введите название файла: ")
  val fileName = readLine()
  val file = new File(fileName)
  if (!file.exists() || !file.isFile) {
    printError("Файл не найден")
    return None
  }

  try {
    val lines = scala.io.Source.fromFile(file).getLines().toSeq
    if (lines.isEmpty) {
      printError("Файл пустой")
      return None
    }

    val dimension = lines.head.toInt
    val matrix = Array.ofDim[Double](dimension, dimension + 1)

    for (i <- 0 until dimension) {
      val row = lines(i + 1).split("\\s+").map(_.replace(",", ".").toDouble)
      if (row.length != dimension + 1) {
        printError("Неверное количество элементов в строке")
        return None
      }
      matrix(i) = row
    }

    printSuccess("Матрица успешно загружена:")
    matrix.foreach(row => printInfo(row.mkString(" ")))
    Some((matrix, fileName))

  } catch {
    case _: NumberFormatException =>
      printError("Ошибка в формате чисел")
      None
    case _: IndexOutOfBoundsException =>
      printError("В файле меньше строк, чем размерность")
      None
  }
}

  def readMatrixFromGeneration(dimension: Int): Array[Array[Double]] = {
    val matrix = Array.ofDim[Double](dimension, dimension + 1)
    val rnd = new Random()

    for (i <- 0 until dimension) {
      for (j <- 0 to dimension) {
        matrix(i)(j) = rnd.nextInt(101)
      }
    }

    printSuccess("Полученная матрица:")
    matrix.foreach(row => println(row.mkString(" ")))

    matrix
  }
}