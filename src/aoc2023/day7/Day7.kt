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

fun parse(inputStr: String): List<Pair<String, Int>> =
    inputStr.lines().filterNot { it.isBlank() }
        .map { it.split(" ") }
        .map { (a, b) -> a to b.toInt() }

const val order = "_j23456789TJQKA"

data class Hand(val type: Int, val kinds: List<Int>) : Comparable<Hand> {
    override fun compareTo(other: Hand): Int {
        if (type == other.type)
            kinds.zip(other.kinds).forEach { (a, b) -> a.compareTo(b).let { if (it != 0) return it } }
        return type.compareTo(other.type)
    }
}

fun String.toHand(): Hand {
    val counts = groupBy { it }.map { (_, v) -> v.size }.sortedDescending()
    val type = counts[0]*10+counts.getOrElse(1) { 0 }
    val kinds = map { order.indexOf(it) }
    return Hand(type, kinds)
}

fun String.toHandWithJokers(): Hand {
    if ('J' !in this) return toHand()
    val bestType = "234567890TQKA".maxOf { replace('J', it).toHand() }.type
    val kinds = replace('J', 'j').map { order.indexOf(it) }
    return Hand(bestType, kinds)
}

fun List<Pair<String, Int>>.solve(op: (String) -> Hand) = map { op(it.first) to it.second }
    .sortedBy { it.first }
    .map { it.second }
    .mapIndexed { index, bid -> (index + 1) * bid }
    .sum()

fun part1(input: List<Pair<String, Int>>) = input.solve(String::toHand)
fun part2(input: List<Pair<String, Int>>) = input.solve(String::toHandWithJokers)
