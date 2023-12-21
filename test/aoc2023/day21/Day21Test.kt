package aoc2023.day21

import org.junit.jupiter.api.Timeout
import kotlin.test.Ignore
import kotlin.test.Test
import kotlin.test.expect

class Day21Test {

    val testInput: Input? = null

    val testInputStr = """
        ...........
        .....###.#.
        .###.##..#.
        ..#.#...#..
        ....#.#....
        .##..S####.
        .##..#...#.
        .......##..
        .##.#.####.
        .##..##.##.
        ...........
    """.trimIndent()

    @Test
    @Timeout(5)
    fun part1test() {
        expect(16) { part1(parse(testInputStr),6) }
    }

    @Test
    @Timeout(5)
    @Ignore
    fun part2test() {
        expect(3) { part2(parse(testInputStr)) }
    }

}
