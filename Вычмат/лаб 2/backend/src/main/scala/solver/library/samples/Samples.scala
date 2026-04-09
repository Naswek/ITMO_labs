package solver.library.samples

object Samples {
  val functions = List(
    "2,74x³ - 1,93x² - 15,28x - 3,72",
    "-1,38x³ - 5,42x² + 2,57x + 10,95",
    "x³ + 2,84x² - 5,606x - 14,766",
    "x³ - 1,89x² - 2x + 1,76",
    "-2,7x³ - 1,48x² + 19,23x + 6,35",
    "2x³ + 3,41x² - 23,74x + 2,95",
    "x³ + 2,28x² - 1,934x - 3,907",
    "3x³ + 1,7x² - 15,42x + 6,89",
    "-1,8x³ - 2,94x² + 10,37x + 5,38",
    "x³ - 3,125x² - 3,5x + 2,458",
    "4,45x³ + 7,81x² - 9,62x - 8,17",
    "x³ - 4,5x² - 9,21x - 0,383",
    "x³ + 4,81x² - 17,37x + 5,38",
    "2,3x³ + 5,75x² - 7,41x - 10,6",
    "-2,4x³ + 1,27x² + 8,63x + 2,31",
    "5,74x³ - 2,95x² - 10,28x - 3,23",
    "-0,38x³ - 3,42x² + 2,51x + 8,75",
    "x³ + 2,64x² - 5,41x - 11,76",
    "2x³ - 1,89x² - 5x + 2,34",
    "-2,8x³ - 3,48x² + 10,23x + 9,35",
    "1,8x³ - 2,47x² - 5,53x + 1,539",
    "x³ - 3,78x² + 1,25x + 3,49",
    "-x³ + 5,67x² - 7,12x + 1,34",
    "x³ - 2,92x² + 1,435x + 0,791",
    "x³ - 2,56x² - 1,325x + 4,395",
    "x³ - 2,18x² - 3,27x + 1,43"
  )

  val systems = List(
    SystemSample("sin(x+1) - y - 1.2 = 0", "2x + cos(y) - 2 = 0"),
    SystemSample("tg(xy + 0.1) - x² = 0", "x² + 2y² - 1 = 0"),
    SystemSample("cos(x-1) + y - 0.5 = 0", "x - cos(y) - 3 = 0"),
    SystemSample("sin(x+y) - 1.2x - 0.2 = 0", "x² + 2y² - 1 = 0"),
    SystemSample("tg(xy + 0.3) - x² = 0", "0.9x² + 2y² - 1 = 0"),
    SystemSample("sin(x) + 2y - 2 = 0", "x + cos(y-1) - 0.7 = 0"),
    SystemSample("2x - sin(y-0.5) - 1 = 0", "y + cos(x) - 1.5 = 0"),
    SystemSample("tg(xy) - x² = 0", "0.8x² + 2y² - 1 = 0"),
    SystemSample("sin(x+y) - 1.5x + 0.1 = 0", "x² + 2y² - 1 = 0"),
    SystemSample("sin(x+0.5) - y - 1 = 0", "cos(y-2) + x = 0"),
    SystemSample("tg(xy + 0.2) - x² = 0", "x² + 2y² - 1 = 0"),
    SystemSample("x + sin(y) + 0.4 = 0", "2y - cos(x+1) = 0"),
    SystemSample("sin(y) + 2x - 2 = 0", "y + cos(x-1) - 0.7 = 0"),
    SystemSample("sin(x+y) - 1.4x = 0", "x² + y² - 1 = 0"),
    SystemSample("sin(x-1) + y - 1.5 = 0", "x - sin(y+1) - 1 = 0"),
    SystemSample("y - cos(x) - 2 = 0", "x + cos(y-1) - 0.8 = 0"),
    SystemSample("tg(xy) - x² = 0", "0.5x² + 2y² - 1 = 0"),
    SystemSample("sin(y+2) - x - 1.5 = 0", "y + cos(x-2) - 0.5 = 0"),
    SystemSample("sin(y-1) + x - 1.3 = 0", "y - sin(x+1) - 0.8 = 0"),
    SystemSample("sin(x+y) - 1.1x - 0.1 = 0", "x² + y² - 1 = 0"),
    SystemSample("cos(y) + x - 1.5 = 0", "2y - sin(x-0.5) - 1 = 0"),
    SystemSample("tg(xy + 0.3) - x² = 0", "0.5x² + 2y² - 1 = 0"),
    SystemSample("sin(y+0.5) - x - 1 = 0", "y + cos(x-2) = 0"),
    SystemSample("sin(x-y) - xy + 1 = 0", "0.3x² + y² - 2 = 0"),
    SystemSample("cos(x+0.5) + y - 1 = 0", "sin(y) - 2x - 2 = 0"),
    SystemSample("cos(x+0.5) + y - 0.7 = 0", "sin(y) - 0.5x - 2 = 0")
  )

  val methodsFunctions = List(
    "Chord method",
    "Secant method",
    "Simple iteration method"
  )

  val methodsSystems = List(
    "System Newton method"
  )
}