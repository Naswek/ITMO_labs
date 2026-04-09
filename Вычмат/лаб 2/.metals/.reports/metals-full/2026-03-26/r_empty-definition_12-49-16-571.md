error id: file://<WORKSPACE>/backend/src/main/scala/solver/library/systems/SystemPack.scala:
file://<WORKSPACE>/backend/src/main/scala/solver/library/systems/SystemPack.scala
empty definition using pc, found symbol in pc: 
empty definition using semanticdb
empty definition using fallback
non-local guesses:

offset: 160
uri: file://<WORKSPACE>/backend/src/main/scala/solver/library/systems/SystemPack.scala
text:
```scala
package solver.library.systems  

case class SystemPack(
  f: Array[Double] => Array[Double],
  ja  SystemPack(
    f = x => Array(math.sin(x(0))),
    jacobian@@ = x => numericalJacobian(x => Array(math.sin(x(0))), x)
  )cobi: Array[Double] => Array[Array[Double]]
)
```


#### Short summary: 

empty definition using pc, found symbol in pc: 