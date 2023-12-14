package aoc2023.day14

import aoc2023.Puzzle
import aoc2023.getDay
import aoc2023.logged
import aoc2023.readAndParse

fun main() {
    val input = readAndParse("local/${getDay {}}_input.txt", ::parse)
    val puzzle = Puzzle(input, ::part1, ::part2)
    puzzle.part1()
    puzzle.part2()
}

//data class Input(val todo: Int)
typealias Input = List<String>

fun parse(inputStr: String): Input = inputStr.lines().filterNot { it.isBlank() }

fun part1(input: Input): Any? = input[0].indices.sumOf { col ->
    val array = CharArray(input.size) { '.' }
    var top = -1
    input.indices.forEach { row ->
        if (input[row][col] == '#') {
            array[row] = '#'
            top = row
        } else if (input[row][col] == 'O') {
            array[top + 1] = 'O'
            top += 1
        }
    }
    array.withIndex().filter { it.value == 'O' }.sumOf { input.size - it.index }
}

fun performCycle(state: Input): Input {
//    state.forEach { println(it) }
//    println()

    val w = state.first().length
    val h = state.size
    val array = Array(h) { row -> CharArray(w) { col -> state[row][col] } }
    array.north()
    array.west()
    array.south()
    array.east()
    return array.map { it.concatToString() }
}

fun Array<CharArray>.north() {
    first().indices.forEach { col ->
        var top = -1
        indices.forEach { row ->
            when (this[row][col]) {
                 '#' -> top = row
                 'O' -> {
                    this[row][col] = '.'
                    this[top + 1][col] = 'O'
                    top += 1
                }
            }
        }
    }
}

fun Array<CharArray>.west() {
    (0..lastIndex).forEach { row ->
        var top = -1
        (0..first().lastIndex).forEach { col ->
            if (this[row][col] == '#') {
                top = col
            } else if (this[row][col] == 'O') {
                this[row][col] = '.'
                this[row][top + 1] = 'O'
                top += 1
            }
        }
    }
}

fun Array<CharArray>.south() {
    (0..first().lastIndex).forEach { col ->
        var top = lastIndex + 1
        (lastIndex downTo 0).forEach { row ->
            if (this[row][col] == '#') {
                top = row
            } else if (this[row][col] == 'O') {
                this[row][col] = '.'
                this[top - 1][col] = 'O'
                top -= 1
            }
        }
    }
}

fun Array<CharArray>.east() {
    (0..lastIndex).forEach { row ->
        var top = first().lastIndex + 1
        (first().lastIndex downTo 0).forEach { col ->
            if (this[row][col] == '#') {
                top = col
            } else if (this[row][col] == 'O') {
                this[row][col] = '.'
                this[row][top - 1] = 'O'
                top -= 1
            }
        }
    }
}

fun calcLoad(state: Input): Int = state.withIndex().sumOf { (row, line) ->
    line.count { it == 'O' } * (state.size - row)
}

fun part2(input: Input): Any {
    val done = mutableMapOf(input to 0L)
    var step = 0L
    var cycle = 0L
    generateSequence(input) { state ->
        val newState = performCycle(state)
        step += 1
        if (newState in done) null.also { cycle = step - done[newState]!!.logged("prev") }
        else newState.also { done[it] = step }.logged(step)
    }.last()
//    done.forEach { (t, u) -> t.logged(u) }
    step.logged("step")
    cycle.logged("cycle")
    val times = (1000000000L / cycle).logged("times")
    val last = (1000000000L - times * cycle).logged("last")
    (last + cycle * times).logged("check")
    return calcLoad(done.toList().single { (_, v) -> v == last }.first)
}
// 94622 - wrong, too high
// 94291 - too high
