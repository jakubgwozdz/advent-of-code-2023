package aoc2023.day24

import aoc2023.Puzzle
import aoc2023.expect
import aoc2023.getDay
import aoc2023.logged
import aoc2023.readAndParse
import kotlin.math.sign

fun main() {
    val input = readAndParse("local/${getDay {}}_input.txt", ::parse)
    val puzzle = Puzzle(input, ::part1, ::part2)
    puzzle.part1().expect(31921)
    puzzle.part2().expect(761691907059631)
}

data class Pos(val x: Long, val y: Long, val z: Long) {
    override fun toString() = "$x, $y, $z"
}

data class Hailstone(val p: Pos, val v: Pos) {
    override fun toString(): String = "$p @ $v"
    fun atTime(t: Long) = Pos(p.x + t * v.x, p.y + t * v.y, p.z + t * v.z)
}

typealias Input = List<Hailstone>

fun parse(inputStr: String): Input = inputStr.lines().filterNot { it.isBlank() }.map { line ->
    val (p, v) = line.split("@")
        .map { it.split(",").map(String::trim).map(String::toLong) }
        .map { (x, y, z) -> Pos(x, y, z) }
    Hailstone(p, v)
}

fun part1(input: Input, range: LongRange = 200000000000000..400000000000000) =
    input.indices.sumOf { ia -> (ia..input.lastIndex).count { ib -> calc2d(input[ia], input[ib], range) } }

fun calc2d(a: Hailstone, b: Hailstone, range: LongRange): Boolean {
    val m1 = a.v.y.toDouble() / a.v.x
    val m2 = b.v.y.toDouble() / b.v.x

    val b1 = a.p.y - m1 * a.p.x
    val b2 = b.p.y - m2 * b.p.x

    if (m1 == m2) {
        return false
    }

    val x = (b2 - b1) / (m1 - m2)
    val y = m1 * x + b1
    val result = x >= range.first && x <= range.last && y >= range.first && y <= range.last &&
            (x - a.p.x).sign == a.v.x.toDouble().sign && (y - a.p.y).sign == a.v.y.toDouble().sign &&
            (x - b.p.x).sign == b.v.x.toDouble().sign && (y - b.p.y).sign == b.v.y.toDouble().sign
    return result
}

fun part2(input: Input): Long {
    val pivot = input.first { h ->
        input.all { h2 ->
            h.v.x == h2.v.x && h.p.x == h2.p.x ||
                    h.v.x != h2.v.x && (h.p.x - h2.p.x) % (h2.v.x - h.v.x) == 0L
        }
    }.logged("pivot")

    val (a, b) = input
    val ta = (a.p.x - pivot.p.x) / (pivot.v.x - a.v.x).logged("ta")
    val tb = (b.p.x - pivot.p.x) / (pivot.v.x - b.v.x).logged("tb")
    val a1 = a.atTime(ta)
    val b1 = b.atTime(tb)
    val dx = b1.x - a1.x
    val dy = b1.y - a1.y
    val dz = b1.z - a1.z
    val px = a1.x + dx / (ta - tb) * ta
    val py = a1.y + dy / (ta - tb) * ta
    val pz = a1.z + dz / (ta - tb) * ta

    return px+py+pz
}
