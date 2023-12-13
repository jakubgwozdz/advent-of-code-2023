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
    fun trimDots(): Row =
        copy(springs = springs.dropWhile { it == '.' }.dropLastWhile { it == '.' }.replace("\\.+".toRegex(), "."))
}
typealias Input = List<Row>

fun parse(inputStr: String): Input = inputStr.lines().filterNot { it.isBlank() }.map { line ->
    line.split(" ").let { (springs, i) -> Row(springs, i.ints(",")) }
}

fun calc(row: Row): Long {

    val (springs, groups) = row
    val minimals = groups.reversed().runningReduce { acc: Int, i: Int -> acc + 1 + i }.reversed()
    val firstNotDot = springs
        .reversed()
        .runningFoldIndexed(springs.length) { index, acc, c -> if (c == '.') acc else springs.length - index - 1 }
        .reversed()
    val lastHash = springs.lastIndexOf('#')

    data class State(val s: Int = 0, val g: Int = 0) {
        fun skipDots() = copy(s = firstNotDot[s])
        fun assumeDot() = copy(s = firstNotDot[s + 1])
        fun skipFirstGroup() = copy(s = s + groups[g] + 1, g = g + 1)

        fun noMoreGroups() = g >= groups.size
        fun success() = g >= groups.size && s > lastHash
        fun firstDot() = springs[s] == '.'
        fun firstQuestion() = springs[s] == '?'
        fun wontFit() = minimals[g] > springs.length - s
        fun fitsBeginning() = springs.length - s >= groups[g] &&
                (0..<groups[g]).none { springs[s + it] == '.' } &&
                springs.getOrNull(s + groups[g]) != '#'
    }

//    val cache = Cache()
    val cache = mutableMapOf<State, Long>()

    fun calcRecursive(state: State): Long = cache[state] ?: when {
        state.success() -> 1
        state.noMoreGroups() -> 0
        state.wontFit() -> 0
        state.firstDot() -> calcRecursive(state.skipDots())
        else -> (if (state.firstQuestion()) calcRecursive(state.assumeDot()) else 0L) +
                (if (state.fitsBeginning()) calcRecursive(state.skipFirstGroup()) else 0L)
    }.also { cache[state] = it }

    return calcRecursive(State())
}

fun part1(input: Input) = input.sumOf { calc(it.trimDots()) }
fun part2(input: Input) = input.sumOf { calc(it.repeated(5).trimDots()) }
