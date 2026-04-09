error id: file://<WORKSPACE>/backend/src/main/scala/com/example/solver/library/Library.scala:
file://<WORKSPACE>/backend/src/main/scala/com/example/solver/library/Library.scala
empty definition using pc, found symbol in pc: 
empty definition using semanticdb
empty definition using fallback
non-local guesses:
	 -Systems.
	 -scala/Predef.Systems.
offset: 498
uri: file://<WORKSPACE>/backend/src/main/scala/com/example/solver/library/Library.scala
text:
```scala
import java.nio.file.FileSystems
object FunctionLib {
    val functionPacks: Map[Int, FunctionPack] = Map(
        1 -> FunctionPack(Functions.f1, Functions.d1f1, Functions.d2f1),
        2 -> FunctionPack(Functions.f2, Functions.d1f2, Functions.d2f2),
        3 -> FunctionPack(Functions.f3, Functions.d1f3, Functions.d2f3),
        4 -> FunctionPack(Functions.f4, Functions.d1f4, Functions.d2f4),
        5 -> FunctionPack(Functions.f5, Functions.d1f5, Functions.d2f5),
        6 -> SystemPack(Sy@@stems.sys1, Systems.d1sys1, Systems.d2sys1),
        7 -> SystemPack(Systems.sys2, Systems.d1sys2, Systems.d2sys2),
        8 -> SystemPack(Systems.sys3, Systems.d1sys3, Systems.d2sys3)         
    )
}
```


#### Short summary: 

empty definition using pc, found symbol in pc: 