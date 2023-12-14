package aoc2023.day14

import aoc2023.Puzzle
import aoc2023.getDay
import aoc2023.logged
import aoc2023.readAndParse

fun main() {
    val input = readAndParse("local/${getDay {}}_input.txt", ::parse)
    val puzzle = Puzzle(input, ::part1, ::part2)
    puzzle.part1(105003)
    puzzle.part2()
}

//data class Input(val todo: Int)
typealias Input = List<String>

fun parse(inputStr: String): Input = inputStr.lines().filterNot { it.isBlank() }

fun part1(input: Input)= buildArray(input).apply(Array<CharArray>::north)
    .let(::toStrings).let(::calcLoad)

fun performCycle(state: Input): Input {
    val array = buildArray(state)
    array.north()
    array.west()
    array.south()
    array.east()
    return toStrings(array)
}

fun toStrings(array: Array<CharArray>) = array.map { it.concatToString() }
fun buildArray(state: Input) = Array(state.size) { row -> CharArray(state.first().length) { col -> state[row][col] } }

fun Array<CharArray>.north() {
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

fun Array<CharArray>.west() {
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

fun Array<CharArray>.south() {
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

fun Array<CharArray>.east() {
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

fun calcLoad(state: Input): Int =
    state.withIndex().sumOf { (row, line) -> line.count { it == 'O' } * (state.size - row) }

fun part2(input: Input): Any {
    check(input == toStrings(buildArray(input)))
    val cycles = 1000000000

    var state = input
    val done = mutableMapOf<Input, Long>()
    var step = 0L

    while (state !in done) {
        done[state] = step++
        state = performCycle(state)
    }
    val cycle = step - done[state]!!

    step.logged("step")
    cycle.logged("cycle")
    val times = (cycles / cycle).logged("times")
    val last = (cycles - times * cycle).logged("last")
    (last + cycle * times).logged("check")
    return calcLoad(done.toList().single { it.second == last }.first)
}
// 94622 - wrong, too high
// 94291 - too high
