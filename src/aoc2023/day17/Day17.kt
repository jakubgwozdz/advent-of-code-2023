package aoc2023.day17

import aoc2023.PriorityQueue
import aoc2023.Puzzle
import aoc2023.day17.Dir.E
import aoc2023.day17.Dir.N
import aoc2023.day17.Dir.S
import aoc2023.day17.Dir.W
import aoc2023.expect
import aoc2023.getDay
import aoc2023.logged
import aoc2023.readAndParse

fun main() {
    val input = readAndParse("local/${getDay {}}_input.txt", ::parse)
    val puzzle = Puzzle(input, ::part1, ::part2)
    puzzle.part1().expect(814)
    puzzle.part2().expect(974)
}

typealias Input = List<String>

fun parse(inputStr: String): Input {
    return inputStr.lines().filterNot { it.isBlank() }
}

enum class Dir { N, E, S, W }
data class Pos(val r: Int, val c: Int)

operator fun Pos.plus(d: Dir): Pos = when (d) {
    N -> Pos(r - 1, c)
    E -> Pos(r, c + 1)
    S -> Pos(r + 1, c)
    W -> Pos(r, c - 1)
}

operator fun Pos.minus(o: Pos): Dir = when {
    o + N == this -> N
    o + E == this -> E
    o + S == this -> S
    o + W == this -> W
    else -> TODO()
}

operator fun Dir.unaryMinus() = when (this) {
    N -> S
    E -> W
    S -> N
    W -> E
}

data class State(val pos: Pos, val lastMoves: Pair<Dir, Int>)
data class StateWithData(val state: State, val visited: Set<Pos>, val cost: Int)

fun part1(input: Input): Any {

    fun StateWithData.possibleMoves(): List<StateWithData> = buildList {
        val (lastDir, count) = state.lastMoves
        val back = -lastDir
        val last3dir = if (count >= 3) lastDir else null

        Dir.entries.forEach { dir ->
            val newPos = state.pos + dir
            if (dir != back && dir != last3dir &&
                newPos !in visited &&
                newPos.r in input.indices && newPos.c in input[newPos.r].indices
            ) {
                val newLastMoves = dir to if (dir == lastDir) count + 1 else 1
                val newState = State(newPos, newLastMoves)
                add(StateWithData(newState, visited + newPos, cost + input[newPos.r][newPos.c].digitToInt()))
            }
        }
    }

    val start = StateWithData(State(Pos(0, 0), E to 0), emptySet(), 0)
    val end = Pos(input.lastIndex, input.last().lastIndex)
    val toGo = PriorityQueue<StateWithData> { s1, s2 -> s1.cost - s2.cost }
        .apply { offer(start) }
    val besties = mutableMapOf<State, Int>()
    var min = Int.MAX_VALUE
    while (toGo.isNotEmpty()) {
        val stateWithCost = toGo.poll()
        if ((besties[stateWithCost.state] ?: Int.MAX_VALUE) > stateWithCost.cost) {
            besties[stateWithCost.state] = stateWithCost.cost
            if (stateWithCost.state.pos == end) min =
                min.coerceAtMost(stateWithCost.cost.logged(stateWithCost.state.lastMoves))
            stateWithCost.possibleMoves().forEach(toGo::offer)
        }
    }
    return min
}

fun part2(input: Input): Any {

    fun StateWithData.possibleMoves(): List<StateWithData> = if (state.lastMoves.second in (1..3)) {
        buildList {
            val (lastDir, count) = state.lastMoves
            val newPos = state.pos + lastDir
            if (newPos.r in input.indices && newPos.c in input[newPos.r].indices) {
                val newLastMoves = lastDir to count + 1
                val newState = State(newPos, newLastMoves)
                add(StateWithData(newState, visited + newPos, cost + input[newPos.r][newPos.c].digitToInt()))
            }
        }
    } else buildList {
        val (lastDir, count) = state.lastMoves
        val back = -lastDir
        val last3dir = if (count >= 10) lastDir else null

        Dir.entries.forEach { dir ->
            val newPos = state.pos + dir
            if (dir != back && dir != last3dir &&
                newPos !in visited &&
                newPos.r in input.indices && newPos.c in input[newPos.r].indices
            ) {
                val newLastMoves = dir to if (dir == lastDir) count + 1 else 1
                val newState = State(newPos, newLastMoves)
                add(StateWithData(newState, visited + newPos, cost + input[newPos.r][newPos.c].digitToInt()))
            }
        }
    }

    val start = StateWithData(State(Pos(0, 0), E to 0), emptySet(), 0)
    val end = Pos(input.lastIndex, input.last().lastIndex)
    val toGo = PriorityQueue<StateWithData> { s1, s2 -> s1.cost - s2.cost }
        .apply { offer(start) }
    val besties = mutableMapOf<State, Int>()
    var min = Int.MAX_VALUE
    while (toGo.isNotEmpty()) {
        val stateWithCost = toGo.poll()
        if ((besties[stateWithCost.state] ?: Int.MAX_VALUE) > stateWithCost.cost) {
            besties[stateWithCost.state] = stateWithCost.cost
            if (stateWithCost.state.pos == end) min =
                min.coerceAtMost(stateWithCost.cost.logged(stateWithCost.state.lastMoves))
            stateWithCost.possibleMoves().forEach(toGo::offer)
        }
    }
    return min
}
