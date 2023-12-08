package aoc2023.day8

import org.junit.jupiter.api.Timeout
import kotlin.test.Test
import kotlin.test.expect

class Day8Test {

    val testInputStr = """
        RL

        AAA = (BBB, CCC)
        BBB = (DDD, EEE)
        CCC = (ZZZ, GGG)
        DDD = (DDD, DDD)
        EEE = (EEE, EEE)
        GGG = (GGG, GGG)
        ZZZ = (ZZZ, ZZZ)
    """.trimIndent()

    val testInputStr1 = """
        LLR

        AAA = (BBB, BBB)
        BBB = (AAA, ZZZ)
        ZZZ = (ZZZ, ZZZ)
    """.trimIndent()

    val testInputStr2 = """
        LR

        11A = (11B, XXX)
        11B = (XXX, 11Z)
        11Z = (11B, XXX)
        22A = (22B, XXX)
        22B = (22C, 22C)
        22C = (22Z, 22Z)
        22Z = (22B, 22B)
        XXX = (XXX, XXX)
    """.trimIndent()

    @Test
    @Timeout(5)
    fun part1test() {
        expect(2L) { part1(parse(testInputStr)) }
    }

    @Test
    @Timeout(5)
    fun part1test2() {
        expect(6L) { part1(parse(testInputStr1)) }
    }

    @Test
    @Timeout(5)
    fun part2test() {
        expect(6L) { part2(parse(testInputStr2)) }
    }

}
