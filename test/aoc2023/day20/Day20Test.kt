package aoc2023.day20

import org.junit.jupiter.api.Timeout
import kotlin.test.Ignore
import kotlin.test.Test
import kotlin.test.expect

class Day20Test {

    val testInputStr = """
        broadcaster -> a, b, c
        %a -> b
        %b -> c
        %c -> inv
        &inv -> a
    """.trimIndent()

    val testInputStr2 = """
        broadcaster -> a
        %a -> inv, con
        &inv -> b
        %b -> con
        &con -> output
    """.trimIndent()

    @Test
    @Timeout(5)
    fun part1test1() {
        expect(32000000) { part1(parse(testInputStr)) }
    }

    @Test
    @Timeout(5)
    fun part1test2() {
        expect(11687500) { part1(parse(testInputStr2)) }
    }

    @Test
    @Timeout(5)
    @Ignore
    fun part2test() {
        expect(3) { part2(parse(testInputStr)) }
    }

}
