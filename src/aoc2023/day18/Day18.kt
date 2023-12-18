package aoc2023.day18

import aoc2023.Puzzle
import aoc2023.day18.Dir.D
import aoc2023.day18.Dir.L
import aoc2023.day18.Dir.R
import aoc2023.day18.Dir.U
import aoc2023.expect
import aoc2023.getDay
import aoc2023.readAndParse
import kotlin.math.absoluteValue

fun main() {
    val input = readAndParse("local/${getDay {}}_input.txt", ::parse)
    val puzzle = Puzzle(input, ::part1, ::part2)
    puzzle.part1().expect(50746L)
    puzzle.part2().expect(70086216556038)
}

enum class Dir { R, D, L, U }

data class Entry(val dir: Dir, val times: Int, val color: Int)
typealias Input = List<Entry>

val regex = "([LRDU]) (\\d+) \\(#(.{6})\\)".toRegex()
fun parse(inputStr: String): Input = inputStr.lines()
    .mapNotNull { regex.matchEntire(it) }
    .map { it.destructured }
    .map { (d, c, color) -> Entry(Dir.valueOf(d), c.toInt(), color.toInt(16)) }


fun part1(input: Input) = calc(input.map { it.dir to it.times })
fun part2(input: Input) = calc(input.map { (_, _, c) -> Dir.entries[c % 16] to c / 16 })

fun calc(data: List<Pair<Dir, Int>>): Long {
    var area = 0L
    var outline = 0L
    var y = 0L
    data.forEach { (d, t) ->
        when (d) {
            R -> area += y * t
            D -> y += t
            L -> area -= y * t
            U -> y -= t
        }
        outline += t
    }
    return area.absoluteValue + outline / 2 + 1
}
