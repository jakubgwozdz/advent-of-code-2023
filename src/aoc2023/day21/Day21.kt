package aoc2023.day21

import aoc2023.Puzzle
import aoc2023.day21.Dir.E
import aoc2023.day21.Dir.N
import aoc2023.day21.Dir.S
import aoc2023.day21.Dir.W
import aoc2023.expect
import aoc2023.getDay
import aoc2023.readAndParse

fun main() {
    val input = readAndParse("local/${getDay {}}_input.txt", ::parse)
    val puzzle = Puzzle(input, ::part1, ::part2)
    puzzle.part1().expect(3751L)
    puzzle.part2()
}

typealias Input = List<String>

fun parse(inputStr: String): Input {
    return inputStr.lines().filterNot { it.isBlank() }
}

enum class Dir { N, E, S, W }
data class Pos(val r: Int, val c: Int)

operator fun Pos.plus(d: Dir): Pos = when (d) {
    N -> Pos(r - 1, c)
    E -> Pos(r, c + 1)
    S -> Pos(r + 1, c)
    W -> Pos(r, c - 1)
}

fun part1(input: Input) = calc(input, 64)
fun part2(input: Input) = calc(input, 26501365)

fun calc(input: Input, steps: Int): Long {
    val size = input.size
    check(input.all { it.length == size })
    val initial: Pos = input.indexOfFirst { it.contains("S") }.let { r -> Pos(r, input[r].indexOf('S')) }
    val array = Array(size) { BooleanArray(size) }
    input.indices.forEach { r -> input[r].indices.forEach { c -> array[r][c] = input[r][c] != '#' } }

    var prev = setOf(initial)
    repeat(steps) {
        prev = prev.flatMap { p ->
            Dir.entries.map { p + it }.filter { (r, c) -> array[r.mod(size)][c.mod(size)] }
        }.toSet() - prev
    }

    array.indices.forEach { r ->
        println(array[r].indices.map { c -> if (Pos(r, c) in prev) 'O' else if (array[r][c]) '.' else '#' }
            .joinToString(""))
    }
    return prev.size.toLong()
}
