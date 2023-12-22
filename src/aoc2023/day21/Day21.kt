package aoc2023.day21

import aoc2023.Puzzle
import aoc2023.expect
import aoc2023.getDay
import aoc2023.readAndParse

fun main() {
    val input = readAndParse("local/${getDay {}}_input.txt", ::parse)
    val puzzle = Puzzle(input, ::part1, ::part2)
    puzzle.part1().expect(3751)
    puzzle.part2().expect(619407349431167)
}

typealias Input = List<String>

fun parse(inputStr: String): Input {
    return inputStr.lines().filterNot { it.isBlank() }
}

fun part1(input: Input) = input.calc(64).single()

fun part2(input: Input): Any {
    val cycle = input.size
    val times = 26501365 / cycle
    val rest = 26501365 % cycle

    val (f1, f2, f3) = input.calc(cycle + rest, 2 * cycle + rest, 3 * cycle + rest)

    // f(n) = n^2 * x + n * y + z

    // f1 = x + y + z
    // f2 = 4x + 2y + z
    // f3 = 9x + 3y + z

    val x = ((f1 + f3 - 2 * f2) / 2L)
    val y = (f2 - f1 - 3 * x)
    val z = (f1 - x - y)
    return x * times * times + y * times + z
}

data class Pos(val r: Int, val c: Int)

fun Pos.moves() = listOf(
    Pos(r - 1, c),
    Pos(r, c + 1),
    Pos(r + 1, c),
    Pos(r, c - 1),
)

fun Input.calc(vararg steps: Int): List<Int> {
    val result = mutableMapOf<Int, Int>()
    val initial: Pos = indexOfFirst { it.contains("S") }.let { r -> Pos(r, this[r].indexOf('S')) }

    val found = mutableMapOf(-1 to emptySet(), 0 to setOf(initial))
    val sums = mutableMapOf(-1 to 0, 0 to 1)

    (1..steps.max()).forEach {
        found[it] = found[it - 1]!!.flatMap { p ->
            p.moves().filter { (r, c) -> this[r.mod(this.size)][c.mod(this.size)] != '#' }
        }.toSet() - found[it - 2]!!
        sums[it] = found[it]!!.count() + sums[it - 2]!!
        if (it in steps) result[it] = sums[it]!!
    }
    return result.values.toList()
}
