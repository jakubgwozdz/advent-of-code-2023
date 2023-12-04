package aoc2023.day4

import aoc2023.Puzzle
import aoc2023.getDay
import aoc2023.readAndParse

fun main() {
    val input = readAndParse("local/${getDay {}}_input.txt", ::parse)
    val puzzle = Puzzle(input, ::part1, ::part2)
    puzzle.part1()
    puzzle.part2()
}

data class Card(val winning: Set<Int>, val mine: Set<Int>)
typealias Input = List<Card>

fun parse(inputStr: String) = inputStr.lines().filterNot { it.isBlank() }.map { line ->
    fun String.ints() = split(" ").filterNot { it.isBlank() }.map { it.toInt() }
    val (_, winStr, myStr) = line.split(":", "|")
    Card(winStr.ints().toSet(), myStr.ints().toSet())
}

private fun Card.matchCount() = mine.intersect(winning).size

fun part1(input: Input) = input.sumOf { card -> card.matchCount().let { 1.shl(it - 1) } }

fun part2(input: Input): Int {
    val counts = IntArray(input.size) { 1 }
    input.forEachIndexed { index, card ->
        repeat(card.matchCount()) { counts[index + 1 + it] += counts[index] }
    }
    return counts.sum()
}
