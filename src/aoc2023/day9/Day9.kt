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

//data class Input(val todo: Int)
typealias Input = List<List<Int>>

fun parse(inputStr: String): Input = inputStr.lines().filterNot { it.isBlank() }
    .map { it.split(" ").map(String::toInt) }

fun calcNext(history: List<Int>): Int = if (history.all { it == 0 }) 0
else history.windowed(2) { (a, b) -> b - a }
    .let { history.last() + calcNext(it) }

fun calcPrev(history: List<Int>): Int = if (history.all { it == 0 }) 0
else history.windowed(2) { (a, b) -> b - a }
    .let { history.first() - calcPrev(it) }

fun part1(input: Input) = input.sumOf { list -> calcNext(list) }
fun part2(input: Input) = input.sumOf { list -> calcPrev(list) }
