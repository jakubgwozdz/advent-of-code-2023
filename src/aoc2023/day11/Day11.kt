package aoc2023.day11

import aoc2023.Puzzle
import aoc2023.getDay
import aoc2023.logged
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

fun part1(input: Input) = calc(input, 2)
fun part2(input: Input) = calc(input, 1000000)

fun calc(input: Input, expansion: Int): Long {
    val rows = input.map { it.first }.expanded(expansion)
    val cols = input.map { it.second }.expanded(expansion)
    val pairs = input.flatMapIndexed { index, (r1, c1) ->
        input.subList(index, input.size)
            .map { (r2, c2) ->
                (rows[r2] - rows[r1]).absoluteValue + (cols[c2] - cols[c1]).absoluteValue
            }
    }
    return pairs.sum()
}

private fun List<Int>.expanded(expansion: Int): List<Long> {
    var d = 0L
    return (0..max()).map { it + if (it in this) d else d.also { d += expansion - 1 } }
}
