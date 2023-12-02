package aoc2023.day2

import aoc2023.Puzzle
import aoc2023.getDay
import aoc2023.readAndParse

fun main() {
    val input = readAndParse("local/${getDay {}}_input.txt", ::parse)
    val puzzle = Puzzle(input, ::part1, ::part2)
    puzzle.part1()
    puzzle.part2()
}

typealias Input = List<Game>

data class Game(val id: Int, val sets: List<GameSet>)
data class GameSet(val r: Int, val g: Int, val b: Int)

fun part1(input: Input) = input.filter { game -> game.sets.all { it.r <= 12 && it.g <= 13 && it.b <= 14 } }
    .sumOf { it.id }

fun part2(input: Input) = input.sumOf { game ->
    val r = game.sets.maxOfOrNull { it.r } ?: 0
    val g = game.sets.maxOfOrNull { it.g } ?: 0
    val b = game.sets.maxOfOrNull { it.b } ?: 0
    r * g * b
}

fun parse(inputStr: String): Input = inputStr.lines().filterNot { it.isBlank() }.map {
    val (gameIdDesc, gameDesc) = it.split(":")
    val id = gameIdDesc.substringAfter(" ").toInt()
    val sets = gameDesc.split(";").map { setDesc ->
        var r = 0
        var g = 0
        var b = 0
        setDesc.split(",").forEach { t ->
            val (i, c) = t.trim().split(" ")
            when (c) {
                "red" -> r = i.toInt()
                "green" -> g = i.toInt()
                "blue" -> b = i.toInt()
                else -> error("`$c` in $gameIdDesc")
            }
        }
        GameSet(r, g, b)
    }
    Game(id, sets)
}
