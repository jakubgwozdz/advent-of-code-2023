package aoc2023.day4

import aoc2023.Puzzle
import aoc2023.getDay
import aoc2023.ints
import aoc2023.readAndParse

fun main() {
    val input = readAndParse("local/${getDay {}}_input.txt", ::parse)
    val puzzle = Puzzle(input, ::part1, ::part2)
    puzzle.part1()
    puzzle.part2()
}

fun parse(inputStr: String) = inputStr.lines().filterNot { it.isBlank() }.map { line ->
    val (_, winStr, myStr) = line.split(":", "|")
    Card(winStr.ints().toSet(), myStr.ints().toSet())
}

data class Card(val winning: Set<Int>, val mine: Set<Int>)
typealias Input = List<Card>

private fun Card.matchCount() = mine.intersect(winning).size

fun part1(input: Input) = input
    .map { card -> card.matchCount() }
    .sumOf { if (it >= 0) 1.shl(it - 1) else 0 }

data class Acc(
    val sum: Int,
    val state: List<Int>, // *copies* won so far (excl. original)
)

fun part2(input: Input) = input
    .map { card -> card.matchCount() }
    .fold(Acc(0, emptyList()), ::part2round)
    .sum

fun part2round(acc: Acc, matches: Int): Acc {
    val count = (acc.state.firstOrNull() ?: 0) + 1
    val tail = acc.state.drop(1)
    val newState = List(tail.size.coerceAtLeast(matches)) {
        val prev = if (it < tail.size) tail[it] else 0
        val new = if (it < matches) count else 0
        prev + new
    }
    return Acc(acc.sum + count, newState)
}
