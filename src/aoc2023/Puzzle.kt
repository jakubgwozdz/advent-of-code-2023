package aoc2023

class Puzzle<T : Any>(
    private val input: T,
    private val p1: (T) -> Any?,
    private val p2: (T) -> Any?,
) {
    val day = "aoc\\d+.day\\d+".toRegex().find(p1.javaClass.name)?.value
    fun part1() = execute("$day.part1()") { p1(input) }
    fun part2() = execute("$day.part2()") { p2(input) }
}
