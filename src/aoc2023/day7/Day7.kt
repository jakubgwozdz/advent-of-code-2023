package aoc2023.day7

import aoc2023.Puzzle
import aoc2023.getDay
import aoc2023.readAndParse

fun main() {
    val input = readAndParse("local/${getDay {}}_input.txt", ::parse)
    val puzzle = Puzzle(input, ::part1, ::part2)
    puzzle.part1()
    puzzle.part2()
}

fun parse(inputStr: String) = inputStr.lines().filterNot { it.isBlank() }
    .map { it.split(" ") }
    .map { (a, b) -> a to b.toInt() }

const val order = "_j23456789TJQKA_"

data class Hand(val type: Int, val cards: String) : Comparable<Hand> {
    val value = cards.fold(type) { acc, c -> acc * 16 + order.indexOf(c) }
    override fun compareTo(other: Hand): Int = value.compareTo(other.value)
    override fun toString() = value.toString(16).padStart(8,'0')
}

fun normalHand(cards: String): Hand {
    val counts = cards.groupBy { it }.map { (_, v) -> v.size }.sortedDescending()
    val type = (counts[0] * 16 + counts.getOrElse(1) { 0 })*16
    return Hand(type, cards)
}

fun jokeHand(cards: String): Hand {
    if ('J' !in cards) return normalHand(cards)
    val bestType = "234567890TQKA".maxOf { normalHand(cards.replace('J', it)) }.type
    return Hand(bestType, cards.replace('J', 'j'))
}

typealias Input = List<Pair<String, Int>>

fun Input.solve(op: (String) -> Hand) = map { op(it.first) to it.second }
    .sortedBy { it.first }
    .mapIndexed { index, (_, bid) -> (index + 1) * bid }
    .sum()

fun part1(input: Input) = input.solve(::normalHand)
fun part2(input: Input) = input.solve(::jokeHand)
