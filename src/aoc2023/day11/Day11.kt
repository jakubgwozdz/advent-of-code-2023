package aoc2023.day11

import aoc2023.Puzzle
import aoc2023.getDay
import aoc2023.logged
import aoc2023.readAndParse
import kotlin.math.absoluteValue

fun main() {
    listOf(0L, 1L, 1L, 3L, 5L, 0L).runningFold(5L) { acc, i -> acc + i }.logged()

    val input = readAndParse("local/${getDay {}}_input.txt", ::parse)
    val puzzle = Puzzle(input, ::part1, ::part2)
    puzzle.part1()
    puzzle.part2()
}

typealias Input = List<Pair<Int, Int>>

fun parse(inputStr: String): Input = inputStr.lines().flatMapIndexed { row, line ->
    line.mapIndexedNotNull { col, ch -> if (ch == '#') row to col else null }
}

fun part1(input: Input) = input.calc2D(2)
fun part2(input: Input) = input.calc2D(1000000)

fun Input.calc2D(expansion: Int) = map { it.first }.calc1D(expansion) + map { it.second }.calc1D(expansion)

private fun List<Int>.calc1D(expansion: Int) = expanded(expansion).distances()

private fun List<Long>.distances() = sumOf { r1 -> sumOf { r2 -> (r2 - r1).absoluteValue } } / 2

private fun List<Int>.expanded(expansion: Int): List<Long> = sorted()
    .zipWithNext { a, b -> (b - a).toLong() }
    .map { if (it > 1) (it - 1) * expansion + 1 else it }
    .runningFold(first().toLong(), Long::plus)
