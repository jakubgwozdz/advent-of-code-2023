package aoc2023.day19

import org.junit.jupiter.api.Timeout
import kotlin.test.Ignore
import kotlin.test.Test
import kotlin.test.expect

class Day19Test {

    val testInput: Input? = null

    val testInputStr = """
        px{a<2006:qkq,m>2090:A,rfg}
        pv{a>1716:R,A}
        lnx{m>1548:A,A}
        rfg{s<537:gd,x>2440:R,A}
        qs{s>3448:A,lnx}
        qkq{x<1416:A,crn}
        crn{x>2662:A,R}
        in{s<1351:px,qqz}
        qqz{s>2770:qs,m<1801:hdj,R}
        gd{a>3333:R,R}
        hdj{m>838:A,pv}

        {x=787,m=2655,a=1222,s=2876}
        {x=1679,m=44,a=2067,s=496}
        {x=2036,m=264,a=79,s=2244}
        {x=2461,m=1339,a=466,s=291}
        {x=2127,m=1623,a=2188,s=1013}
    """.trimIndent()


    @Test
    @Timeout(1)
    @Ignore
    fun parseTest() {
        expect(testInput) { parse(testInputStr) }
    }

    @Test
    @Timeout(5)
    fun part1test() {
        expect(19114) { part1(parse(testInputStr)) }
    }

    @Test
    @Timeout(5)
    fun part2test() {
        expect(167409079868000) { part2(parse(testInputStr)) }
    }

    @Test
    fun intRanges() {
        expect(10..20) { 0..100 intersect 10..20 }
        expect(10..20) { 10..20 intersect 0..100 }
        expect(10..20) { 10..100 intersect 0..20 }
        expect(10..20) { 0..20 intersect 10..100 }
        expect(0) { (0..10 intersect 20..30).count() }
        expect(10..20) { (10..30).coerceAtMost(20) }
        expect(10..20) { (0..20).coerceAtLeast(10) }
        expect(10..20) { (10..20).coerceAtMost(25) }
        expect(10..20) { (10..20).coerceAtLeast(5) }
    }

}
