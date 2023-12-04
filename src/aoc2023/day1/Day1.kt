package aoc2023.day1

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
}

fun part1(input: Input) = input.sumOf(String::part1calibration)
fun part2(input: Input) = input.sumOf(String::part2calibration)

private fun String.part1calibration() = first(Char::isDigit).digitToInt() * 10 + last(Char::isDigit).digitToInt()

private fun String.part2calibration(): Int {
    val l1: Int = indices.firstNotNullOf { betterToDigitOrNull(it) }
    val l2: Int = indices.reversed().firstNotNullOf { betterToDigitOrNull(it) }
    return l1 * 10 + l2
}

private fun String.betterToDigitOrNull(startIndex: Int): Int? =
    if (this[startIndex].isDigit()) this[startIndex].digitToInt()
    else digitNames.firstNotNullOfOrNull { (d, name) -> if (startsWith(name, startIndex)) d else null }

val digitNames = listOf(
    1 to "one",
    2 to "two",
    3 to "three",
    4 to "four",
    5 to "five",
    6 to "six",
    7 to "seven",
    8 to "eight",
    9 to "nine",
)
