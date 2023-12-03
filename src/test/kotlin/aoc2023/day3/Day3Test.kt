package aoc2023.day3

import org.junit.jupiter.api.Timeout
import kotlin.test.Ignore
import kotlin.test.Test
import kotlin.test.expect

class Day3Test {

    val testInput: Input? = null // listOf("42")

    val inputStr = """
                    467..114..
                    ...*......
                    ..35..633.
                    ......#...
                    617*......
                    .....+.58.
                    ..592.....
                    ......755.
                    ...${'$'}.*....
                    .664.598..
                """.trimIndent()

    @Test
    @Timeout(1)
    @Ignore
    fun parseTest() {
        expect(testInput) { parse(inputStr) }
    }

    @Test
    @Timeout(5)
    fun part1test() {
        expect(4361L) { part1(parse(inputStr)) }
    }

    @Test
    @Timeout(5)
    fun part2test() {
        expect(467835L) { part2(parse(inputStr)) }
    }

}
