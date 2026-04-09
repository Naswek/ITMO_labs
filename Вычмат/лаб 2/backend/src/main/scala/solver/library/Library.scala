package solver.library

import solver.library.functions.{FunctionPack, Functions, Method1D}
import solver.library.systems.{SystemPack, Systems, SystemMethod}
import solver.methods.{ChordMethod, SecantMethod, SimpleIterationMethod, SystemNewton}

object Library {
    val functionPack: Map[Int, FunctionPack] = Map(
        0 -> Functions.p1,
        1 -> Functions.p2,
        2 -> Functions.p3,
        3 -> Functions.p4,
        4 -> Functions.p5,
        5 -> Functions.p6,
        6 -> Functions.p7,
        7 -> Functions.p8,
        8 -> Functions.p9,
        9 -> Functions.p10,
        10 -> Functions.p11,
        11 -> Functions.p12,
        12 -> Functions.p13,
        13 -> Functions.p14,
        14 -> Functions.p15,
        15 -> Functions.p16,
        16 -> Functions.p17,
        17 -> Functions.p18,
        18 -> Functions.p19,
        19 -> Functions.p20,
        20 -> Functions.p21,
        21 -> Functions.p22,
        22 -> Functions.p23,
        23 -> Functions.p24,
        24 -> Functions.p25,
        25 -> Functions.p26
    )

    val systemPack: Map[Int, SystemPack] = Map(
        0 -> Systems.sys1,
        1 -> Systems.sys2,
        2 -> Systems.sys3,
        3 -> Systems.sys4,
        4 -> Systems.sys5,
        5 -> Systems.sys6,
        6 -> Systems.sys7,
        7 -> Systems.sys8,
        8 -> Systems.sys9,
        9 -> Systems.sys10,
        10 -> Systems.sys11,
        11 -> Systems.sys12,
        12 -> Systems.sys13,
        13 -> Systems.sys14,
        14 -> Systems.sys15,
        15 -> Systems.sys16,
        16 -> Systems.sys17,
        17 -> Systems.sys18,
        18 -> Systems.sys19,
        19 -> Systems.sys20,
        20 -> Systems.sys21,
        21 -> Systems.sys22,
        22 -> Systems.sys23,
        23 -> Systems.sys24,
        24 -> Systems.sys25,
        25 -> Systems.sys26
    )

    val functionMethods: Map[Int, Method1D] = Map(
        0 -> new ChordMethod,
        1 -> new SecantMethod,
        2 -> new SimpleIterationMethod
    )

    val systemMethods: Map[Int, SystemMethod] = Map(
        0 -> new SystemNewton
    )
}