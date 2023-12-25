package aoc2023.day25

import aoc2023.Puzzle
import aoc2023.expect
import aoc2023.getDay
import aoc2023.logged
import aoc2023.reachable
import aoc2023.readAndParse
import kotlin.random.Random

fun main() {
    val input = readAndParse("local/${getDay {}}_input.txt", ::parse)
    val puzzle = Puzzle(input, ::part1, ::part2)
    puzzle.part1().expect(552695)
    puzzle.part2()
}

//data class Input(val todo: Int)
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
//    .also { it.tgf("local/day25.tgf") }

fun part1(input: Input): Int {
    val nodes = input.keys.toList()
    val edges = input.flatMap { (k, l) -> l.map { if (k < it) k to it else it to k } }.toSet()

    fun krager(): Set<Pair<String, String>> {
        val random = Random(0)
        // Krager's algorithm
        while (true) {
            val labeledEdges = edges.associateWith { mutableListOf(it) }.toMutableMap()
            while (labeledEdges.size > 2) {
                val (v1, v2) = labeledEdges.keys.random(random)
                val contracted = labeledEdges.remove(v1 to v2)!!

            }
            labeledEdges.forEach { it.logged() }
        }
    }

    // hack using yEd
    val cut = setOf("xbl" to "qqh", "tbq" to "qfj", "xzn" to "dsr")

    val reachable = input.mapValues { (k, v) ->
        val o = cut.singleOrNull { it.first == k }?.second
            ?: cut.singleOrNull { it.second == k }?.first
        if (o != null) v - o
        else v
    }
        .reachable("xbl")
    return reachable.size * (input.size - reachable.size)
}

fun part2(input: Input): Any? = null // TODO("part 2 with ${input.toString().length} data")


