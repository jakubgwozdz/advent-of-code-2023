package aoc2023.day10

import aoc2023.Puzzle
import aoc2023.day10.Move.E
import aoc2023.day10.Move.N
import aoc2023.day10.Move.S
import aoc2023.day10.Move.W
import aoc2023.getDay
import aoc2023.readAndParse
import kotlin.math.absoluteValue

fun main() {
    val input = readAndParse("local/${getDay {}}_input.txt", ::parse)
    val puzzle = Puzzle(input, ::part1, ::part2)
    puzzle.part1()
    puzzle.part2()
}

typealias Pos = Pair<Int, Int>

operator fun Pos.plus(move: Move): Pos = when (move) {
    N -> first - 1 to second
    S -> first + 1 to second
    W -> first to second - 1
    E -> first to second + 1
}

enum class Move { N, S, W, E }
typealias Input = List<Move>

operator fun List<String>.get(pos: Pos): Char = getOrNull(pos.first)?.getOrNull(pos.second) ?: '.'

fun parse(inputStr: String): Input {
    val lines = inputStr.lines()
    val start: Pos = lines.indices.asSequence()
        .flatMap { row -> lines[row].indices.map { row to it } }
        .first { pos -> lines[pos] == 'S' }
    val firstMove = when {
        lines[start + N] in "7|F" -> N
        lines[start + E] in "7-J" -> E
        else -> S
    }
    return generateSequence(start to firstMove) { (pos, move) ->
        val nextPos = pos + move
        if (nextPos != start) nextPos to when (lines[nextPos]) {
            '|' -> if (move == N) N else S
            '-' -> if (move == W) W else E
            'L' -> if (move == S) E else N
            'J' -> if (move == S) W else N
            'F' -> if (move == N) E else S
            '7' -> if (move == N) W else S
            else -> error("$nextPos?")
        }
        else null
    }
        .map { it.second }
        .toList()
}

fun part1(input: Input) = input.count() / 2
fun part2(input: Input) = input.fold(0 to 0) { (sum, d), move ->
    when (move) {
        N -> sum to d + 1
        S -> sum to d - 1
        W -> sum - d to d
        E -> sum + d to d
    }
}.first.absoluteValue - (input.size / 2) + 1
