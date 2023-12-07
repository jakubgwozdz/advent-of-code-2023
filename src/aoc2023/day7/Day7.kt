package aoc2023.day7

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

// 247047014 too high
typealias Input = List<Pair<Hand, Int>>

fun parse(inputStr: String): Input =
    inputStr.lines().filterNot { it.isBlank() }.map { it.split(" ") }.map { (a, b) -> a.toHand() to b.toInt() }

val order = "**23456789TJQKA"
private fun Int.of(kind: Int) = order[kind].toString().repeat(this)

sealed interface Hand : Comparable<Hand>
data class FiveOfA(val kind: Int) : Hand {
    override fun compareTo(other: Hand) = when (other) {
        is FiveOfA -> kind.compareTo(other.kind)
        else -> 1
    }

    override fun toString(): String = 5.of(kind)
}

data class FourOfA(val kind: Int, val rest: Int) : Hand {
    override fun compareTo(other: Hand) = when (other) {
        is FiveOfA -> -1
        is FourOfA -> compareValuesBy(this, other, FourOfA::kind, FourOfA::rest)
        else -> 1
    }

    override fun toString(): String = "${4.of(kind)} ${1.of(rest)}"
}

data class FullHouse(val triple: Int, val pair: Int) : Hand {
    override fun compareTo(other: Hand) = when (other) {
        is FiveOfA -> -1
        is FourOfA -> -1
        is FullHouse -> compareValuesBy(this, other, FullHouse::triple, FullHouse::pair)
        else -> 1
    }
    override fun toString(): String = "${3.of(triple)} ${2.of(pair)}"
}

data class ThreeOfA(val kind: Int, val rest1: Int, val rest2: Int) : Hand {
    override fun compareTo(other: Hand) = when (other) {
        is FiveOfA -> -1
        is FourOfA -> -1
        is FullHouse -> -1
        is ThreeOfA -> compareValuesBy(this, other, ThreeOfA::kind, ThreeOfA::rest1, ThreeOfA::rest2)
        else -> 1
    }
    override fun toString(): String = "${3.of(kind)} ${1.of(rest1)} ${1.of(rest2)}"
}

data class TwoPair(val pair1: Int, val pair2: Int, val rest: Int) : Hand {
    override fun compareTo(other: Hand) = when (other) {
        is FiveOfA -> -1
        is FourOfA -> -1
        is FullHouse -> -1
        is ThreeOfA -> -1
        is TwoPair -> compareValuesBy(this, other, TwoPair::pair1, TwoPair::pair2, TwoPair::rest)
        else -> 1
    }
    override fun toString(): String = "${2.of(pair1)} ${2.of(pair2)} ${1.of(rest)}"
}

data class OnePair(val kind: Int, val rest1: Int, val rest2: Int, val rest3: Int) : Hand {
    override fun compareTo(other: Hand) = when (other) {
        is FiveOfA -> -1
        is FourOfA -> -1
        is FullHouse -> -1
        is ThreeOfA -> -1
        is TwoPair -> -1
        is OnePair -> compareValuesBy(this, other, OnePair::kind, OnePair::rest1, OnePair::rest2, OnePair::rest3)
        else -> 1
    }
    override fun toString(): String = "${2.of(kind)} ${1.of(rest1)} ${1.of(rest2)} ${1.of(rest3)}"
}

data class HighCard(val k1: Int, val k2: Int, val k3: Int, val k4: Int, val k5: Int) : Hand {
    override fun compareTo(oth: Hand) = when (oth) {
        is HighCard -> compareValuesBy(this, oth, HighCard::k1, HighCard::k2, HighCard::k3, HighCard::k4, HighCard::k5)
        else -> -1
    }
    override fun toString(): String = "${1.of(k1)} ${1.of(k2)} ${1.of(k3)} ${1.of(k4)} ${1.of(k5)}"
}

fun String.toHand(): Hand {
    val g = map { order.indexOf(it) }.onEach { check(it in 2..14) { "$it on `$this`" } }
        .groupBy { it }.mapValues { (_, v) -> v.size }.toList()
        .sortedWith(compareBy(Pair<Int, Int>::second, Pair<Int, Int>::first)).reversed()
    check(g.sumOf { it.second } == 5) { "`$this` parsed to $g and doesn't sum up" }
    g.windowed(2)
        .forEach { (a, b) -> check(a.second > b.second || a.second == b.second && a.first > b.first) { "failed sorting $g on $a vs $b" } }

    return when {
        g.size == 1 -> FiveOfA(g[0].first)
        g.size == 2 && g[0].second == 4 -> FourOfA(g[0].first, g[1].first)
        g.size == 2 && g[0].second == 3 -> FullHouse(g[0].first, g[1].first)
        g.size == 3 && g[0].second == 3 -> ThreeOfA(g[0].first, g[1].first, g[2].first)
        g.size == 3 && g[0].second == 2 -> TwoPair(g[0].first, g[1].first, g[2].first)
        g.size == 4 -> g.map { it.first }.let { (a, b, c, d) -> OnePair(a, b, c, d) }
        g.size == 5 -> g.map { it.first }.let { (a, b, c, d, e) -> HighCard(a, b, c, d, e) }
        else -> error("what to do with `$this` that parsed to $g ???")
    }.logged { this }
}

fun part1(input: Input) = input.sortedBy { it.first }
    .mapIndexed { index, (hand, bid) -> Triple(index + 1, hand, bid).logged() }
    .sumOf { (rank, hand, bid) -> rank * bid }

fun part2(input: Input): Any? = TODO("part 2 with ${input.toString().length} data")


