package aoc2023.day21

import org.junit.jupiter.api.Test
import org.junit.jupiter.api.Timeout
import java.util.concurrent.TimeUnit
import kotlin.test.expect

@Timeout(1, unit = TimeUnit.SECONDS)
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
    @Timeout(5)
    fun test6() {
        expect(16L) { calc(parse(testInputStr),6) }
    }

    @Test
    @Timeout(5)
    fun test10() {
        expect(50L) { calc(parse(testInputStr),10) }
    }

    @Test
    @Timeout(5)
    fun test50() {
        expect(1594L) { calc(parse(testInputStr),50) }
    }

    @Test
    fun test5000() {
//        expect(16733044L) { calc(parse(testInputStr),5000) }
    }

}
