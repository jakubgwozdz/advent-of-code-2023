package aoc2023.day19

import aoc2023.Puzzle
import aoc2023.expect
import aoc2023.getDay
import aoc2023.logged
import aoc2023.readAndParse

fun main() {
    val input = readAndParse("local/${getDay {}}_input.txt", ::parse)
    val puzzle = Puzzle(input, ::part1, ::part2)
    puzzle.part1().expect(446935)
    puzzle.part2()
}

sealed interface Rule {
    val target: String
    fun test(part: Part): Boolean
    fun retarget(target: String): Rule
}

data class IfGt(val rating: Char, val value: Int, override val target: String) : Rule {
    override fun test(part: Part) = part[rating]!! > value
    override fun retarget(target: String) = copy(target = target)
}

data class IfLt(val rating: Char, val value: Int, override val target: String) : Rule {
    override fun test(part: Part) = part[rating]!! < value
    override fun retarget(target: String) = copy(target = target)
}

data class Else(override val target: String) : Rule {
    override fun test(part: Part) = true
    override fun retarget(target: String) = copy(target = target)
}

typealias Workflow = List<Rule>

fun Workflow.test(part: Part): String {
    forEach { if (it.test(part)) return it.target }
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
        .map { it.split(",").associate { it.first() to it.substringAfter("=").toInt() } }
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

operator fun PartRange.times(other: PartRange): PartRange =
    mapValues { (k, v) -> other[k]!!.let { v intersect it } }

infix fun IntRange.intersect(other: IntRange) = first.coerceAtLeast(other.first)..last.coerceAtMost(other.last)
fun IntRange.coerceAtLeast(i: Int) = if (i < first) this else i..last
fun IntRange.coerceAtMost(i: Int) = if (i > last) this else first..i

infix fun PartRange.intersect(r: Pair<Char, IntRange>) =
    mapValues { (k, v) -> if (k == r.first) v intersect r.second else v }

infix fun PartRange.coerceAtLeast(r: Pair<Char, Int>) =
    mapValues { (k, v) -> if (k == r.first) v.coerceAtLeast(r.second) else v }

infix fun PartRange.coerceAtMost(r: Pair<Char, Int>) =
    mapValues { (k, v) -> if (k == r.first) v.coerceAtMost(r.second) else v }

fun PartRange.count() = entries.fold(1L) { acc, (_, v) -> if (v.last < v.first) 0 else acc * (v.last - v.first + 1) }

private val IntRange.size: Int get() = if (last < first) 0 else last - first + 1

fun part2(input: Input): Long {
    val simplified = input.workflows.simplified()
        .onEach { it.describe().logged() }
    val xSet = simplified.values.splits('x')
    val mSet = simplified.values.splits('m')
    val aSet = simplified.values.splits('a')
    val sSet = simplified.values.splits('s')
    return xSet.sumOf { x ->
        x.size * mSet.sumOf { m ->
            m.size * aSet.sumOf { a ->
                a.size * sSet.sumOf { s->
                    if (simplified.test(mapOf('x' to x.first, 'm' to m.first, 'a' to a.first, 's' to s.first))) s.size.toLong() else 0
                }
            }
        }.logged(x)
    }

    val maxRange: PartRange = mapOf('x' to 1..4000, 'm' to 1..4000, 'a' to 1..4000, 's' to 1..4000)
    val mapped = simplified
        .mapValues {
            var remaining = maxRange
            val result = mutableListOf<Pair<PartRange, String>>()
            it.value.forEach { rule ->
                when (rule) {
                    is Else -> if (remaining.count() > 0) result.add(remaining to rule.target)
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
                }
            }
            result.toList()
//                .logged(it.describe())
        }
    TODO()
//    return input.workflows.calcPart2("A", maxRange)
}

fun Collection<Workflow>.splits(rating: Char) =
    flatMap { w ->
        buildSet {
            add(1)
            add(4001)
            w.forEach {
                if (it is IfGt && it.rating == rating) this.add(it.value + 1)
                if (it is IfLt && it.rating == rating) this.add(it.value)
            }
        }
    }.toSet().toList().sorted()
        .zipWithNext().map { it.first..<it.second }
        .logged(rating)


fun Map<String, Workflow>.simplified(): Map<String, Workflow> {
    var result = this
    var r = result.simplify1()
    while (r != result) {
        result = r
        r = result.simplify1().simplify3().simplify2()
    }
    return r
}

fun Map<String, Workflow>.simplify1(): Map<String, Workflow> = mapValues { (_, w) -> w.simplified() }
fun Map<String, Workflow>.simplify2(): Map<String, Workflow> {
    val used = mutableSetOf("in")
    values.forEach { w -> w.forEach { r -> used += r.target } }
    return filterKeys { it in used }
}

fun Map<String, Workflow>.simplify3(): Map<String, Workflow> = mapValues { (_, w) ->
    w.map { if ((this[it.target]?.size ?: 0) == 1) it.retarget(this[it.target]!!.single().target) else it }
}

fun Workflow.simplified(): Workflow {
    var result = this
    var r = result.simplify()
    while (r != result) {
        result = r
        r = result.simplify()
    }
    return r
}

fun Workflow.simplify(): Workflow {
    val result = mutableListOf(first())
    drop(1).forEach { r ->
        val prev = result.last()
        if (r.target != prev.target) result += r
        else if (r is Else) {
            result.removeLast(); result += r
        } else if (r is IfGt && prev is IfGt && r.rating == prev.rating) {
            result.removeLast(); result += listOf(r, prev).minBy { it.target }
        } else if (r is IfLt && prev is IfLt && r.rating == prev.rating) {
            result.removeLast(); result += listOf(r, prev).maxBy { it.target }
        } else result += r
    }
    return result.toList()
}

private fun Map.Entry<String, Workflow>.describe(): String = "$key{${
    value.joinToString(",") {
        when (it) {
            is Else -> it.target
            is IfGt -> "${it.rating}>${it.value}:${it.target}"
            is IfLt -> "${it.rating}<${it.value}:${it.target}"
        }
    }
}}"




