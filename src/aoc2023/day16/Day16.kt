package aoc2023.day16

import aoc2023.Puzzle
import aoc2023.day16.Dir.E
import aoc2023.day16.Dir.N
import aoc2023.day16.Dir.S
import aoc2023.day16.Dir.W
import aoc2023.expect
import aoc2023.getDay
import aoc2023.readAndParse

fun main() {
    val input = readAndParse("local/${getDay {}}_input.txt", ::parse)
    val puzzle = Puzzle(input, ::part1, ::part2)
    puzzle.part1().expect(7236)
    puzzle.part2().expect(7521)
}

typealias Input = List<String>

fun parse(inputStr: String): Input {
    return inputStr.lines().filterNot { it.isBlank() }
}

enum class Dir { N, E, S, W }
data class Entry(val r: Int, val c: Int, val dir: Dir)

operator fun Input.contains(entry: Entry) = entry.r in indices && entry.c in this[entry.r].indices
operator fun Input.get(entry: Entry) = this[entry.r][entry.c]
operator fun Entry.plus(d: Dir): Entry = when (d) {
    N -> Entry(r - 1, c, d)
    E -> Entry(r, c + 1, d)
    S -> Entry(r + 1, c, d)
    W -> Entry(r, c - 1, d)
}

operator fun Int.contains(dir: Dir) = this and (1 shl dir.ordinal) != 0
operator fun Int.plus(dir: Dir) = this or (1 shl dir.ordinal)
operator fun Array<IntArray>.contains(e: Entry) = e.dir in this[e.r][e.c]
operator fun Array<IntArray>.plusAssign(e: Entry) { this[e.r][e.c] = this[e.r][e.c] + e.dir }

fun Input.calc(start: Entry): Int {
    val visited = Array(size) { IntArray(first().length) }
    val toGo = mutableListOf(start)
    while (toGo.isNotEmpty()) {
        val e = toGo.removeFirst()
        if (e in this && e !in visited) {
            visited += e
            when (e.dir) {
                N -> when (this[e]) {
                    '-' -> toGo += listOf(e + E, e + W)
                    '/' -> toGo += e + E
                    '\\' -> toGo += e + W
                    else -> toGo += e + N
                }

                E -> when (this[e]) {
                    '|' -> toGo += listOf(e + S, e + N)
                    '/' -> toGo += e + N
                    '\\' -> toGo += e + S
                    else -> toGo += e + E
                }

                S -> when (this[e]) {
                    '-' -> toGo += listOf(e + E, e + W)
                    '/' -> toGo += e + W
                    '\\' -> toGo += e + E
                    else -> toGo += e + S
                }

                W -> when (this[e]) {
                    '|' -> toGo += listOf(e + S, e + N)
                    '/' -> toGo += e + S
                    '\\' -> toGo += e + N
                    else -> toGo += e + W
                }
            }
        }
    }
    return indices.sumOf { r-> this[r].indices.count { c-> visited[r][c] != 0 } }
}

fun part1(input: Input) = input.calc(Entry(0, 0, E))

fun Input.borders() = indices.map { r -> Entry(r, this[r].indices.first, E) } +
        indices.map { r -> Entry(r, this[r].indices.last, W) } +
        first().indices.map { c -> Entry(indices.first, c, S) } +
        first().indices.map { c -> Entry(indices.last, c, N) }

fun part2(input: Input) = input.borders().maxOf { input.calc(it) }
