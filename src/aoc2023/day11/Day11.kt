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

fun List<Int>.calc1D(expansion: Int) = expanded(expansion).distinctWithCounts().distances()

fun List<Int>.expanded(expansion: Int): List<Long> = sorted()
    .zipWithNext { a, b -> (b - a).toLong() }
    .map { if (it > 1) (it - 1) * expansion + 1 else it }
    .runningFold(first().toLong(), Long::plus)

fun List<Long>.distinctWithCounts() = groupBy { it }.map { it.key to it.value.size }

fun List<Pair<Long, Int>>.distances() =
    sumOf { (v1, c1) -> sumOf { (v2, c2) -> (v2 - v1).absoluteValue * c1 * c2 } } / 2
