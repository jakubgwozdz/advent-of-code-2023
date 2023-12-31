package aoc2023.day7

import org.junit.jupiter.api.Timeout
import kotlin.test.Ignore
import kotlin.test.Test
import kotlin.test.expect

class Day7Test {

    val testInputStr = """
        32T3K 765
        T55J5 684
        KK677 28
        KTJJT 220
        QQQJA 483
    """.trimIndent()


    @Test
    @Timeout(5)
    fun part1test() {
        expect(6440) { part1(parse(testInputStr)) }
    }

    @Test
    @Timeout(5)
    fun part2test() {
        expect(5905) { part2(parse(testInputStr)) }
    }

}
