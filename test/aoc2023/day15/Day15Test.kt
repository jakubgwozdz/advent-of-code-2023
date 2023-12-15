package aoc2023.day15

import org.junit.jupiter.api.Timeout
import kotlin.test.Test
import kotlin.test.expect

class Day15Test {

    val testInputStr = """
        rn=1,cm-,qp=3,cm=2,qp-,pc=4,ot=9,ab=5,pc-,pc=6,ot=7
    """.trimIndent()

    @Test
    @Timeout(5)
    fun part1test() {
        expect(1320) { part1(parse(testInputStr)) }
    }

    @Test
    @Timeout(5)
    fun part2test() {
        expect(145) { part2(parse(testInputStr)) }
    }

}
