package aoc2023.day25

import aoc2023.Puzzle
import aoc2023.expect
import aoc2023.getDay
import aoc2023.readAndParse

fun main() {
    val input = readAndParse("local/${getDay {}}_input.txt", ::parse)
    val puzzle = Puzzle(input, ::part1, ::part2)
    puzzle.part1().expect(552695)
    puzzle.part2()
}

typealias Input = Map<String, List<String>>

fun parse(inputStr: String): Input = buildMap<String, MutableList<String>> {
    inputStr.lines().filterNot { it.isBlank() }.forEach { line ->
        val (k, vs) = line.split(":").map { it.trim() }
        vs.split(" ").map { it.trim() }.filter { it.isNotBlank() }.forEach { v ->
            computeIfAbsent(k) { mutableListOf() } += (v)
            computeIfAbsent(v) { mutableListOf() } += (k)
        }
    }
}.mapValues { (_, v) -> v.toList() }

fun Pair<String, String>.sorted() = if (first <= second) this else second to first

// not 100% correct, but works for my input
fun part1(input: Input): Int {
    val subgraph = input.keys.toMutableSet()
    fun count(v: String) = input[v]!!.count { it !in subgraph }
    while (subgraph.sumOf(::count) != 3) subgraph.remove(subgraph.maxBy(::count))
    return subgraph.size * (input.size - subgraph.size)
}

fun part2(input: Input): Any? = null // TODO("part 2 with ${input.toString().length} data")


