package aoc2023.day12

import org.junit.jupiter.api.Timeout
import kotlin.test.Test
import kotlin.test.expect

class Day12Test {

    val testInputStr = """
        ???.### 1,1,3
        .??..??...?##. 1,1,3
        ?#?#?#?#?#?#?#? 1,3,1,6
        ????.#...#... 4,1,1
        ????.######..#####. 1,6,5
        ?###???????? 3,2,1
    """.trimIndent()

//    @Test
//    @Timeout(1)
//    fun parseTest() {
//        expect(testInput) { parse(testInputStr) }
//    }

    @Test
    @Timeout(5)
    fun part1testParts() {
        expect(listOf(1L, 4L, 1L, 1L, 4L, 10L)) { parse(testInputStr).map { calc(it) } }
    }

    @Test
    @Timeout(5)
    fun part1test() {
        expect(21L) { part1(parse(testInputStr)) }
    }

    @Test
    @Timeout(5)
    fun part2test() {
        expect(525152L) { part2(parse(testInputStr)) }
    }

}
