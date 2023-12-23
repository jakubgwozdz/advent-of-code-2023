package aoc2023.day23

import org.junit.jupiter.api.Timeout
import kotlin.test.Ignore
import kotlin.test.Test
import kotlin.test.expect

class Day23Test {

    val testInput: Input? = null

    val testInputStr = """
        #.#####################
        #.......#########...###
        #######.#########.#.###
        ###.....#.>.>.###.#.###
        ###v#####.#v#.###.#.###
        ###.>...#.#.#.....#...#
        ###v###.#.#.#########.#
        ###...#.#.#.......#...#
        #####.#.#.#######.#.###
        #.....#.#.#.......#...#
        #.#####.#.#.#########v#
        #.#...#...#...###...>.#
        #.#.#v#######v###.###v#
        #...#.>.#...>.>.#.###.#
        #####v#.#.###v#.#.###.#
        #.....#...#...#.#.#...#
        #.#########.###.#.#.###
        #...###...#...#...#.###
        ###.###.#.###v#####v###
        #...#...#.#.>.>.#.>.###
        #.###.###.#.###.#.#v###
        #.....###...###...#...#
        #####################.#
    """.trimIndent()

    @Test
    fun part1test() {
        expect(94) { part1(parse(testInputStr)) }
    }

    @Test
    @Timeout(5)
    @Ignore
    fun part2test() {
        expect(3) { part2(parse(testInputStr)) }
    }

}
