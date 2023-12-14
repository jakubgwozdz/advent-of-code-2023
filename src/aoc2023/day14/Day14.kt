package aoc2023.day14

import aoc2023.Puzzle
import aoc2023.getDay
import aoc2023.readAndParse

fun main() {
    val input = readAndParse("local/${getDay {}}_input.txt", ::parse)
    val puzzle = Puzzle(input, ::part1, ::part2)
    puzzle.part1(105003)
    puzzle.part2(93742)
}

typealias Input = List<String>

fun parse(inputStr: String): Input = inputStr.lines().filterNot { it.isBlank() }

fun part1(input: Input) = input.buildArray().north().toInput().calcLoad()

fun Input.performCycle() = buildArray()
    .north()
    .west()
    .south()
    .east()
    .toInput()

fun Array<CharArray>.toInput() = map { it.concatToString() }
fun Input.buildArray() = Array(size) { row -> CharArray(first().length) { col -> this[row][col] } }

fun Array<CharArray>.north() = apply {
    first().indices.forEach { col ->
        var top = -1
        indices.forEach { row ->
            when (this[row][col]) {
                '#' -> top = row
                'O' -> {
                    this[row][col] = '.'
                    this[++top][col] = 'O'
                }
            }
        }
    }
}

fun Array<CharArray>.west() = apply {
    indices.forEach { row ->
        var top = -1
        first().indices.forEach { col ->
            when (this[row][col]) {
                '#' -> top = col
                'O' -> {
                    this[row][col] = '.'
                    this[row][++top] = 'O'
                }
            }
        }
    }
}

fun Array<CharArray>.south() = apply {
    first().indices.forEach { col ->
        var top = size
        indices.reversed().forEach { row ->
            when (this[row][col]) {
                '#' -> top = row
                'O' -> {
                    this[row][col] = '.'
                    this[--top][col] = 'O'
                }
            }
        }
    }
}

fun Array<CharArray>.east() = apply {
    indices.forEach { row ->
        var top = first().size
        first().indices.reversed().forEach { col ->
            when (this[row][col]) {
                '#' -> top = col
                'O' -> {
                    this[row][col] = '.'
                    this[row][--top] = 'O'
                }
            }
        }
    }
}

fun Input.calcLoad(): Int =
    withIndex().sumOf { (row, line) -> line.count { it == 'O' } * (size - row) }

fun part2(input: Input): Any {

    val done = mutableMapOf<Input, Long>()
    var state = input
    var step = 0L

    while (state !in done) {
        done[state] = step++
        state = state.performCycle()
    }
    return done.toList().single { it.second == (done[state]!!..<step).at(1000000000) }.first.calcLoad()
}

fun LongRange.at(n: Int): Long {
    val size = last - first + 1
    return n - (n / size - first / size - first / size) * size
}
