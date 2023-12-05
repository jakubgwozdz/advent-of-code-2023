package aoc2023.day5

import aoc2023.Puzzle
import aoc2023.getDay
import aoc2023.readAndParse

fun main() {
    val input = readAndParse("local/${getDay {}}_input.txt", ::parse)
    val puzzle = Puzzle(input, ::part1, ::part2)
    puzzle.part1()
    puzzle.part2()
}

data class Entry(val dest: Long, val src: Long, val len: Long) {
    fun locate(src: Long) = if (src in this.src..<this.src + len) dest + (src - this.src) else null
}

typealias Mapping = List<Entry>

fun Mapping.locate(src: Long): Long {
    forEach { entry -> entry.locate(src)?.let { return it } }
    return src
}

data class Input(val seeds: List<Long>, val mappings: List<Mapping>) {
    override fun toString() = describe()

    fun findLastDestination(seed: Long): Long = mappings.fold(seed) { acc, entries -> entries.locate(acc) }
}

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

fun part1(input: Input): Any = input.seeds.minOf(input::findLastDestination)

fun part2(input: Input): Any? {
    TODO("part 2 with ${input.toString().length} data")
}

fun Input.describe() = buildString {
    appendLine("seeds: ${seeds.joinToString(" ")}")
    mappings.forEachIndexed { index, mapping ->
        appendLine()
        appendLine("No. $index map:")
        mapping.forEach { entry -> appendLine("$entry.dest $entry.src $entry.len") }
    }
}

