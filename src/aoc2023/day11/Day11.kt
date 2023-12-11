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

typealias Pos = Pair<Long, Long>
typealias Input = List<Pos>

fun parse(inputStr: String): Input = inputStr.lines().flatMapIndexed { row, line ->
    line.mapIndexedNotNull { col, ch -> if (ch == '#') row.toLong() to col.toLong() else null }
}

fun part1(input: Input) = calc(input, 2)
fun part2(input: Input) = calc(input, 1000000)

fun calc(input: Input, expansion: Long) = input.expanded2D(expansion).distances()

private fun Input.expanded2D(expansion: Long): List<Pos> {
    val rows = map { it.first }.expanded1D(expansion)
    val cols = map { it.second }.expanded1D(expansion)
    return map { (r, c) -> rows[r.toInt()] to cols[c.toInt()] }
}

private fun List<Long>.expanded1D(expansion: Long): List<Long> {
    var d = 0
    return (0..max()).map { i -> i + (d * expansion - 1).also { if (i !in this) d++ } }
}

private fun List<Pos>.distances() = indices.sumOf { i1 ->
    val (r1, c1) = this[i1]
    (i1+1..lastIndex).sumOf { i2 ->
        val (r2, c2) = this[i2]
        (r2 - r1).absoluteValue + (c2 - c1).absoluteValue
    }
}
