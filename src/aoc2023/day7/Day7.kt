package aoc2023.day7

import aoc2023.Puzzle
import aoc2023.day7.Type.*
import aoc2023.getDay
import aoc2023.readAndParse

fun main() {
    val input = readAndParse("local/${getDay {}}_input.txt", ::parse)
    val puzzle = Puzzle(input, ::part1, ::part2)
    puzzle.part1()
    puzzle.part2()
}

typealias Input = List<Pair<String, Int>>

fun parse(inputStr: String): Input =
    inputStr.lines().filterNot { it.isBlank() }.map { it.split(" ") }.map { (a, b) -> a to b.toInt() }

val order = "_j23456789TJQKA"

enum class Type { FiveOfA, FourOfA, FullHouse, ThreeOfA, TwoPair, OnePair, HighCard }
data class Hand(val type: Type, val kinds: List<Int>) : Comparable<Hand> {
    override fun compareTo(other: Hand): Int {
        type.ordinal.compareTo(other.type.ordinal).let { if (it != 0) return -it }
        kinds.zip(other.kinds).forEach { (a, b) -> a.compareTo(b).let { if (it != 0) return it } }
        return 0
    }

    fun cards() = kinds.joinToString("") { order[it].toString() }
}

fun String.toHand(): Hand {
    val counts = groupBy { it }.map { (_, v) -> v.size }
    val type = when (counts.size to counts.max()) {
        1 to 5 -> FiveOfA
        2 to 4 -> FourOfA
        2 to 3 -> FullHouse
        3 to 3 -> ThreeOfA
        3 to 2 -> TwoPair
        4 to 2 -> OnePair
        5 to 1 -> HighCard
        else -> error("`$this` parsed to $counts")
    }
    val kinds = map { order.indexOf(it) }
    return Hand(type, kinds)
}

fun String.toHandWithJokers(): Hand {
    if ('J' !in this) return toHand()
    val cards = replace('J', 'j')
    val bestType = "234567890TQKA".maxOf { cards.replace('j', it).toHand() }.type
    val kinds = cards.map { order.indexOf(it) }
    return Hand(bestType, kinds)
}

fun part1(input: Input) = input
    .map { (str, bid) -> str.toHand() to bid }
    .sortedBy { it.first }
    .mapIndexed { index, (hand, bid) -> Triple(index + 1, hand, bid) }
    .sumOf { (rank, hand, bid) -> rank * bid }

fun part2(input: Input) = input
    .map { (str, bid) -> str.toHandWithJokers() to bid }
    .sortedBy { it.first }
    .mapIndexed { index, (hand, bid) -> Triple(index + 1, hand, bid) }
    .sumOf { (rank, hand, bid) -> rank * bid }

