package aoc2023.day18

import org.junit.jupiter.api.Timeout
import kotlin.test.Test
import kotlin.test.expect

class Day18Test {

    val testInput: Input? = null

    val testInputStr = """
        R 6 (#70c710)
        D 5 (#0dc571)
        L 2 (#5713f0)
        D 2 (#d2c081)
        R 2 (#59c680)
        D 2 (#411b91)
        L 5 (#8ceee2)
        U 2 (#caa173)
        L 1 (#1b58a2)
        U 2 (#caa171)
        R 2 (#7807d2)
        U 3 (#a77fa3)
        L 2 (#015232)
        U 2 (#7a21e3)
    """.trimIndent()

    @Test
    @Timeout(5)
    fun part1test() {
        expect(62L) { part1(parse(testInputStr)) }
    }

    @Test
    @Timeout(5)
    fun part2test() {
        expect(952408144115L) { part2(parse(testInputStr)) }
    }

}
