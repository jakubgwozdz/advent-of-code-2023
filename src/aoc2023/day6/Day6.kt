package aoc2023.day6

import aoc2023.Puzzle
import aoc2023.isqrt
import aoc2023.getDay
import aoc2023.longs
import aoc2023.readAndParse

fun main() {
    val input = readAndParse("local/${getDay {}}_input.txt", ::parse)
    val puzzle = Puzzle(input, ::part1, ::part2)
    puzzle.part1()
    puzzle.part2()
}

data class Input(val time: String, val distance: String)

fun parse(inputStr: String): Input {
    val (t, d) = inputStr.lines()
    return Input(t.substringAfter(":"), d.substringAfter(":"))
}

fun Input.zipLongs() = time.longs().zip(distance.longs(), ::calc)
fun calc(t: Long, d: Long): Long = t - 2 * lastNotBetter(t, d) - 1
fun lastNotBetter(t: Long, d: Long) = (t - (t * t - 4 * d).isqrt(ceil = true)) / 2

fun part1(input: Input) = input
    .zipLongs()
    .reduce(Long::times)

fun Input.fixForPart2() = Input(time.filterNot(Char::isWhitespace), distance.filterNot(Char::isWhitespace))
fun part2(input: Input) = input
    .fixForPart2()
    .zipLongs()
    .reduce(Long::times)
