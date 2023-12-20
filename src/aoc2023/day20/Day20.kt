package aoc2023.day20

import aoc2023.Puzzle
import aoc2023.day20.Type.Broadcaster
import aoc2023.day20.Type.Conjunction
import aoc2023.day20.Type.FlipFlop
import aoc2023.getDay
import aoc2023.readAndParse

fun main() {
    val input = readAndParse("local/${getDay {}}_input.txt", ::parse)
    val puzzle = Puzzle(input, ::part1, ::part2)
    puzzle.part1()
    puzzle.part2()
}

enum class Type { Broadcaster, FlipFlop, Conjunction }
typealias Input = Map<String, Pair<Type, List<String>>>

fun parse(inputStr: String): Input = inputStr.lines().filterNot { it.isBlank() }
    .map { it.split(" -> ") }
    .associate { (f, t) ->
        val (name, type) = when {
            f == "broadcaster" -> f to Broadcaster
            f.startsWith("%") -> f.drop(1) to FlipFlop
            f.startsWith("&") -> f.drop(1) to Conjunction
            else -> error(f)
        }
        val outputs = t.split(", ")
        name to (type to outputs)
    }

fun part1(input: Input): Any? = input.toString() // TODO("part 1 with ${input.toString().length} data")

fun part2(input: Input): Any? = null // TODO("part 2 with ${input.toString().length} data")


