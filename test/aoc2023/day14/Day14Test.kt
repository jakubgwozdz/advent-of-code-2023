package aoc2023.day14

import org.junit.jupiter.api.Timeout
import kotlin.test.Test
import kotlin.test.expect

class Day14Test {

    val testInput: Input? = null

    val testInputStr = """
        O....#....
        O.OO#....#
        .....##...
        OO.#O....O
        .O.....O#.
        O.#..O.#.#
        ..O..#O..O
        .......O..
        #....###..
        #OO..#....
    """.trimIndent()

    @Test
    @Timeout(5)
    fun part1test() {
        expect(136) { part1(parse(testInputStr)) }
    }

    @Test
    @Timeout(5)
    fun part2test() {
        expect(64) { part2(parse(testInputStr)) }
    }

}
