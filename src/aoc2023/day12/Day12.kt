package aoc2023.day12

import aoc2023.Puzzle
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

fun calc(row: Row): Long {
    data class State(val springs: String, val groups: List<Int>) {
        fun skipDots() = copy(springs = springs.dropWhile { it == '.' })
        fun assumeDot() = copy(springs = "." + springs.drop(1))
        fun assumeHash() = copy(springs = "#" + springs.drop(1))
        fun skipFirstGroup() = State(springs.drop(groups.first() + 1), groups.drop(1))

        fun noMoreGroups() = groups.isEmpty()
        fun noMoreHash() = '#' !in springs
        fun firstDot() = springs.first() == '.'
        fun firstQuestion() = springs.first() == '?'
        fun onlyDotsLeft() = springs.all { it == '.' }
        fun fitsBeginning() = springs.length >= groups.first() &&
                '.' !in springs.take(groups.first()) &&
                (springs.length < groups.first() + 1 || springs[groups.first()] != '#')
    }

    val cache = mutableMapOf<State, Long>()
    return DeepRecursiveFunction<State, Long> { state ->
        when {
            state in cache -> cache[state]!!
            state.noMoreGroups() -> if (state.noMoreHash()) 1 else 0
            state.onlyDotsLeft() -> if (state.noMoreGroups()) 1 else 0
            state.firstQuestion() -> callRecursive(state.assumeHash()) + callRecursive(state.assumeDot())
            state.firstDot() -> callRecursive(state.skipDots())
            state.fitsBeginning() -> callRecursive(state.skipFirstGroup())
            else -> 0
        }.also { cache[state] = it }
    }(State(row.springs, row.groups))
}

fun part1(input: Input) = input.sumOf { calc(it) }
fun part2(input: Input) = input.sumOf { calc(it.repeated(5)) }
