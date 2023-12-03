package aoc2023.day3

import aoc2023.Puzzle
import aoc2023.getDay
import aoc2023.readAndParse

fun main() {
    val input = readAndParse("local/${getDay {}}_input.txt", ::parse)
    val puzzle = Puzzle(input, ::part1, ::part2)
    puzzle.part1()
    puzzle.part2()
}

typealias Input = List<String>

fun Input.isInside(r: Int, c: Int) = (r in indices) && (c in this[r].indices)
fun Input.isDigitAt(r: Int, c: Int) = isInside(r, c) && this[r][c].isDigit()
fun Input.isSymbolAt(r: Int, c: Int) = isInside(r, c) && this[r][c] != '.' && !this[r][c].isDigit()
fun Input.forEachPoint(op: (Int, Int) -> Unit) = forEachIndexed { r, l -> l.forEachIndexed { c, _ -> op(r, c) } }
fun Input.findNumberPosition(r: Int, c: Int): IntRange {
    var c1 = c
    while (isDigitAt(r, c1)) c1--
    c1++
    var c2 = c
    while (isDigitAt(r, c2)) c2++
    c2--
    return c1..c2
}

fun parse(inputStr: String): Input = inputStr.lines().filterNot { it.isBlank() }

fun part1(input: Input): Any? {
    var result = 0L
    input.forEachPoint { r, c ->
        if (input.isDigitAt(r, c) && !input.isDigitAt(r, c - 1)) {
            val numberPos = input.findNumberPosition(r, c)
            val number = input[r].substring(numberPos).toLong()
            val c2 = numberPos.last
            val symbolAdj = (c - 1..c2 + 1).any { input.isSymbolAt(r - 1, it) || input.isSymbolAt(r + 1, it) }
                    || input.isSymbolAt(r, c - 1) || input.isSymbolAt(r, c2 + 1)
            if (symbolAdj) result += number
        }
    }
    return result
}

fun adjacents(r: Int, c: Int) =
    (r - 1..r + 1).flatMap { rx -> (c - 1..c + 1).filterNot { cx -> r == rx && c == cx }.map { cx -> rx to cx } }

fun part2(input: Input): Any? {
    var result = 0L
    input.forEachPoint { r, c ->
        if (input[r][c] == '*') {
            val adjNumbers = buildList {
                adjacents(r, c)
                    .filter { (rx, cx) -> input.isDigitAt(rx, cx) }
                    .forEach { (rx, cx) ->
                        val range = input.findNumberPosition(rx, cx)
                        add(rx to range)
                    }
            }.distinct()
            if (adjNumbers.size == 2) result += adjNumbers
                .map { (r1, cr) -> input[r1].substring(cr).toLong() }
                .reduce { a, b -> a * b }
        }
    }
    return result
}



