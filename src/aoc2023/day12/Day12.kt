package aoc2023.day12

import aoc2023.Puzzle
import aoc2023.cachedDeepRecursiveFunction
import aoc2023.getDay
import aoc2023.ints
import aoc2023.readAndParse

fun main() {
    val input = readAndParse("local/${getDay {}}_input.txt", ::parse)
    val puzzle = Puzzle(input, ::part1, ::part2)
    puzzle.part1()
    puzzle.part2()
}

data class Row(val springs: String, val groups: List<Int>) {
    fun repeated(i: Int) = Row((1..i).joinToString("?") { springs }, buildList { repeat(i) { addAll(groups) } })
}
typealias Input = List<Row>

fun parse(inputStr: String): Input = inputStr.lines().filterNot { it.isBlank() }.map { line ->
    line.split(" ").let { (springs, i) -> Row(springs, i.ints(",")) }
}

fun String.indexOfFirstNotDot(start: Int) = generateSequence(start) {
    if (getOrNull(it) == '.') it + 1 else null
}.last()

fun calc(row: Row): Long {
    data class State(val s: Int = 0, val g: Int = 0, val h: Int = 0) {
        fun skipDots() = copy(s = row.springs.indexOfFirstNotDot(s), h = 0)
        fun assumeDot() = copy(s = s + 1, h = 0)
        fun assumeHash() = copy(h = h + 1)
        fun skipFirstGroup() = copy(s = s + row.groups[g] + 1, g = g + 1, h = 0)

        fun noMoreGroups() = row.groups.size <= g
        fun noMoreHash() = (s..row.springs.lastIndex).none { row.springs[it] == '#' }
        fun firstDot() = h == 0 && row.springs[s] == '.'
        fun firstQuestion() = h == 0 && row.springs[s] == '?'
        fun onlyDotsLeft() = (s..row.springs.lastIndex).all { row.springs[it] == '.' }
        fun fitsBeginning(): Boolean {
            val len = row.groups[g]
            val next = "#".repeat(h) + row.springs.drop(s + h).take(len - h + 1)
            return next.length >= len &&
                    (0..<len).none { next[it] == '.' } &&
                    next.getOrNull(len) != '#'
        }
    }

    return cachedDeepRecursiveFunction<State, Long> {
        when {
            it.noMoreGroups() && it.noMoreHash() -> 1
            it.noMoreGroups() -> 0
            it.onlyDotsLeft() -> 0
            it.firstQuestion() -> callRecursive(it.assumeHash()) + callRecursive(it.assumeDot())
            it.firstDot() -> callRecursive(it.skipDots())
            it.fitsBeginning() -> callRecursive(it.skipFirstGroup())
            else -> 0
        }
    }(State())
}

fun part1(input: Input) = input.sumOf { calc(it) }
fun part2(input: Input) = input.sumOf { calc(it.repeated(5)) }
