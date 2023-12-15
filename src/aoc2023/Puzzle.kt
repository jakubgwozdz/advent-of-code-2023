package aoc2023

class Puzzle<T : Any, R1 : Any?, R2 : Any?>(
    private val input: T,
    private val p1: (T) -> R1,
    private val p2: (T) -> R2,
) {
    val day = "aoc\\d+.day\\d+".toRegex().find(p1.javaClass.name)?.value
    fun part1() = execute("$day.part1()") { p1(input) }
    fun part2() = execute("$day.part2()") { p2(input) }
}
