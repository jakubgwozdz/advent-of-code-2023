package aoc2023.day15

import aoc2023.Puzzle
import aoc2023.expect
import aoc2023.getDay
import aoc2023.readAndParse

fun main() {
    val input = readAndParse("local/${getDay {}}_input.txt", ::parse)
    val puzzle = Puzzle(input, ::part1, ::part2)
    puzzle.part1().expect(498538)
    puzzle.part2().expect(286278)
}

typealias Input = List<String>

fun parse(inputStr: String): Input = inputStr.lines().first().split(",")

fun String.hash(): Int = fold(0) { acc: Int, c: Char -> ((acc + c.code) * 17) % 256 }

fun part1(input: Input) = input.sumOf { it.hash() }

fun part2(input: Input): Any {
    val boxes = List(256) { mutableMapOf<String, Int>() }
    input.forEach { op ->
        if (op.endsWith('-')) {
            val label = op.substringBefore('-')
            boxes[label.hash()].remove(label)
        }
        else {
            val label = op.substringBefore('=')
            boxes[label.hash()][label] = op.substringAfter('=').toInt()
        }
    }

    return boxes.withIndex().sumOf { (boxId, map) ->
        map.toList().withIndex().sumOf { (slotId, p) ->
            (boxId + 1) * (slotId + 1) * p.second
        }
    }
}
