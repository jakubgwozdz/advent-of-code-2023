package aoc2023.day17

import aoc2023.PriorityQueue
import aoc2023.Puzzle
import aoc2023.Queue
import aoc2023.day17.Dir.E
import aoc2023.day17.Dir.N
import aoc2023.day17.Dir.S
import aoc2023.day17.Dir.W
import aoc2023.expect
import aoc2023.getDay
import aoc2023.readAndParse

fun main() {
    val input = readAndParse("local/${getDay {}}_input.txt", ::parse)
    val puzzle = Puzzle(input, ::part1, ::part2)
    puzzle.part1().expect(814)
    puzzle.part2().expect(974)
}

typealias Input = List<String>

fun parse(inputStr: String): Input = inputStr.lines().filterNot { it.isBlank() }

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

fun part1(input: Input) = input.solve(1, 3)
fun part2(input: Input) = input.solve(4, 10)

fun Input.solve(minMoves: Int, maxMoves: Int): Int? {
    val queue =
        PriorityQueue<StateWithData>(compareBy<StateWithData> { it.cost + (it.state.pos.r + it.state.pos.c) * 1 })
    val isEnd: (StateWithData) -> Boolean = { it.state.pos.r == lastIndex && it.state.pos.c == last().lastIndex }
    val moves: (StateWithData) -> List<StateWithData> = { s ->
        if (s.state.lastMoves.second in (1..<minMoves)) {
            buildList {
                val (lastDir, count) = s.state.lastMoves
                val newPos = s.state.pos + lastDir
                if (newPos.r in this@solve.indices && newPos.c in this@solve[newPos.r].indices) {
                    val newLastMoves = lastDir to count + 1
                    val newState = State(newPos, newLastMoves)
                    add(
                        StateWithData(
                            newState,
                            s.visited + newPos,
                            s.cost + this@solve[newPos.r][newPos.c].digitToInt()
                        )
                    )
                }
            }
        } else buildList {
            val (lastDir, count) = s.state.lastMoves
            val back = -lastDir
            val last3dir = if (count >= maxMoves) lastDir else null

            Dir.entries.forEach { dir ->
                val newPos = s.state.pos + dir
                if (dir != back && dir != last3dir &&
                    newPos !in s.visited &&
                    newPos.r in this@solve.indices && newPos.c in this@solve[newPos.r].indices
                ) {
                    val newLastMoves = dir to if (dir == lastDir) count + 1 else 1
                    val newState = State(newPos, newLastMoves)
                    add(
                        StateWithData(
                            newState,
                            s.visited + newPos,
                            s.cost + this@solve[newPos.r][newPos.c].digitToInt()
                        )
                    )
                }
            }
        }
    }
    val besties = mutableMapOf<State, Int>()
    val isBetter: (StateWithData) -> Boolean = {
        besties[it.state]
            .let { b -> b == null || b > it.cost }
            .also { better -> if (better) besties[it.state] = it.cost }
    }

    return search(
        StateWithData(State(Pos(0, 0), E to 0), emptySet(), 0),
        moves,
        { it.cost },
        isBetter,
        isEnd,
        queue,
    )
}

fun <T : Any, R : Comparable<R>> search(
    startState: T,
    moves: (T) -> List<T>,
    valueOp: (T) -> R,
    worth: (T) -> Boolean,
    isEnd: (T) -> Boolean,
    queue: Queue<T> = Queue(),
): R? {
    val toGo = queue.apply { offer(startState) }
    var min: R? = null
    while (toGo.isNotEmpty()) {
        val next = toGo.poll()
        val value = valueOp(next)
        if (worth(next)) {
            if (isEnd(next)) {
                if (min == null || value < min) min = value
            } else {
                moves(next).forEach(toGo::offer)
            }
        }
    }
    return min
}
