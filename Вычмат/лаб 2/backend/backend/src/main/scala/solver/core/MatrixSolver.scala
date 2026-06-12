package solver.core

object MatrixSolver {

  def solve(matrix: Array[Array[Double]]): Array[Double] = {
    val n = matrix.length

    for (i <- 0 until n) {
      var max = i
      for (k <- i + 1 until n) {
        if (math.abs(matrix(k)(i)) > math.abs(matrix(max)(i))) max = k
      }

      val temp = matrix(i)
      matrix(i) = matrix(max)
      matrix(max) = temp

      if (math.abs(matrix(i)(i)) < 1e-12) {
        throw new ArithmeticException("Матрица вырождена или плохо обусловлена")
      }

      for (k <- i + 1 until n) {
        val factor = matrix(k)(i) / matrix(i)(i)
        for (j <- i until n + 1) {
          matrix(k)(j) -= factor * matrix(i)(j)
        }
      }
    }

    val solution = new Array[Double](n)
    for (i <- n - 1 to 0 by -1) {
      var sum = 0.0
      for (j <- i + 1 until n) {
        sum += matrix(i)(j) * solution(j)
      }
      solution(i) = (matrix(i)(n) - sum) / matrix(i)(i)
    }
    solution
  }
}