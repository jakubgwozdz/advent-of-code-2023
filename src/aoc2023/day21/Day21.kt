package aoc2023.day21

import aoc2023.Puzzle
import aoc2023.expect
import aoc2023.getDay
import aoc2023.logged
import aoc2023.readAndParse

fun main() {
    val input = readAndParse("local/${getDay {}}_input.txt", ::parse)
    val puzzle = Puzzle(input, ::part1, ::part2)
    puzzle.part1().expect(3751L)
    puzzle.part2()
}

typealias Input = List<String>

fun parse(inputStr: String): Input {
    return inputStr.lines().filterNot { it.isBlank() }
}

data class Pos(val r: Long, val c: Long)

operator fun Pos.plus(o: Pos) = Pos(r + o.r, c + o.c)
fun part1(input: Input) = calc(input, 64)

//fun part2(input: Input) = calc(input, 2)
fun part2(input: Input): Any = TODO()//calc(input, 26501365)

fun calc(input: Input, steps: Int): Long {
    input.withIndex().sumOf { (r, l) -> l.withIndex().count { (c, ch) -> c % 2 == r % 2 && ch != '#' } }.logged("even")
    input.withIndex().sumOf { (r, l) -> l.withIndex().count { (c, ch) -> c % 2 != r % 2 && ch != '#' } }.logged("odd")
    val size = input.size
    check(input.all { it.length == size })
    val array45 = Array(size * 2) { BooleanArray(size * 2) }
    array45.indices.forEach { r45 ->
        array45[r45].indices.forEach { c45 ->
            if (r45 % 2 == c45 % 2) array45[r45][c45] =
                input[((r45 + c45 + size) / 2).mod(size)][((r45 - c45 + size) / 2).mod(size)] != '#'
        }
    }

    val p = size - steps..size + steps step 2
    return p.sumOf { r -> p.count { c -> array45[r.mod(size * 2)][c.mod(size * 2)] } }.toLong()

}

