package aoc2023.day12

import aoc2023.Puzzle
import aoc2023.getDay
import aoc2023.ints
import aoc2023.readAndParse

fun main() {
    val input = readAndParse("local/${getDay {}}_input.txt", ::parse)
    val puzzle = Puzzle(input, ::part1, ::part2)
    puzzle.part1()
    puzzle.part2()
}

data class Row(val springs: String, val groups: List<Int>)
typealias Input = List<Row>

fun parse(inputStr: String): Input = inputStr.lines().filterNot { it.isBlank() }.map { line ->
    line.split(" ").let { (springs, i) -> Row(springs, i.ints(",")) }
}

fun calc(row: Row): Long = DeepRecursiveFunction<Row, Long> { (springs, groups) ->
    when {
        groups.isEmpty() -> if ('#' in springs) 0 else 1
        springs.isEmpty() -> if (groups.isEmpty()) 1 else 0
        springs.first() == '.' -> callRecursive(Row(springs.drop(1), groups))
        springs.first() == '?' -> callRecursive(Row(springs.replaceFirst('?', '.'), groups)) +
                callRecursive(Row(springs.replaceFirst('?', '#'), groups))

        !fits(springs, groups.first()) -> 0
        else -> callRecursive(Row(springs.drop(groups.first() + 1), groups.drop(1)))
    }
}(row)

private fun fits(springs: String, len: Int) =
    springs.length >= len && '.' !in springs.take(len) && (springs.length < len + 1 || springs[len] != '#')

fun part1(input: Input) = input.sumOf { calc(it) }

fun part2(input: Input) = input.sumOf { calc(fiveTimes(it)) }

fun fiveTimes(row: Row): Row {
    return Row((1..5).map { row.springs }.joinToString("?"), buildList{ repeat(5) { addAll(row.groups)} })
}


