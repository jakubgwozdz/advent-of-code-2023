package aoc2023.day6

import aoc2023.Puzzle
import aoc2023.getDay
import aoc2023.logged
import aoc2023.longs
import aoc2023.readAndParse
import kotlin.math.ceil
import kotlin.math.sqrt

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

fun calc(t: Long, d: Long): Long = t - 2 * firstBetter(t, d) + 1

fun firstBetter(t: Long, d: Long) = (t - (t * t - 4 * d).ceilSqrt()) / 2 + 1

fun Long.ceilSqrt() = ceil(sqrt(toDouble())).toLong()

private fun part1Races(input: Input) = input.time.longs().zip(input.distance.longs())
private fun part2Races(input: Input) =
    input.time.filterNot { it.isWhitespace() }.longs().zip(input.distance.filterNot { it.isWhitespace() }.longs())

fun part1(input: Input) = part1Races(input).map { (t, d) -> calc(t, d) }.reduce(Long::times)

fun part2(input: Input) = part2Races(input).map { (t, d) -> calc(t, d) }.reduce(Long::times)




