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
data class Pos(val r: Int, val c: Int) {
    override fun toString(): String = "($r,$c)"
}

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

operator fun Input.contains(p: Pos) = p.r in this.indices && p.c in this[p.r].indices
private operator fun Input.get(p: Pos) = this[p.r][p.c].digitToInt()

typealias Key = Pair<Pos, Dir?>

data class State(val pos: Pos, val lastMove: Dir?, val cost: Int) : Comparable<State> {
    override fun compareTo(other: State): Int = this.cost.compareTo(other.cost)
    val key: Key = pos to lastMove
}

fun part1(input: Input) = input.solve(1, 3)
fun part2(input: Input) = input.solve(4, 10)

fun Input.solve(minMoves: Int, maxMoves: Int): Int? {
    val queue = PriorityQueue(compareBy<State> { it.cost })
    val isEnd: (State) -> Boolean = { it.pos.r == lastIndex && it.pos.c == last().lastIndex }
    val moves: (State) -> List<State> = { s ->
        buildList {
            val dirs = when (s.lastMove) {
                E, W -> listOf(S, N)
                N, S -> listOf(E, W)
                null -> listOf(S, E)
            }
            dirs.forEach { dir ->
                var nextPos = s.pos + dir
                var i = 0
                var nextCost = s.cost
                while ((i < maxMoves) && (nextPos in this@solve)) {
                    i++
                    nextCost += this@solve[nextPos]
                    if (i >= minMoves) add(State(nextPos, dir, nextCost))
                    nextPos += dir
                }
            }
        }
    }
    val besties = mutableMapOf<Key, Int>()
    val isBetter: (State) -> Boolean = {
        besties[it.key]
            .let { b -> b == null || b > it.cost }
            .also { better -> if (better) besties[it.key] = it.cost }
    }
    return search(
        State(Pos(0, 0), null, 0),
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
        if (worth(next) && (min == null || value < min)) {
            if (isEnd(next)) min = value else moves(next).forEach(toGo::offer)
        }
    }
    return min
}
