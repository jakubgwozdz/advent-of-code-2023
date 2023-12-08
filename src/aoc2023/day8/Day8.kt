package aoc2023.day8

import aoc2023.Puzzle
import aoc2023.getDay
import aoc2023.lcm
import aoc2023.readAndParse

fun main() {
    val input = readAndParse("local/${getDay {}}_input.txt", ::parse)
    val puzzle = Puzzle(input, ::part1, ::part2)
    puzzle.part1()
    puzzle.part2()
}

enum class Choice { L, R }
typealias State = Pair<Long, String>
data class Input(val instruction: List<Choice>, val map: Map<String, Pair<String, String>>) {
    fun next(from: State): State {
        val (steps, current) = from
        val result = when (instruction[(steps % instruction.size).toInt()]) {
            Choice.L -> map[current]!!.first
            Choice.R -> map[current]!!.second
        }
        return steps + 1 to result
    }
}

fun parse(inputStr: String): Input {
    val lines = inputStr.lines().filterNot { it.isBlank() }
    val instruction = lines.first().map { Choice.valueOf(it.toString()) }
    val regex = "(...) = \\((...), (...)\\)".toRegex()
    val map = lines.drop(1).map { regex.matchEntire(it) }
        .map { it!!.destructured }
        .associate { (a, b, c) -> a to (b to c) }
    return Input(instruction, map)
}

fun part1(input: Input) = find(input, "AAA") { it == "ZZZ" }

private fun find(input: Input, start: String, predicate: (String) -> Boolean): Long {
    var state = (0L to start)
    while (!predicate(state.second)) state = input.next(state)
    return state.first
}

// this is waaaaay oversimplified but works for the input
fun part2(input: Input) = input.map.keys.filter { it.endsWith('A') }
    .map { start -> find(input, start) { it.endsWith('Z') } }
    .reduce { a, b -> lcm(a, b) }
//9064949303801
