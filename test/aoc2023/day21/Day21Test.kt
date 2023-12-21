package aoc2023.day21

import org.junit.jupiter.api.Timeout
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
        expect(16L) { calc(parse(testInputStr),6) }
    }

    @Test
    @Timeout(5)
    fun part2test1() {
        expect(50L) { calc(parse(testInputStr),10) }
        expect(1594L) { calc(parse(testInputStr),50) }
        expect(16733044L) { calc(parse(testInputStr),5000) }
    }

}
