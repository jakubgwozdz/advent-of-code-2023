package aoc2023.day1

import org.junit.jupiter.api.Timeout
import kotlin.test.Ignore
import kotlin.test.Test
import kotlin.test.expect

class Day1Test {

    val testInput = listOf("1abc2","pqr3stu8vwx","a1b2c3d4e5f","treb7uchet")

    @Test
    @Timeout(1)
    @Ignore
    fun parseTest() {
        expect(testInput) {
            parse(
                """
                    1abc2
                    pqr3stu8vwx
                    a1b2c3d4e5f
                    treb7uchet
                """.trimIndent()
            )
        }
    }

    @Test
    @Timeout(5)
    @Ignore
    fun part1test() {
        expect(142) { part1(testInput) }
    }

    @Test
    @Timeout(5)
    @Ignore
    fun part2test() {
        expect(281) { part2(testInput) }
    }

}
