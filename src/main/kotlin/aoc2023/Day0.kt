package aoc2023

import java.io.File

fun main() {
    val prod = Day0(File("local/day0_input.txt").readText().trim().let(::day0parse))
    calculate(prod::part1)
    calculate(prod::part2)
}

fun day0parse(input: String) = Day0.Input(input.toInt())

class Day0(private val parsed: Input) {

    data class Input(val value: Int)

    fun part1(): Any {
        return parsed
    }

    fun part2(): Any {
        return parsed.toString().length
    }

}
