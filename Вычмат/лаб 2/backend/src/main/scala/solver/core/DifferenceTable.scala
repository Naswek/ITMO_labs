package solver.interpolation.core

import solver.interpolation.models.Point

object DifferenceTable {
  
  def finite(y: Seq[Double]): Seq[Seq[Double]] = {
    val n = y.size
    val table = Array.ofDim[Double](n, n)
    for (i <- 0 until n) table(i)(0) = y(i)
    
    for (j <- 1 until n) {
      for (i <- 0 until (n - j)) {
        table(i)(j) = table(i + 1)(j - 1) - table(i)(j - 1)
      }
    }
    table.map(_.toSeq).toSeq
  }

  def divided(points: Seq[Point]): Seq[Seq[Double]] = {
    val n = points.size
    val table = Array.ofDim[Double](n, n)
    for (i <- 0 until n) table(i)(0) = points(i).y
    
    for (j <- 1 until n) {
      for (i <- 0 until (n - j)) {
        table(i)(j) = (table(i + 1)(j - 1) - table(i)(j - 1)) / (points(i + j).x - points(i).x)
      }
    }
    table.map(_.toSeq).toSeq
  }
}