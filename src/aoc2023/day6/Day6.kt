package aoc2023.day6

import aoc2023.Puzzle
import aoc2023.getDay
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
//    .logged()

fun calc(t: Long, d: Long): Long = t - 2 * firstBetter(t, d) + 1

fun firstBetter(t: Long, d: Long) = (t - (t * t - 4 * d).ceilSqrt()) / 2 + 1

// inspired by pseudocode at https://rosettacode.org/wiki/Isqrt_(integer_square_root)_of_X
fun Long.ceilSqrt(): Long {
    require(this >= 0)
    var q = 1L
    while (q <= this) q = q shl 2
    var z = this
    var r = 0L
    while (q > 1L) {
        q = q shr 2
        val t = z - r - q
        r = r shr 1
        if (t >= 0) {
            z = t
            r += q
        }
    }
    return if (z == 0L) r else r + 1
}

private fun part1Races(input: Input) = input.time.longs().zip(input.distance.longs())
private fun part2Races(input: Input) =
    input.time.filterNot { it.isWhitespace() }.longs().zip(input.distance.filterNot { it.isWhitespace() }.longs())

fun part1(input: Input) = part1Races(input).map { (t, d) -> calc(t, d) }.reduce(Long::times)

fun part2(input: Input) = part2Races(input).map { (t, d) -> calc(t, d) }.reduce(Long::times)




