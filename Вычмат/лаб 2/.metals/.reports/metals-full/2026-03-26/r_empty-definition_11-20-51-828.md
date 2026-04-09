error id: file://<WORKSPACE>/backend/src/main/scala/com/example/solver/library/Library.scala:sys3.
file://<WORKSPACE>/backend/src/main/scala/com/example/solver/library/Library.scala
empty definition using pc, found symbol in pc: sys3.
empty definition using semanticdb
empty definition using fallback
non-local guesses:
	 -Systems.sys3.
	 -Systems.sys3#
	 -Systems.sys3().
	 -scala/Predef.Systems.sys3.
	 -scala/Predef.Systems.sys3#
	 -scala/Predef.Systems.sys3().
offset: 367
uri: file://<WORKSPACE>/backend/src/main/scala/com/example/solver/library/Library.scala
text:
```scala
import java.nio.file.FileSystems
object Lib {
    val functionPack: Map[Int, FunctionPack] = Map(
        1 -> Functions.f1,
        2 -> Functions.f2,
        3 -> Functions.f3,
        4 -> Functions.f4,
        5 -> Functions.f5    
    )

    val systemPack: Map[Int, SystemPack] = Map(
        1 -> Systems.sys1,
        2 -> Systems.sys2,
        3 -> Systems.s@@ys3         
    )

    val functionMethods: Map[Int, Solver1D] = Map(
        1 -> new ChordMethod,
        2 -> new SecantMethod,
        3 -> new SimpleIterationMethod
    )

    val systemMethods: Map[Int, SystemSolver] = Map(
        1 -> new SystemNewton
    )
}
```


#### Short summary: 

empty definition using pc, found symbol in pc: sys3.