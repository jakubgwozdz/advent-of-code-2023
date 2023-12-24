package aoc2023.day24

import kotlin.test.Test
import kotlin.test.expect

class Day24Test {

    val testInputStr = """
        19, 13, 30 @ -2,  1, -2
        18, 19, 22 @ -1, -1, -2
        20, 25, 34 @ -2, -2, -4
        12, 31, 28 @ -1, -2, -1
        20, 19, 15 @  1, -5, -3
    """.trimIndent()

    @Test
    fun part1test() {
        expect(2) { part1(parse(testInputStr), 7..27L) }
    }

    @Test
    fun part2test() {
        expect(47) { part2(parse(testInputStr)) }
    }

}
