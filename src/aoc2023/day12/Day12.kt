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
    fun trimDots(): Row =
        copy(springs = springs.dropWhile { it == '.' }.dropLastWhile { it == '.' }.replace("\\.+".toRegex(), "."))
}
typealias Input = List<Row>

fun parse(inputStr: String): Input = inputStr.lines().filterNot { it.isBlank() }.map { line ->
    line.split(" ").let { (springs, i) -> Row(springs, i.ints(",")) }
}

fun String.indexOfFirstNotDot(start: Int) =
    generateSequence(start) { if (getOrNull(it) == '.') it + 1 else null }.last()

fun calc(row: Row): Long {

    val minimals = row.groups.reversed().runningFold(-1) { acc: Int, i: Int -> acc + 1 + i }
        .drop(1)
        .reversed()

    data class State(val s: Int = 0, val g: Int = 0) {
        fun skipDots() = copy(s = row.springs.indexOfFirstNotDot(s))
        fun assumeDot() = copy(s = s + 1)
        fun skipFirstGroup() = copy(s = s + row.groups[g] + 1, g = g + 1)

        fun noMoreGroups() = row.groups.size <= g
        fun noMoreHash() = (s..row.springs.lastIndex).none { row.springs[it] == '#' }
        fun firstDot() = row.springs[s] == '.'
        fun firstQuestion() = row.springs[s] == '?'
        fun onlyDotsLeft() = (s..row.springs.lastIndex).all { row.springs[it] == '.' }
        fun wontFit() = minimals[g] > row.springs.length - s
        fun fitsBeginning(): Boolean {
            val len = row.groups[g]
            val next = row.springs.drop(s).take(len + 1)
            return next.length >= len && (0..<len).none { next[it] == '.' } && next.getOrNull(len) != '#'
        }
    }

    return cachedDeepRecursiveFunction<State, Long> {
        when {
            it.noMoreGroups() && it.noMoreHash() -> 1
            it.noMoreGroups() -> 0
            it.onlyDotsLeft() -> 0
            it.wontFit() -> 0
            it.firstDot() -> callRecursive(it.skipDots())
            it.firstQuestion() && it.fitsBeginning() -> callRecursive(it.assumeDot()) + callRecursive(it.skipFirstGroup())
            it.firstQuestion() -> callRecursive(it.assumeDot())
            it.fitsBeginning() -> callRecursive(it.skipFirstGroup())
            else -> 0
        }
    }(State())
}

fun part1(input: Input) = input.sumOf { calc(it.trimDots()) }
fun part2(input: Input) = input.sumOf { calc(it.repeated(5).trimDots()) }
