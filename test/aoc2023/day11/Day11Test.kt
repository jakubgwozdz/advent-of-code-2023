package aoc2023.day11

import org.junit.jupiter.api.Timeout
import kotlin.test.Test
import kotlin.test.expect

class Day11Test {

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
    @Timeout(5)
    fun part1test() {
        expect(374L) { part1(parse(testInputStr)) }
    }

    @Test
    @Timeout(5)
    fun part2test10() {
        expect(1030L) { parse(testInputStr).calc2D(10) }
    }

}
