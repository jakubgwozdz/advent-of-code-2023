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

fun parse(inputStr: String): Input {
    val lines = inputStr.lines().filterNot { it.isBlank() }
    val instruction = lines.first().map { Choice.valueOf(it.toString()) }
    val regex = "(...) = \\((...), (...)\\)".toRegex()
    val map = lines.drop(1).map { regex.matchEntire(it) }
        .map { it!!.destructured }
        .associate { (a, b, c) -> a to (b to c) }
    return Input(instruction, map)
}

enum class Choice { L, R }
typealias State = Pair<Long, String>
data class Input(val instruction: List<Choice>, val map: Map<String, Pair<String, String>>)

fun State.next(input: Input): State {
    val (steps, current) = this
    val result = when (input.instruction[(steps % input.instruction.size).toInt()]) {
        Choice.L -> input.map[current]!!.first
        Choice.R -> input.map[current]!!.second
    }
    return steps + 1 to result
}

fun find(input: Input, start: String, predicate: (String) -> Boolean) =
    generateSequence(0L to start) { if (predicate(it.second)) null else it.next(input) }.last().first

fun part1(input: Input) = find(input, "AAA") { it == "ZZZ" }

// this is waaaaay oversimplified but works for the input
fun part2(input: Input) = input.map.keys.filter { it.endsWith('A') }
    .map { start -> find(input, start) { it.endsWith('Z') } }
    .reduce { a, b -> lcm(a, b) }

