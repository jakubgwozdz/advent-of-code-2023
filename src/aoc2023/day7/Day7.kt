package aoc2023.day7

import aoc2023.Puzzle
import aoc2023.day7.Type.*
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
enum class Type(val power: Int = 0) { FiveOfA, FourOfA, FullHouse, ThreeOfA, TwoPair, OnePair, HighCard }

data class Hand(val type: Type, val kinds: List<Int>) : Comparable<Hand> {
    override fun compareTo(other: Hand): Int {
        type.ordinal.compareTo(other.type.ordinal).let { if (it != 0) return -it }
        kinds.zip(other.kinds).forEach { (a, b) -> a.compareTo(b).let { if (it != 0) return it } }
        return 0
    }

    override fun toString(): String = toString2()//

    fun toString1() = type.name + " " + kinds.joinToString("") { 1.of(it) }

    fun toString2(): String = when (type) {
        FiveOfA -> kinds.joinToString(" ") { 5.of(it) }
        FourOfA -> kinds.let { (a, b) -> "${4.of(a)} ${1.of(b)}" }
        FullHouse -> kinds.let { (a, b) -> "${3.of(a)} ${2.of(b)}" }
        ThreeOfA -> kinds.let { (a, b, c) -> "${3.of(a)} ${1.of(b)} ${1.of(c)}" }
        TwoPair -> kinds.let { (a, b, c) -> "${2.of(a)} ${2.of(b)} ${1.of(c)}" }
        OnePair -> 2.of(kinds.first()) + " " + kinds.drop(1).joinToString(" ") { 1.of(it) }
        HighCard -> kinds.joinToString(" ") { 1.of(it) }
    }

}

fun String.toHand(): Hand {
    val g = map { order.indexOf(it) }.onEach { check(it in 2..14) { "$it on `$this`" } }
        .groupBy { it }.mapValues { (_, v) -> v.size }.toList()
        .sortedWith(compareBy(Pair<Int, Int>::second, Pair<Int, Int>::first)).reversed()
    check(g.sumOf { it.second } == 5) { "`$this` parsed to $g and doesn't sum up" }
    g.windowed(2)
        .forEach { (a, b) -> check(a.second > b.second || a.second == b.second && a.first > b.first) { "failed sorting $g on $a vs $b" } }
    val t1 = g.size
    val t2 = g[0].second
    val kinds = g.map { it.first }

    return when {
        t1 == 1 && t2 == 5 -> Hand(FiveOfA, kinds)
        t1 == 2 && t2 == 4 -> Hand(FourOfA, kinds)
        t1 == 2 && t2 == 3 -> Hand(FullHouse, kinds)
        t1 == 3 && t2 == 3 -> Hand(ThreeOfA, kinds)
        t1 == 3 && t2 == 2 -> Hand(TwoPair, kinds)
        t1 == 4 && t2 == 2 -> Hand(OnePair, kinds)
        t1 == 5 && t2 == 1 -> Hand(HighCard, kinds)
        else -> error("what to do with `$this` that parsed to $g ???")
    }.logged { this }
}

fun part1(input: Input) = input.sortedBy { it.first }
    .mapIndexed { index, (hand, bid) -> Triple(index + 1, hand, bid).logged() }
    .sumOf { (rank, hand, bid) -> rank * bid }

fun part2(input: Input): Any? = TODO("part 2 with ${input.toString().length} data")


