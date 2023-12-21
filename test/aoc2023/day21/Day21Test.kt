package aoc2023.day21

import org.junit.jupiter.api.Timeout
import kotlin.test.Test
import kotlin.test.expect

@Timeout(1, threadMode = Timeout.ThreadMode.SEPARATE_THREAD)
class Day21Test {

    val testInput: Input? = null

    val testInputStr = """
        ...........
        .....###.#.
        .###.##..#.
        ..#.#...#..
        ....#.#....
        .##..S####.
        .##..#...#.
        .......##..
        .##.#.####.
        .##..##.##.
        ...........
    """.trimIndent()

    @Test
    fun test6() {
        expect(16L) { calc(parse(testInputStr),6) }
    }

    @Test
    fun test10() {
        expect(50L) { calc(parse(testInputStr),10) }
    }

    @Test
    fun test50() {
        expect(1594L) { calc(parse(testInputStr),50) }
    }

    @Test
    fun test5000() {
        expect(16733044L) { calc(parse(testInputStr),5000) }
    }

}
