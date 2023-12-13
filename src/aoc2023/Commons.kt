package aoc2023

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.runBlocking
import java.io.File
import kotlin.time.Duration
import kotlin.time.measureTimedValue

fun <R> execute(desc: String, expected: R? = null, part: () -> R?): Duration {
    val t = measureTimedValue {
        try {
            part().let {
                if (expected != null && it != expected) "$it while expected $expected"
                else it
            }
        } catch (e: Throwable) {
            e.printStackTrace()
            e.message
        }
    }
    println("$desc took ${t.duration.inWholeMicroseconds / 1000.0}ms and resulted with: ${t.value}")
    return t.duration
}

fun getDay(function: () -> Any?) = "day\\d+".toRegex().find(function.javaClass.name)?.value

fun <T> readAndParse(filename: String, parseOp: (String) -> T) = File(filename).readText().trim().let(parseOp)

fun <T> String.tryMatch(regex: Regex, op: (MatchResult.Destructured) -> T) =
    regex.matchEntire(this)?.destructured?.let(op)

inline fun <T : Any?> T.logged(prefixOp: () -> Any? = { "" }) = also { v ->
    prefixOp().toString().let { if (it.isNotEmpty()) println("$it: $v") else println(v) }
}

fun <T : Any?> T.logged(prefix: Any?) = logged { prefix }
fun <T> T.expect(e: T) = also {
    check(e == it) { "$it while expected $e" }
}

fun LongRange.move(delta: Long): LongRange = first + delta..last + delta
fun LongRange.intersect(other: LongRange) = first.coerceAtLeast(other.first)..last.coerceAtMost(other.last)

fun String.ints(delimiter: String = " ") = split(delimiter).filterNot(String::isBlank).map(String::toInt)
fun String.longs(delimiter: String = " ") = split(delimiter).filterNot(String::isBlank).map(String::toLong)

// inspired by pseudocode at https://rosettacode.org/wiki/Isqrt_(integer_square_root)_of_X
fun Long.isqrt(ceil: Boolean = false): Long {
    require(this >= 0)
    var q = 1L
    while (q <= this) q = q shl 2
    var z = this
    var r = 0L
    while (q > 1L) {
        q = q shr 2
        val t = z - r - q
        r = r shr 1
        if (t >= 0) {
            z = t
            r += q
        }
    }
    return if (!ceil || z == 0L) r else r + 1
}

tailrec fun gcd(a: Long, b: Long): Long = if (b == 0L) a else gcd(b, a % b)
fun lcm(a: Long, b: Long): Long = a / gcd(a, b) * b

inline fun <T, R> cachedDeepRecursiveFunction(
    cache: MutableMap<T, R> = mutableMapOf(),
    crossinline block: suspend DeepRecursiveScope<T, R>.(T) -> R
): DeepRecursiveFunction<T, R> =
    DeepRecursiveFunction { value ->
        cache[value] ?: block(value).also { cache[value] = it }
    }

class Cache<K, V>(val map: MutableMap<K, V> = mutableMapOf()) : MutableMap<K, V> by map {
    var hits = 0L
    var misses = 0L
    override fun get(key: K): V? {
        if (key in this) hits++ else misses++
        return map[key]
    }

    override fun toString() = "cache hits=$hits; misses=$misses"
}

//fun <T, R> List<T>.mapParallel(op: (T) -> R) = parallelStream().map { op(it) }.toList().toList()
fun <T, R> List<T>.mapParallel(op: (T) -> R) = runBlocking { map { async(Dispatchers.Default) { op(it) } }.awaitAll() }
//fun <T, R> List<T>.mapParallel(op: (T) -> R) = map { op(it) }
