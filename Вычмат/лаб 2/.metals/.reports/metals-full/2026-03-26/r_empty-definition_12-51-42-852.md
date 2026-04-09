error id: file://<WORKSPACE>/backend/src/main/scala/solver/library/systems/Systems.scala:
file://<WORKSPACE>/backend/src/main/scala/solver/library/systems/Systems.scala
empty definition using pc, found symbol in pc: 
empty definition using semanticdb
empty definition using fallback
non-local guesses:
	 -numericalJacobian.
	 -numericalJacobian#
	 -numericalJacobian().
	 -scala/Predef.numericalJacobian.
	 -scala/Predef.numericalJacobian#
	 -scala/Predef.numericalJacobian().
offset: 189
uri: file://<WORKSPACE>/backend/src/main/scala/solver/library/systems/Systems.scala
text:
```scala
package solver.library.systems

package solver.library.systems.Utils

object Systems {

  val sys1: SystemPack =
  SystemPack(
    f = x => Array(math.sin(x(0))),
    jacobian = x => numeri@@calJacobian(x => Array(math.sin(x(0))), x)
  )

  val sys2: SystemPack =
  SystemPack(
    f = x => Array(math.sin(x(0))),
    jacobian = x => numericalJacobian(x => Array(math.sin(x(0))), x)
  )

  val sys3: SystemPack =
  SystemPack(
    f = x => Array(math.sin(x(0))),
    jacobian = x => numericalJacobian(x => Array(math.sin(x(0))), x)
  )
}
```


#### Short summary: 

empty definition using pc, found symbol in pc: 