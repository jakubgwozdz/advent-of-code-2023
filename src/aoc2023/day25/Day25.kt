package aoc2023.day25

import aoc2023.Puzzle
import aoc2023.Queue
import aoc2023.getDay
import aoc2023.logged
import aoc2023.readAndParse

fun main() {
    val input = readAndParse("local/${getDay {}}_input.txt", ::parse)
    val puzzle = Puzzle(input, ::part1, ::part2)
    puzzle.part1()
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
    val vertices = input.keys.toList()
    vertices.indices.forEach { i ->
        val vi = vertices[i]
        (i + 1..<vertices.size).forEach { j ->
            val vj = vertices[i]
            (j + 1..<vertices.size).forEach { k ->
                val vk = vertices[i]
                val reachable = mutableSetOf<String>()
                val toGo = Queue<String>()
                toGo.offer(vertices.first())
                while (toGo.isNotEmpty()) {
                    val v = toGo.poll()
                    val boring = v != vi && v != vj && v != vk
                    if (v !in reachable) {
                        reachable += v
                        val candidates = input[v]!!
                        candidates.forEach {
                            if (boring || it != vi && it != vj && it != vk)
                                if (it !in reachable) toGo.offer(it)
                        }
                    }
                }
                if (reachable.size < input.size) return reachable.size * (input.size - reachable.size)
            }
        }
        "$vi ($i of ${vertices.size})".logged("i")
    }
    TODO()
}


fun part2(input: Input): Any? = null // TODO("part 2 with ${input.toString().length} data")


