package aoc2023.day11

import aoc2023.Puzzle
import aoc2023.getDay
import aoc2023.readAndParse
import kotlin.math.absoluteValue

fun main() {
    val input = readAndParse("local/${getDay {}}_input.txt", ::parse)
    val puzzle = Puzzle(input, ::part1, ::part2)
    puzzle.part1()
    puzzle.part2()
}

typealias Pos = Pair<Int, Int>
typealias Input = List<Pos>

fun parse(inputStr: String): Input = inputStr.lines().flatMapIndexed { row, line ->
    line.mapIndexedNotNull { col, ch -> if (ch == '#') row to col else null }
}

fun part1(input: Input) = calc2D(input, 2)
fun part2(input: Input) = calc2D(input, 1000000)

fun calc2D(input: Input, expansion: Int) =
    input.map { it.first }.calc1D(expansion) + input.map { it.second }.calc1D(expansion)

private fun List<Int>.calc1D(expansion: Int) = expanded(expansion).distances()

private fun List<Long>.distances() = sumOf { r1 -> sumOf { r2 -> (r2 - r1).absoluteValue } } / 2

private fun List<Int>.expanded(expansion: Int): List<Long> {
    var d = 0L
    val f = (0..max()).map { it + if (it in this) d else d.also { d += expansion - 1 } }
    return map { f[it] }
}
