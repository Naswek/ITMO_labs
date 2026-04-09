
package solver.library.systems  

object Utils {

    def numericalJacobian(
        F: Array[Double] => Array[Double],
        x: Array[Double],
        h: Double = 1e-6
        ): Array[Array[Double]] = {

        val n = x.length
        val f0 = F(x)
        val m = f0.length

        val jacobi = Array.ofDim[Double](m, n)

        for (j <- 0 until n) {
            val xh = x.clone()
            xh(j) += h

            val fh = F(xh)

            for (i <- 0 until m) {
            jacobi(i)(j) = (fh(i) - f0(i)) / h
            }
        }

        jacobi
    }


    def solveLinear(A: Array[Array[Double]], b: Array[Double]): Array[Double] = {
        val n = b.length
        val aug = Array.tabulate(n, n + 1)((i, j) =>
            if (j < n) A(i)(j) else b(i)
        )

        for (i <- 0 until n) {
            val pivot = aug(i)(i)
            for (j <- i until n + 1) aug(i)(j) /= pivot

            for (k <- i + 1 until n) {
            val factor = aug(k)(i)
            for (j <- i until n + 1)
                aug(k)(j) -= factor * aug(i)(j)
            }
        }

        val x = Array.fill(n)(0.0)
        for (i <- (0 until n).reverse) {
            x(i) = aug(i)(n)
            for (j <- i + 1 until n)
            x(i) -= aug(i)(j) * x(j)
        }

        x
    }
}