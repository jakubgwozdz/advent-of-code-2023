package aoc2023.day10

import aoc2023.Puzzle
import aoc2023.day10.Move.*
import aoc2023.getDay
import aoc2023.logged
import aoc2023.readAndParse
import java.util.Comparator
import kotlin.math.absoluteValue

fun main() {
    val input = readAndParse("local/${getDay {}}_input.txt", ::parse)
    val puzzle = Puzzle(input, ::part1, ::part2)
    puzzle.part1()
    puzzle.part2()
}

typealias Pos = Pair<Int, Int>

data class Input(val map: List<String>, val start: Pos)

fun parse(inputStr: String, cheat: Char = '|'): Input {
    val lines = inputStr.lines()
    val start = lines.indexOfFirst { it.contains('S') }.let { r -> r to lines[r].indexOf('S') }
    return Input(lines.map { it.replace('S', cheat) }, start)
}

fun findBoth(pos: Pos, input: Input): List<Pos> = pos.let { (r, c) ->
    when (input.map[r][c]) {
//        'S' -> listOf(r + 1 to c, r - 1 to c)
        '|' -> listOf(r + 1 to c, r - 1 to c)
        '-' -> listOf(r to c - 1, r to c + 1)
        'L' -> listOf(r - 1 to c, r to c + 1)
        'J' -> listOf(r - 1 to c, r to c - 1)
        'F' -> listOf(r + 1 to c, r to c + 1)
        '7' -> listOf(r + 1 to c, r to c - 1)
        else -> error("${input.map[r][c]} at $pos")
    }
}

fun part1(input: Input): Int = // 7086
    input.loop().count() / 2

private fun Input.loop(): Sequence<Pos> {
    val loop = generateSequence(start to start) { (prevPos, pos) ->
        val newPos = findBoth(pos, this).first { it != prevPos }
        if (newPos == start) null
        else pos to newPos
    }
    return loop.map { it.second }
}

enum class Move { N, S, W, E }

fun List<Move>.countInsides(): Int {
    var sum = 0
    var dx = 0
    forEach { move ->
        when (move) {
            N -> sum -= dx
            S -> sum += dx
            W -> dx--
            E -> dx++
        }
    }
    return sum.absoluteValue - (size/2)+1
}

fun part2(input: Input): Int {
    val loop = input.loop().toList()
    val moves = (loop + loop.first()).zipWithNext(::move)
    return moves.countInsides()
}

private fun List<Pos>.normalize(): List<Pos> {
    val topLeft = minWith(Comparator.comparingInt(Pos::first).thenComparingInt(Pos::second))
    val index = indexOf(topLeft)
    val indexOfNext = (index + 1) % size
    return when (move(this[index], this[indexOfNext])) {
        E -> drop(index) + take(index)
        S -> (drop(index) + take(index)).reversed().let { listOf(it.last()) + it.dropLast(1) }
        else -> error("wtf")
    }
}

private fun move(pos1: Pos, pos2: Pos): Move {
    val (r1, c1) = pos1
    val (r2, c2) = pos2
    return when (r2 - r1 to c2 - c1) {
        -1 to 0 -> N
        1 to 0 -> S
        0 to -1 -> W
        0 to 1 -> E
        else -> error("$pos1 moves to $pos2?")
    }
}


