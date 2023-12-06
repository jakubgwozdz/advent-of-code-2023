package aoc2023.day6

import aoc2023.Puzzle
import aoc2023.getDay
import aoc2023.ints
import aoc2023.logged
import aoc2023.longs
import aoc2023.readAndParse

fun main() {
    val input = readAndParse("local/${getDay {}}_input.txt", ::parse)
    val puzzle = Puzzle(input, ::part1, ::part2)
    puzzle.part1()
    puzzle.part2()
}

data class Input(val time: String, val distance: String)

fun parse(inputStr: String) = inputStr.lines()
    .map { it.substringAfter(":").trim() }.let { (t, d) -> Input(t, d) }
    .logged()

fun calc(t: Long, d: Long): Long {
    var r = 0L
    var i = 0L
    while (i++<t) if (((t - i) * i) > d ) r++
    return r
}

fun part1(input: Input) =
    input.time.longs().zip(input.distance.longs())
        .map { (t, d) -> calc(t, d) }.fold(1L, Long::times)

fun part2(input: Input) =
    input.time.filterNot { it.isWhitespace() }.longs().zip(input.distance.filterNot { it.isWhitespace() }.longs())
        .map { (t, d) -> calc(t, d) }.fold(1L, Long::times)



