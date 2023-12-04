package aoc2023.day2

import aoc2023.Puzzle
import aoc2023.getDay
import aoc2023.readAndParse

fun main() {
    val input = readAndParse("local/${getDay {}}_input.txt", ::parse)
    val puzzle = Puzzle(input, ::part1, ::part2)
    puzzle.part1()
    puzzle.part2()
}

fun part1(input: Input) = input
    .filter(Game::isLegit121314)
    .sumOf { it.id }

private fun Game.isLegit121314() = sets.all { it.red <= 12 && it.green <= 13 && it.blue <= 14 }

fun part2(input: Input) = input.sumOf { game ->
    val r = game.sets.maxOfOrNull { it.red } ?: 0
    val g = game.sets.maxOfOrNull { it.green } ?: 0
    val b = game.sets.maxOfOrNull { it.blue } ?: 0
    r * g * b
}
