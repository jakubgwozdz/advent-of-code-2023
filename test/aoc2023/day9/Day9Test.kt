package aoc2023.day9

import org.junit.jupiter.api.Timeout
import kotlin.test.Test
import kotlin.test.expect

class Day9Test {

    val testInputStr = """
        0 3 6 9 12 15
        1 3 6 10 15 21
        10 13 16 21 30 45
    """.trimIndent()

    @Test
    @Timeout(5)
    fun part1test() {
        expect(114) { part1(parse(testInputStr)) }
    }

    @Test
    @Timeout(5)
    fun part2test() {
        expect(2) { part2(parse(testInputStr)) }
    }

}
