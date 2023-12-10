package aoc2023.day10

import aoc2023.day10.Move.E
import aoc2023.day10.Move.N
import aoc2023.day10.Move.S
import aoc2023.day10.Move.W
import org.junit.jupiter.api.Timeout
import kotlin.test.Test
import kotlin.test.expect

class Day10Test {

    @Test
    fun part2t0a() {
        expect(16) { listOf(E, E, E, E, E, S, S, S, S, S, W, W, W, W, W, N, N, N, N, N).countInsides() }
    }

    @Test
    @Timeout(5)
    fun part2test1() {
        val testInputStr = """
            ...........
            .S-------7.
            .|F-----7|.
            .||.....||.
            .||.....||.
            .|L-7.F-J|.
            .|..|.|..|.
            .L--J.L--J.
            ...........
        """.trimIndent()
        expect(4) { part2(parse(testInputStr, 'F')) }
    }

    @Test
    @Timeout(5)
    fun part2test1a() {
        val testInputStr = """
            ..........
            .S------7.
            .|F----7|.
            .||....||.
            .||....||.
            .|L-7F-J|.
            .|..||..|.
            .L--JL--J.
            ..........
        """.trimIndent()
        expect(4) { part2(parse(testInputStr, 'F')) }
    }

    @Test
    @Timeout(5)
    fun part2test2() {
        val testInputStr = """
            .F----7F7F7F7F-7....
            .|F--7||||||||FJ....
            .||.FJ||||||||L7....
            FJL7L7LJLJ||LJ.L-7..
            L--J.L7...LJS7F-7L7.
            ....F-J..F7FJ|L7L7L7
            ....L7.F7||L7|.L7L7|
            .....|FJLJ|FJ|F7|.LJ
            ....FJL-7.||.||||...
            ....L---J.LJ.LJLJ...
        """.trimIndent()
        expect(8) { part2(parse(testInputStr, 'F')) }
    }

    @Test
    @Timeout(5)
    fun part2test3() {
        val testInputStr = """
            FF7FSF7F7F7F7F7F---7
            L|LJ||||||||||||F--J
            FL-7LJLJ||||||LJL-77
            F--JF--7||LJLJ7F7FJ-
            L---JF-JLJ.||-FJLJJ7
            |F|F-JF---7F7-L7L|7|
            |FFJF7L7F-JF7|JL---7
            7-L-JL7||F7|L7F-7F7|
            L.L7LFJ|||||FJL7||LJ
            L7JLJL-JLJLJL--JLJ.L
        """.trimIndent()
        expect(10) { part2(parse(testInputStr, '7')) }
    }


}
