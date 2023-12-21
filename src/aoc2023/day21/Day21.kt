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
    // 1240757633899542 too high
    // 620389562662470 too high
}

typealias Input = List<String>

fun parse(inputStr: String): Input {
    return inputStr.lines().filterNot { it.isBlank() }
}

fun part1(input: Input) = input.calc(64)

fun part2(input: Input): Any {
//    repeat(10) {
//        input.calc(input.size * it - 1).logged(input.size * it - 1)
//    }

    val cycle = (input.size * 2)
        .logged("cycle")
    val times = (26501365 / cycle)
        .logged("times")
    val rest = (26501365 % cycle)
        .logged("rest")
    // f(n * cycle - 1) = n^2 * f(cycle - 1)
    input.calc(cycle - 1)
        .logged("calc(cycle-1)")

    fun f(n: Int) = input.calc(n * cycle + rest)

    val f1 = f(1)
        .logged("f(1) = calc(cycle+rest)")
    val f2 = f(2)
        .logged("f(2) = calc(2*cycle+rest)")
    val f3 = f(3)
        .logged("f(3) = calc(3*cycle+rest)")

    // f(n) = n^2 * x + n * y + z

    // f1 = x + y + z
    // f2 = 4x + 2y + z
    // f3 = 9x + 3y + z

    val x = ((f1 + f3 - 2 * f2) / 2)
        .logged("x")
    val y = (f2 - f1 - 3 * x)
        .logged("y")
    val z = (f1 - x - y)
        .logged("z")

    fun g(n: Int) = x * n * n + y * n + z

    repeat(10) {
        f(it).logged("f($it)")
        g(it).logged("g($it)")
    }
    return g(times)
}

fun Input.calc(steps: Int): Long {
    check(all { it.length == size })
    val array45 = Array(size * 2) { BooleanArray(size * 2) }
    array45.indices.forEach { r45 ->
        array45[r45].indices.forEach { c45 ->
            if (r45 % 2 == c45 % 2) array45[r45][c45] =
                this[((r45 + c45 + size) / 2).mod(size)][((r45 - c45 + size) / 2).mod(size)] != '#'
        }
    }

    val p = size - steps..size + steps step 2
    return p.sumOf { r -> p.count { c -> array45[r.mod(size * 2)][c.mod(size * 2)] } }.toLong()

}

