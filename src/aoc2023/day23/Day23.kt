package aoc2023.day23

import aoc2023.Puzzle
import aoc2023.expect
import aoc2023.getDay
import aoc2023.logged
import aoc2023.readAndParse

fun main() {
    val input = readAndParse("local/${getDay {}}_input.txt", ::parse)
    val puzzle = Puzzle(input, ::part1, ::part2)
    puzzle.part1().expect(2354)
    puzzle.part2()
}

typealias Input = List<String>

fun parse(inputStr: String): Input {
    return inputStr.lines().filterNot { it.isBlank() }
}

typealias Pos = Pair<Int, Int>

val Pos.r get() = first
val Pos.c get() = second

val Pos.N: Pos get() = r - 1 to c
val Pos.S: Pos get() = r + 1 to c
val Pos.W: Pos get() = r to c - 1
val Pos.E: Pos get() = r to c + 1

fun Pos.adjacents() = listOf(N, W, S, E)

data class State(val pos: Pos, val visited: Set<Pos> = setOf(pos))

fun State.go(p: Pos) = State(p, visited + p)

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

// 6163 too low
fun part2(input: Input): Int = calc(input) { pos, _ -> pos.adjacents() }

private fun calc(input: Input, movesOp: (Pos, Char) -> List<Pos>): Int {
    val graph = input.asSequence()
        .flatMapIndexed { r, l -> l.mapIndexed { c, ch -> Pos(r, c) to ch } }
        .filter { (_, ch) -> ch != '#' }
        .associate { (pos, ch) ->
            pos to movesOp(pos, ch).filter { (r, c) -> r in input.indices && input[r][c] != '#' }
        }
    val start = Pos(0, 1)
        .logged("start")
    val end = input.lastIndex.let { r -> r to input[r].lastIndex - 1 }
        .logged("end")

    var max = 0
    return DeepRecursiveFunction<State, Int> { state ->
        if (state.pos == end) state.visited.size.logged(max).also { max = max.coerceAtLeast(it) }
        else graph[state.pos]!!.filterNot { it in state.visited }.maxOfOrNull { callRecursive(state.go(it)) } ?: 0
    }(State(start)) - 1
}
