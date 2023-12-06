package aoc2023.day6

import org.junit.jupiter.api.Timeout
import kotlin.test.Ignore
import kotlin.test.Test
import kotlin.test.expect

class Day6Test {

    val testInputStr = """
        Time:      7  15   30
        Distance:  9  40  200
    """.trimIndent()


    @Test
    @Timeout(5)
    fun part1test() {
        expect(288) { part1(parse(testInputStr)) }
    }

    @Test
    @Timeout(5)
    fun part2test() {
        expect(71503) { part2(parse(testInputStr)) }
    }

}
