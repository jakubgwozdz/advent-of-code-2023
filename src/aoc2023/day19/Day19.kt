package aoc2023.day19

import aoc2023.Puzzle
import aoc2023.expect
import aoc2023.getDay
import aoc2023.readAndParse

fun main() {
    val input = readAndParse("local/${getDay {}}_input.txt", ::parse)
    val puzzle = Puzzle(input, ::part1, ::part2)
    puzzle.part1().expect(446935)
    puzzle.part2().expect(141882534122898)
}

sealed interface Rule {
    val target: String
}

data class IfGt(val rating: Char, val value: Int, override val target: String) : Rule
data class IfLt(val rating: Char, val value: Int, override val target: String) : Rule
data class Else(override val target: String) : Rule
typealias Workflow = List<Rule>

fun Workflow.test(part: Part): String {
    forEach {
        if (it is IfGt && part[it.rating]!! > it.value ||
            it is IfLt && part[it.rating]!! < it.value ||
            it is Else
        ) return it.target
    }
    error("fail on workflow $this")
}

typealias Part = Map<Char, Int>

data class Input(val workflows: Map<String, Workflow>, val parts: List<Part>)

fun parse(inputStr: String): Input {
    val r1 = "(\\w+)\\{(.+)}".toRegex()
    val lines = inputStr.lines()
    val rules = lines.takeWhile(String::isNotBlank)
        .mapNotNull(r1::matchEntire)
        .map(MatchResult::destructured)
        .associate { (key, desc) ->
            key to desc.split(",").map {
                val target = it.substringAfter(":")
                val c = it.first()
                if (it.contains("<")) IfLt(c, it.substringAfter("<").substringBefore(":").toInt(), target)
                else if (it.contains(">")) IfGt(c, it.substringAfter(">").substringBefore(":").toInt(), target)
                else Else(target)
            }
        }
    val parts = lines
        .dropWhile(String::isNotBlank)
        .filter(String::isNotBlank)
        .map { it.drop(1).dropLast(1) }
        .map { r -> r.split(",").associate { it.first() to it.substringAfter("=").toInt() } }
    return Input(rules, parts)
}

fun part1(input: Input) = input.parts.filter { part ->
    input.workflows.test(part)
}.sumOf { part -> part.values.sum() }

fun Map<String, Workflow>.test(part: Part): Boolean {
    var w = "in"
    while (w in this) w = this[w]!!.test(part)
    return w == "A"
}

typealias PartRange = Map<Char, IntRange>

fun PartRange.intersect(other: PartRange): PartRange = mapValues { (k, v) -> other[k]!!.let { v intersect it } }

infix fun IntRange.intersect(other: IntRange) = first.coerceAtLeast(other.first)..last.coerceAtMost(other.last)
fun IntRange.coerceAtLeast(i: Int) = if (i < first) this else i..last
fun IntRange.coerceAtMost(i: Int) = if (i > last) this else first..i

infix fun PartRange.coerceAtLeast(r: Pair<Char, Int>) =
    mapValues { (k, v) -> if (k == r.first) v.coerceAtLeast(r.second) else v }

infix fun PartRange.coerceAtMost(r: Pair<Char, Int>) =
    mapValues { (k, v) -> if (k == r.first) v.coerceAtMost(r.second) else v }

fun PartRange.count() = entries.fold(1L) { acc, (_, v) -> if (v.last < v.first) 0 else acc * (v.last - v.first + 1) }

fun part2(input: Input): Long {
    val maxRange: PartRange = mapOf('x' to 1..4000, 'm' to 1..4000, 'a' to 1..4000, 's' to 1..4000)
    val mapped = input.workflows
        .mapValues {
            var remaining = maxRange
            val result = mutableListOf<Pair<PartRange, String>>()
            it.value.forEach { rule ->
                when (rule) {
                    is IfGt -> {
                        val matching = remaining.coerceAtLeast(rule.rating to rule.value + 1)
                        val rest = remaining.coerceAtMost(rule.rating to rule.value)
                        if (matching.count() > 0L && rest != remaining) {
                            remaining = rest
                            result.add(matching to rule.target)
                        }
                    }

                    is IfLt -> {
                        val matching = remaining.coerceAtMost(rule.rating to rule.value - 1)
                        val rest = remaining.coerceAtLeast(rule.rating to rule.value)
                        if (matching.count() > 0L && rest != remaining) {
                            remaining = rest
                            result.add(matching to rule.target)
                        }
                    }

                    else -> if (remaining.count() > 0) result.add(remaining to rule.target)
                }
            }
            result.toList()
        }

    return DeepRecursiveFunction<Pair<PartRange, String>, Long> { (range, key) ->
        when (key) {
            "A" -> range.count()
            "R" -> 0
            else -> mapped[key]!!.sumOf { (p, k) -> callRecursive(p.intersect(range) to k) }
        }
    }(maxRange to "in")
}
