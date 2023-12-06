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

inline fun <T:Any?> T.logged(prefixOp: () -> Any? = { "" }) = also { v ->
    prefixOp().toString().let { if (it.isNotEmpty()) println("$it: $v") else println(v) }
}

fun LongRange.move(delta: Long): LongRange = first + delta..last + delta
fun LongRange.intersect(other: LongRange) = first.coerceAtLeast(other.first)..last.coerceAtMost(other.last)

fun String.ints() = split(" ").filterNot { it.isBlank() }.map { it.toInt() }
fun String.longs() = split(" ").filterNot { it.isBlank() }.map { it.toLong() }
