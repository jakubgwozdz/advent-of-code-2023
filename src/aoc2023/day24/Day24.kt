package aoc2023.day24

import aoc2023.Puzzle
import aoc2023.getDay
import aoc2023.readAndParse
import kotlin.math.sign

fun main() {
    val input = readAndParse("local/${getDay {}}_input.txt", ::parse)
    val puzzle = Puzzle(input, ::part1, ::part2)
    puzzle.part1()
    puzzle.part2()
}

data class Pos(val x: Long, val y: Long, val z: Long) {
    override fun toString() = "$x, $y, $z"
}

data class Hailstone(val p: Pos, val v: Pos) {
    override fun toString(): String = "$p @ $v"
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

data class Line(val p: Pos, val v: Pos)

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
//    (x to y to result).logged("$a vs $b")

    return result


//    return if (a.v.x == b.v.x && a.p.x == b.v.y) false else if (a.v.x == b.v.x && a.v.y == b.v.y) false else {
//        // x = a.p.x + ta * a.v.x
//        // x = b.p.x + tb * b.v.x
//        // y = a.p.y + ta * a.v.y
//        // y = b.p.y + tb * b.v.y
//        //
//        // (x - a.p.x) / a.v.x == (y - a.p.y) / a.v.y  =>  (x - a.p.x) * a.v.y / a.v.x + a.p.y = y
//        // (x - b.p.x) / b.v.x == (y - b.p.y) / b.v.y  =>  (x - b.p.x) * b.v.y / b.v.x + b.p.y = y
//        //
//        // (x - a.p.x) * a.v.y / a.v.x + a.p.y = (x - b.p.x) * b.v.y / b.v.x + b.p.y
//        // x * (a.v.y / a.v.x - b.v.y / b.v.x) =
//        val x = if (a.v.x == 0L) a.p.x.toDouble() else if (b.v.x == 0L) b.p.x.toDouble() else {
//            (a.p.x.toDouble() * a.v.y / a.v.x - b.p.x.toDouble() * b.v.y / b.v.x + b.p.y) /
//                    (a.v.y.toDouble() / a.v.x - b.v.y.toDouble() / b.v.x) - a.p.y
//        }
//        val y =
//            if (a.v.y == 0L) a.p.y.toDouble() else if (b.v.y == 0L) b.p.y.toDouble() else (x - a.p.x) * a.v.y / a.v.x + a.p.y
//        (x to y).logged("$a vs $b")
//        x >= range.first && x <= range.last && y >= range.first && y <= range.last
//    }
}


fun part2(input: Input): Any? = null // TODO("part 2 with ${input.toString().length} data")


