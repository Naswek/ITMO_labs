package solver.methods
import solver.library.functions.{FunctionPack, Method1D, FunctionResult}

class SecantMethod extends Method1D {
  override def solve(functionPack: FunctionPack, a: Double, b: Double, epsilon: Double): FunctionResult = {
    val fa = functionPack.f(a)
    val fb = functionPack.f(b)

    var xPrev = a
    var xCurr = b
    var fPrev = functionPack.f(xPrev)
    var fCurr = functionPack.f(xCurr)
    var iterations = 0
    var xNext = 0.0


    while ((math.abs(fCurr)) > epsilon && iterations < 1000) {

        if (math.abs(fCurr - fPrev) < 1e-12) {
            return new FunctionResult(xCurr, fCurr, iterations, Some("Предупреждение: знаменатель близок к нулю"))
        }


        xNext = xCurr - (xCurr - xPrev) / (fCurr - fPrev) * fCurr
        
        xPrev = xCurr
        fPrev = fCurr
        xCurr = xNext
        fCurr = functionPack.f(xCurr)

        iterations += 1
    }
    new FunctionResult(xCurr, fCurr, iterations, if (iterations >= 1000) Some("Превышено максимальное количество итераций") else None)
  }
}