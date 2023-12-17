package aoc2023.day17

import org.junit.jupiter.api.Timeout
import kotlin.test.Test
import kotlin.test.expect

class Day17Test {

    val testInputStr = """
        2413432311323
        3215453535623
        3255245654254
        3446585845452
        4546657867536
        1438598798454
        4457876987766
        3637877979653
        4654967986887
        4564679986453
        1224686865563
        2546548887735
        4322674655533
    """.trimIndent()

    @Test
    @Timeout(5)
    fun part1test() {
        expect(102) { part1(parse(testInputStr)) }
    }

    @Test
    @Timeout(5)
    fun part2test() {
        expect(94) { part2(parse(testInputStr)) }
    }

}
