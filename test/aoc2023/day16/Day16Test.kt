package aoc2023.day16

import org.junit.jupiter.api.Timeout
import kotlin.test.Ignore
import kotlin.test.Test
import kotlin.test.expect

class Day16Test {

    val testInput: Input? = null

    val testInputStr = """
        .|...\....
        |.-.\.....
        .....|-...
        ........|.
        ..........
        .........\
        ..../.\\..
        .-.-/..|..
        .|....-|.\
        ..//.|....
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
        expect(46) { part1(parse(testInputStr)) }
    }

    @Test
    @Timeout(5)
    fun part2test() {
        expect(51) { part2(parse(testInputStr)) }
    }

}
