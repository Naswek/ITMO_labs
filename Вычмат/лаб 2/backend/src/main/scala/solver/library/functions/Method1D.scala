package solver.library.functions 

trait Method1D {
  def solve(func: FunctionPack, a: Double, b: Double, eps: Double): FunctionResult
}