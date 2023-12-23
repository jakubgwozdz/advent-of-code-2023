package aoc2023.day23

import aoc2023.Puzzle
import aoc2023.expect
import aoc2023.getDay
import aoc2023.readAndParse

fun main() {
    val input = readAndParse("local/${getDay {}}_input.txt", ::parse)
    val puzzle = Puzzle(input, ::part1, ::part2)
    puzzle.part1().expect(2354)
    puzzle.part2().expect(6686)
}

typealias Input = List<String>

fun parse(inputStr: String): Input = inputStr.lines().filterNot { it.isBlank() }

typealias Pos = Pair<Int, Int>

val Pos.r get() = first
val Pos.c get() = second

val Pos.N: Pos get() = r - 1 to c
val Pos.S: Pos get() = r + 1 to c
val Pos.W: Pos get() = r to c - 1
val Pos.E: Pos get() = r to c + 1

fun Pos.adjacents() = listOf(N, W, S, E)

data class State(val pos: Pos, val visited: Set<Pos> = setOf(pos), val length: Int = 0)

fun State.go(p: Pos, l: Int) = State(p, visited + p, length + l)

fun part1(input: Input): Int = calc(input) { pos, ch ->
    when (ch) {
        '.' -> pos.adjacents()
        '>' -> listOf(pos.E)
        'v' -> listOf(pos.S)
        '^' -> listOf(pos.N)
        '<' -> listOf(pos.W)
        else -> error("$pos: $ch")
    }
}

fun part2(input: Input): Int = calc(input) { pos, _ -> pos.adjacents() }

private fun calc(input: Input, movesOp: (Pos, Char) -> List<Pos>): Int {
    val graph0 = input.asSequence()
        .flatMapIndexed { r, l -> l.mapIndexed { c, ch -> Pos(r, c) to ch } }
        .filter { (_, ch) -> ch != '#' }
        .associate { (pos, ch) ->
            pos to movesOp(pos, ch).filter { (r, c) -> r in input.indices && input[r][c] != '#' }
        }

    graph0.keys.buildImage(input.size, input[0].length)
    val graph1 = graph0.filterValues { it.size != 2 }.keys.associateWith(graph0::simplePath)

    val start = Pos(0, 1)
    val end = input.lastIndex.let { r -> r to input[r].lastIndex - 1 }

    return DeepRecursiveFunction<State, Int> { state ->
        if (state.pos == end) state.length
        else graph1[state.pos]!!.filterNot { it.first in state.visited }
            .maxOfOrNull { callRecursive(state.go(it.first, it.second)) } ?: 0
    }(State(start))
}

private fun Map<Pos, List<Pos>>.simplePath(v: Pos) = this[v]!!.map { p ->
    var l = 1
    var p0 = v
    var p1 = p
    while (this[p1]!!.size == 2) {
        val n = this[p1]!!.single { it != p0 }
        p0 = p1
        p1 = n
        l++
    }
    p1 to l
}
