package aoc2023.day13

import aoc2023.Puzzle
import aoc2023.getDay
import aoc2023.readAndParse

fun main() {
    val input = readAndParse("local/${getDay {}}_input.txt", ::parse)
    val puzzle = Puzzle(input, ::part1, ::part2)
    puzzle.part1()
    puzzle.part2()
}

typealias Mirror = List<String>
typealias Input = List<Mirror>

fun parse(inputStr: String): Input = inputStr.split("\n\n").map { it.lines().filterNot(String::isBlank) }
    .filterNot { it.isEmpty() }

fun findVertical(mirror: Mirror, ignore: Int): Int? = findHorizontal(mirror.transposed(), ignore)

private fun Mirror.transposed(): Mirror = (0..<first().length)
    .map { c -> buildString { this@transposed.forEach { row -> append(row[c]) } } }

fun findHorizontal(mirror: Mirror, ignore: Int) = (1..mirror.lastIndex)
    .filterNot { it == ignore }
    .firstOrNull {
        val before = mirror.take(it).reversed()
        val after = mirror.drop(it)
        val lines = before.size.coerceAtMost(after.size)
        before.take(lines) == after.take(lines)
    }

fun calc(mirror: Mirror, ignore: Int = 0) = findVertical(mirror, ignore % 100)
    ?: findHorizontal(mirror, ignore / 100)?.let { it * 100 }

fun part1(input: Input) = input.sumOf { calc(it) ?: error("no reflection") } // 37718

fun Char.flip() = when (this) {
    '#' -> '.'
    '.' -> '#'
    else -> error("$this?")
}

private fun Mirror.flip(r: Int, c: Int): Mirror =
    take(r) + this[r].let { it.take(c) + it[c].flip() + it.drop(c + 1) } + drop(r + 1)

fun part2(input: Input) = input.sumOf { mirror -> //40995
    val original = calc(mirror)!!
    mirror
        .indices.asSequence().flatMap { r -> mirror[r].indices.asSequence().map { c -> r to c } }
        .firstNotNullOf { (r, c) -> calc(mirror.flip(r, c), original) }
}
