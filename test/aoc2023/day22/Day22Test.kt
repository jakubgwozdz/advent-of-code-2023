package aoc2023.day22

import org.junit.jupiter.api.Timeout
import kotlin.test.Ignore
import kotlin.test.Test
import kotlin.test.expect

class Day22Test {

    val testInput: Input? = null

    val testInputStr = """
        1,0,1~1,2,1
        0,0,2~2,0,2
        0,2,3~2,2,3
        0,0,4~0,2,4
        2,0,5~2,2,5
        0,1,6~2,1,6
        1,1,8~1,1,9
    """.trimIndent()


    @Test
    @Timeout(1)
    @Ignore
    fun parseTest() {
        expect(testInput) { parse(testInputStr) }
    }

    @Test
    @Timeout(5)
    fun part1test() {
        expect(5) { part1(parse(testInputStr)) }
    }

    @Test
    @Timeout(5)
    @Ignore
    fun part2test() {
        expect(3) { part2(parse(testInputStr)) }
    }

}
