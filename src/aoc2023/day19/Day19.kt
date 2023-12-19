package aoc2023.day19

import aoc2023.Puzzle
import aoc2023.getDay
import aoc2023.readAndParse

fun main() {
    val input = readAndParse("local/${getDay {}}_input.txt", ::parse)
    val puzzle = Puzzle(input, ::part1, ::part2)
    puzzle.part1()
    puzzle.part2()
}

fun interface Rule {
    fun test(part: Part): String?
}

data class IfGt(val rating: Char, val value: Int, val target: String) : Rule {
    override fun test(part: Part) = if (part[rating]!! > value) target else null
}

data class IfLt(val rating: Char, val value: Int, val target: String) : Rule {
    override fun test(part: Part) = if (part[rating]!! < value) target else null
}

data class Else(val target: String) : Rule {
    override fun test(part: Part) = target
}

typealias Workflow = List<Rule>

fun Workflow.test(part: Part): String {
    forEach { it.test(part)?.let { return it } }
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
    var w = "in"
    while (w in input.workflows) w = input.workflows[w]!!.test(part)
    w == "A"
}.sumOf { part -> part.values.sum() }

fun part2(input: Input): Long {

    TODO()
}


