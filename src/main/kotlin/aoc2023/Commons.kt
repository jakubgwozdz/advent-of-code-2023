package aoc2023

import kotlin.time.measureTimedValue

fun calculate(part: () -> Any) {
    val t = measureTimedValue(part)
    println("$part took ${t.duration} and gave result: ${t.value}")
}
