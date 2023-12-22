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
    puzzle.part2()
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

fun part1(input: Input): Any {
    val maxX = input.maxOf { it.xs.last }
    val maxY = input.maxOf { it.ys.last }

    val floor = Brick(0, 0..maxX, 0..maxY, 0..0)

    // id of the topmost brick for top[x][y]
    val tops = Array(maxX + 1) { IntArray(maxY + 1) }

    // supports
    val supportedBy = mutableMapOf<Int, MutableSet<Int>>()
    val supporting = mutableMapOf<Int, MutableSet<Int>>()

    val sorted = input.sortedBy { it.zs.first }
    val settled = mutableMapOf(floor.id to floor)
    fun brickAt(x: Int, y: Int) = settled[tops[x][y]]!!

    sorted.forEach { brick ->
        val top = brick.xs.maxOf { x -> brick.ys.maxOf { y -> brickAt(x, y).zs.last } }
        // bricksOnTop
        brick.xs.forEach { x ->
            brick.ys.forEach { y ->
                val b = brickAt(x, y)
                if (b.zs.last == top) {
                    supporting.computeIfAbsent(b.id) { mutableSetOf() }.add(brick.id)
                    supportedBy.computeIfAbsent(brick.id) { mutableSetOf() }.add(b.id)
                }
            }
        }
        brick.settleAt(top + 1)
            .also { it.xs.forEach { x -> it.ys.forEach { y -> tops[x][y] = it.id } } }
            .also { settled[it.id] = it }
    }

    return (1..input.size).count { (supporting[it] ?: emptySet()).all { b -> (supportedBy[b]?.size ?: 0) > 1 } }
}

fun part2(input: Input): Any {
    val maxX = input.maxOf { it.xs.last }
    val maxY = input.maxOf { it.ys.last }

    val floor = Brick(0, 0..maxX, 0..maxY, 0..0)

    // id of the topmost brick for top[x][y]
    val tops = Array(maxX + 1) { IntArray(maxY + 1) }

    val sorted = input.sortedBy { it.zs.first }
    val settled = mutableMapOf(floor.id to floor)
    fun brickAt(x: Int, y: Int) = settled[tops[x][y]]!!

    sorted.forEach { brick ->
        val top = brick.xs.maxOf { x -> brick.ys.maxOf { y -> brickAt(x, y).zs.last } }
        brick.settleAt(top + 1)
            .also { it.xs.forEach { x -> it.ys.forEach { y -> tops[x][y] = it.id } } }
            .also { settled[it.id] = it }
    }

    val preSorted = settled.values.sortedBy { it.zs.first }
    return (1..input.size).sumOf { id -> falling(preSorted.filter { it.id != id }) }
}

fun falling(sorted: Input): Int {
    val floor = sorted[0]
    val maxX = floor.xs.last
    val maxY = floor.ys.last
    val tops = Array(maxX + 1) { IntArray(maxY + 1) }
    val settled = mutableMapOf(floor.id to floor)
    fun brickAt(x: Int, y: Int) = settled[tops[x][y]]!!
    var count = 0
    sorted.drop(1).forEach { brick ->
        val top = brick.xs.maxOf { x -> brick.ys.maxOf { y -> brickAt(x, y).zs.last } }
        if (brick.zs.first > top+1) count++
        brick.settleAt(top + 1)
            .also { it.xs.forEach { x -> it.ys.forEach { y -> tops[x][y] = it.id } } }
            .also { settled[it.id] = it }
    }

    return count
}


