package aoc2023.day5

import aoc2023.Puzzle
import aoc2023.getDay
import aoc2023.readAndParse
import aoc2023.intersect
import aoc2023.move

fun main() {
    val input = readAndParse("local/${getDay {}}_input.txt", ::parse)
    val puzzle = Puzzle(input, ::part1, ::part2)
    puzzle.part1()
    puzzle.part2()
}

data class Entry(val dest: Long, val src: Long, val len: Long)
typealias Mapping = List<Entry>

data class Input(val seeds: List<Long>, val mappings: List<Mapping>) {}

fun parse(inputStr: String): Input {
    fun String.numbers() = split(" ").filterNot { it.isBlank() }.map { it.toLong() }
    val iterator = inputStr.lines().iterator()
    val seeds = iterator.next().substringAfter(":").numbers()
    val mappings = buildList {
        while (iterator.hasNext()) {
            val next = iterator.next()
            if (next.isBlank()) continue
            else if (next.endsWith("map:")) add(buildList {
                while (iterator.hasNext()) {
                    val line = iterator.next()
                    if (line.isBlank()) break
                    else {
                        val (dest, src, len) = line.numbers()
                        add(Entry(dest, src, len))
                    }
                }
            })
            else error("wtf `$next`")
        }
    }
    return Input(seeds, mappings)
}

fun part1(input: Input): Long {
    return input.mappings
        .fold(input.seeds) { acc, mapping ->
            acc.map { locateSingle(it, mapping) }
        }
        .min()
}

fun part2(input: Input): Long {
    return input.mappings.map(Mapping::convert)
        .fold(input.seeds.chunked(2).map { (start, len) -> start..<start + len }) { acc, mapping ->
            acc.flatMap { locateRange(it, mapping) }
        }
        .minOf { it.first }
}

fun Mapping.convert(): List<Pair<LongRange, Long>> {
    val mapped = sortedBy { it.src }.map { it.src..<it.src + it.len to it.dest - it.src }
    val start = (0..<mapped.first().first.first to 0L)
    val end = (mapped.last().first.last + 1..Long.MAX_VALUE to 0L)
    val between = mapped.windowed(2) { (p, n) -> p.first.last + 1..<n.first.first to 0L }
    return (mapped + between + end + start).filterNot { it.first.isEmpty() }.sortedBy { it.first.first }
}

fun locateSingle(src: Long, mapping: Mapping): Long {
    mapping.forEach { if (src in it.src..<it.src + it.len) return it.dest + (src - it.src) }
    return src
}

fun locateRange(initial: LongRange, mappings: List<Pair<LongRange, Long>>): List<LongRange> =
    mappings.map { (src, delta) -> initial.intersect(src).move(delta) }.filterNot { it.isEmpty() }
