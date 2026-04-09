package solver.library.systems  

case class SystemPack(
  f: Array[Double] => Array[Double],
  jacobian: Array[Double] => Array[Array[Double]]
)