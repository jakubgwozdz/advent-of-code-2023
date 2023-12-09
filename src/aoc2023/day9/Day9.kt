package aoc2023.day9

import aoc2023.Puzzle
import aoc2023.getDay
import aoc2023.readAndParse

fun main() {
    val input = readAndParse("local/${getDay {}}_input.txt", ::parse)
    val puzzle = Puzzle(input, ::part1, ::part2)
    puzzle.part1() // 1637452029
    puzzle.part2() // 908
}

fun parse(inputStr: String): Input = inputStr.lines().filterNot { it.isBlank() }
    .map { it.split(" ").map(String::toInt) }

typealias Input = List<List<Int>>

fun List<Int>.calcNext(): Int = if (any { it != 0 }) last() + zipWithNext { a, b -> b - a }.calcNext() else 0

fun part1(input: Input) = input.sumOf(List<Int>::calcNext)
fun part2(input: Input) = input.map(List<Int>::reversed).sumOf(List<Int>::calcNext)
