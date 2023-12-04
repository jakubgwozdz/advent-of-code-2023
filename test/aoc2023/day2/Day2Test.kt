package aoc2023.day2

import org.junit.jupiter.api.Timeout
import kotlin.test.Ignore
import kotlin.test.Test
import kotlin.test.expect

class Day2Test {

    //    val testInput = Input(42)
    val testInput = listOf<Game>(
        Game(id = 1, sets = listOf(GameSet(4, 0, 3), GameSet(1, 2, 6), GameSet(0, 2, 0))),
        Game(id = 2, sets = listOf(GameSet(0, 2, 1), GameSet(1, 3, 4), GameSet(0, 1, 1))),
        Game(id = 3, sets = listOf(GameSet(20, 8, 6), GameSet(4, 13, 5), GameSet(1, 5, 0))),
        Game(id = 4, sets = listOf(GameSet(3, 1, 6), GameSet(6, 3, 0), GameSet(14, 3, 15))),
        Game(id = 5, sets = listOf(GameSet(6, 3, 1), GameSet(1, 2, 2)))
    )

    @Test
    @Timeout(1)
    fun parseTest() {
        expect(testInput) {
            parse(
                """
                    Game 1: 3 blue, 4 red; 1 red, 2 green, 6 blue; 2 green
                    Game 2: 1 blue, 2 green; 3 green, 4 blue, 1 red; 1 green, 1 blue
                    Game 3: 8 green, 6 blue, 20 red; 5 blue, 4 red, 13 green; 5 green, 1 red
                    Game 4: 1 green, 3 red, 6 blue; 3 green, 6 red; 3 green, 15 blue, 14 red
                    Game 5: 6 red, 1 blue, 3 green; 2 blue, 1 red, 2 green
                """.trimIndent()
            )
        }
    }

    @Test
    @Timeout(5)
    fun part1test() {
        expect(8) { part1(testInput) }
    }

    @Test
    @Timeout(5)
    fun part2test() {
        expect(2286) { part2(testInput) }
    }

}
