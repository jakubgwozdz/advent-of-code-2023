package aoc2023.day22

import aoc2023.Puzzle
import aoc2023.expect
import aoc2023.getDay
import aoc2023.readAndParse
import aoc2023.tryMatch

fun main() {
    val input = readAndParse("local/${getDay {}}_input.txt", ::parse)
    val puzzle = Puzzle(input, ::part1, ::part2)
    puzzle.part1().expect(492)
    puzzle.part2().expect(86556)
}

//data class Input(val todo: Int)
data class Brick(val id: Int, val xs: IntRange, val ys: IntRange, val zs: IntRange) {
    fun settleAt(n: Int) = copy(zs = n..n + (zs.last - zs.first))
}

typealias Input = List<Brick>

val regex = "(\\d+),(\\d+),(\\d+)~(\\d+),(\\d+),(\\d+)".toRegex()
fun parse(inputStr: String): Input = inputStr.lines().filterNot { it.isBlank() }
    .mapIndexed { i, l ->
        l.tryMatch(regex) { (x1, y1, z1, x2, y2, z2) ->
            Brick(
                i + 1,
                (x1.toInt()..x2.toInt()).also { check(it.first <= it.last) },
                (y1.toInt()..y2.toInt()).also { check(it.first <= it.last) },
                (z1.toInt()..z2.toInt()).also { check(it.first <= it.last) },
            )
        } ?: error("unparsable $l")
    }

fun interface Op {
    operator fun invoke(brick: Brick, top: Int, brickAt: (Int, Int) -> Brick)
}

fun part1(input: Input): Any {
    val maxX = input.maxOf { it.xs.last }
    val maxY = input.maxOf { it.ys.last }
    val floor = Brick(0, 0..maxX, 0..maxY, 0..0)

    // supports
    val supportedBy = mutableMapOf<Int, MutableSet<Int>>()
    val supporting = mutableMapOf<Int, MutableSet<Int>>()

    settle((input + floor).sortedBy { it.zs.first }) { brick, top, brickAt ->
        brick.xs.flatMap { x -> brick.ys.map { y -> brickAt(x, y) } }
            .filter { it.zs.last == top }
            .forEach {
                supporting.computeIfAbsent(it.id) { mutableSetOf() }.add(brick.id)
                supportedBy.computeIfAbsent(brick.id) { mutableSetOf() }.add(it.id)
            }
    }

    return (1..input.size).count { (supporting[it] ?: emptySet()).all { b -> (supportedBy[b]?.size ?: 0) > 1 } }
}

fun part2(input: Input): Any {
    val maxX = input.maxOf { it.xs.last }
    val maxY = input.maxOf { it.ys.last }
    val floor = Brick(0, 0..maxX, 0..maxY, 0..0)
    val settled = settle((input + floor).sortedBy { it.zs.first }) { _, _, _ -> }
    return (1..input.size).sumOf { id -> falling(settled.filter { it.id != id }) }
}

fun falling(sorted: Input): Int {
    var count = 0
    settle(sorted) { brick, top, _ -> if (brick.zs.first > top + 1) count++ }
    return count
}

fun settle(sorted: List<Brick>, op: Op): List<Brick> {
    val floor = sorted[0]
    val maxX = floor.xs.last
    val maxY = floor.ys.last
    val tops = Array(maxX + 1) { IntArray(maxY + 1) }
    val settled = mutableMapOf(floor.id to floor)
    fun brickAt(x: Int, y: Int) = settled[tops[x][y]]!!
    sorted.drop(1).forEach { brick ->
        val top = brick.xs.maxOf { x -> brick.ys.maxOf { y -> brickAt(x, y).zs.last } }
        op(brick, top, ::brickAt)
        brick.settleAt(top + 1)
            .also { it.xs.forEach { x -> it.ys.forEach { y -> tops[x][y] = it.id } } }
            .also { settled[it.id] = it }
    }
    return settled.values.sortedBy { it.zs.first }
}

