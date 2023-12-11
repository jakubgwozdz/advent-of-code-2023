package aoc2023.day11

import org.junit.jupiter.api.Timeout
import kotlin.test.Ignore
import kotlin.test.Test
import kotlin.test.expect

class Day11Test {

    val testInput: Input? = null

    val testInputStr = """
        ...#......
        .......#..
        #.........
        ..........
        ......#...
        .#........
        .........#
        ..........
        .......#..
        #...#.....
    """.trimIndent()


    @Test
    @Timeout(1)
    @Ignore
    fun parseTest() {
        expect(testInput) { parse(testInputStr) }
    }

    @Test
    @Timeout(5)
    fun part1test() {
        expect(374L) { part1(parse(testInputStr)) }
    }

    @Test
    @Timeout(5)
    fun part2test10() {
        expect(1030L) { calc(parse(testInputStr), 10) }
    }

}
