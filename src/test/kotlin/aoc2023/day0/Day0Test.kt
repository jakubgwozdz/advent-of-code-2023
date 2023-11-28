package aoc2023.day0

import org.junit.jupiter.api.Timeout
import kotlin.test.Ignore
import kotlin.test.Test
import kotlin.test.expect

class Day0Test {

    val testInput = Input(42)

    @Test
    @Timeout(1)
    @Ignore
    fun parseTest() {
        expect(testInput) {
            parse(
                """
                Damage: 42
            """.trimIndent()
            )
        }
    }

    @Test
    @Timeout(5)
    @Ignore
    fun part1test() {
        expect(226) { part1(testInput) }
    }

    @Test
    @Timeout(5)
    @Ignore
    fun part2test() {
        expect(3) { part2(testInput) }
    }

}
