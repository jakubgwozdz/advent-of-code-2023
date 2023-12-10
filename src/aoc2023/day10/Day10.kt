package aoc2023.day10

import aoc2023.Puzzle
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
typealias Input = List<String>

fun parse(inputStr: String): Input {
    return inputStr.lines().filterNot { it.isBlank() }
}

typealias Pos = Pair<Int, Int>

fun findBoth(pos: Pos, input: Input): List<Pos> = pos.let { (r, c) ->
    when (input[r][c]) {
        'S' -> listOf(r + 1 to c, r - 1 to c)
        '|' -> listOf(r + 1 to c, r - 1 to c)
        '-' -> listOf(r to c - 1, r to c + 1)
        'L' -> listOf(r - 1 to c, r to c + 1)
        'J' -> listOf(r - 1 to c, r to c - 1)
        'F' -> listOf(r + 1 to c, r to c + 1)
        '7' -> listOf(r + 1 to c, r to c - 1)
        else -> error("${input[r][c]} at $pos")
    }
}

fun part1(input: Input): Int {
    val start:Pos = input.indexOfFirst { it.contains('S') }.let { r -> r to input[r].indexOf('S') }
        .logged { "Pos" }
    val last = generateSequence((start to start) to 0) { (pos2, steps) ->
        val (prevPos, pos) = pos2
        val newPos = findBoth(pos, input).first { it != prevPos }
        if (newPos == start) null
        else ((pos to newPos) to steps + 1).logged()
    }.last()
        .logged()
    return (last.second + 1) / 2
}

fun part2(input: Input): Any? = null


