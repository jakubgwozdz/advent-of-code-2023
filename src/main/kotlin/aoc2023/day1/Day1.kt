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

private fun String.betterToDigitOrNull(startIndex: Int): Int? = when {
    this[startIndex].isDigit() -> this[startIndex].digitToInt()
    startsWith("zero", startIndex) -> 0
    startsWith("one", startIndex) -> 1
    startsWith("two", startIndex) -> 2
    startsWith("three", startIndex) -> 3
    startsWith("four", startIndex) -> 4
    startsWith("five", startIndex) -> 5
    startsWith("six", startIndex) -> 6
    startsWith("seven", startIndex) -> 7
    startsWith("eight", startIndex) -> 8
    startsWith("nine", startIndex) -> 9
    else -> null
}


