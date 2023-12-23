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

fun part2(input: Input): Int = calc(input)

typealias Visited = LongArray

fun Visited.add(id: Int) {
    val i = id / 64
    val o = id % 64
    this[i] = this[i] or (1L shl o)
}

fun Visited.remove(id: Int) {
    val i = id / 64
    val o = id % 64
    this[i] = this[i] and (1L shl o).inv()
}

operator fun Visited.contains(id: Int): Boolean {
    val i = id / 64
    val o = id % 64
    return this[i] and (1L shl o) != 0L
}

private fun calc(input: Input, movesOp: (Pos, Char) -> List<Pos> = { pos, _ -> pos.adjacents() }): Int {
    val graph0 = input.asSequence()
        .flatMapIndexed { r, l -> l.mapIndexed { c, ch -> Pos(r, c) to ch } }
        .filter { (_, ch) -> ch != '#' }
        .associate { (pos, ch) ->
            pos to movesOp(pos, ch).filter { (r, c) -> r in input.indices && input[r][c] != '#' }
        }

    val graph1 = graph0.filterValues { it.size != 2 }.keys.associateWith(graph0::simplePaths)
//    graph1.tgf()
    val ids = graph1.keys.withIndex().associate { (id, pos) -> pos to id }
    val graph2 = graph1
        .map { (p, v) -> ids[p]!! to v.map { (p2, l) -> ids[p2]!! to l } }
        .sortedBy { it.first }
        .map { it.second }

    val start = ids.entries.single { it.key.r == 0 }.value
    val end = ids.entries.single { it.key.r == input.lastIndex }.value

    val visited = Visited(3) // triple to accommodate 154 bits for part 1
    var current = start
    var length = 0

    fun test(): Int = if (current == end) length
    else graph2[current]
        .filterNot { (id) -> id in visited }
        .maxOfOrNull { (id, l) ->
            val oldLength = length
            val oldId = current
            length += l
            current = id
            visited.add(id)
            test().also {
                visited.remove(id)
                current = oldId
                length = oldLength
            }
        } ?: 0
    return test()
}

private fun Map<Pos, List<Pos>>.simplePaths(v: Pos) = this[v]!!.map { p ->
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
