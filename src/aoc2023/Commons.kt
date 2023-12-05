package aoc2023

import java.io.File
import kotlin.time.Duration
import kotlin.time.measureTimedValue

fun execute(desc: String, part: () -> Any?): Duration {
    val t = measureTimedValue {
        try {
            part()
        } catch (e: Throwable) {
            e.printStackTrace()
            e.message
        }
    }
    println("$desc took ${t.duration} and resulted with: ${t.value}")
    return t.duration
}

fun getDay(function: () -> Any?) = "day\\d+".toRegex().find(function.javaClass.name)?.value

fun <T> readAndParse(filename: String, parseOp: (String) -> T) = File(filename).readText().trim().let(parseOp)

fun <T> String.tryMatch(regex: Regex, op: (MatchResult.Destructured) -> T) =
    regex.matchEntire(this)?.destructured?.let(op)

