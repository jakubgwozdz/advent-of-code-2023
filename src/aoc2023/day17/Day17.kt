package aoc2023.day17

import aoc2023.Puzzle
import aoc2023.getDay
import aoc2023.readAndParse

fun main() {
    val input = readAndParse("local/${getDay {}}_input.txt", ::parse)
    val puzzle = Puzzle(input, ::part1, ::part2)
    puzzle.part1()
    puzzle.part2()
}

typealias Input = List<String>

fun parse(inputStr: String): Input {
    return inputStr.lines().filterNot { it.isBlank() }
//    return Input("Damage: (\\d+)".toRegex().find(inputStr)?.destructured?.component1()?.toInt()!!)
//    TODO("parse ${inputStr.length} data")

}

fun part1(input: Input): Any? = input.toString() // TODO("part 1 with ${input.toString().length} data")

fun part2(input: Input): Any? = null // TODO("part 2 with ${input.toString().length} data")


