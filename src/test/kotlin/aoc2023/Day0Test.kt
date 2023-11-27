package aoc2023

import kotlin.test.Test
import kotlin.test.expect

class Day0Test {
    private val testInput = day0parse("333")
    private val solution = Day0(testInput)

    @Test
    fun part1test() {
        expect(333) { solution.part1() }
    }

    @Test
    fun part2test() {
        expect(3) { solution.part2() }
    }

}
