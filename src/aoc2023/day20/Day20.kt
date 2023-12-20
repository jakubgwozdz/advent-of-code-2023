package aoc2023.day20

import aoc2023.Puzzle
import aoc2023.Queue
import aoc2023.day20.Type.Broadcaster
import aoc2023.day20.Type.Conjunction
import aoc2023.day20.Type.FlipFlop
import aoc2023.expect
import aoc2023.getDay
import aoc2023.logged
import aoc2023.readAndParse

fun main() {
    val input = readAndParse("local/${getDay {}}_input.txt", ::parse)
    val puzzle = Puzzle(input, ::part1, ::part2)
    puzzle.part1().expect(883726240)
    puzzle.part2()
}

enum class Type { Broadcaster, FlipFlop, Conjunction }
typealias Input = Map<String, Pair<Type, List<String>>>

sealed interface ModuleState

data class FlipFlopState(var on: Boolean) : ModuleState
data class ConjunctionState(val last: MutableMap<String, Boolean>) : ModuleState
data object BroadcasterState : ModuleState

typealias State = Map<String, ModuleState>

fun parse(inputStr: String): Input = inputStr.lines().filterNot { it.isBlank() }
    .map { it.split(" -> ") }
    .associate { (f, t) ->
        val (name, type) = when {
            f.startsWith("%") -> f.drop(1) to FlipFlop
            f.startsWith("&") -> f.drop(1) to Conjunction
            else -> f to Broadcaster
        }
        val outputs = t.split(", ").filterNot { it.isBlank() }
        name to (type to outputs)
    }

data class Signal(val from: String, val module: String, val high: Boolean)

fun State.perform(input: Input, op: (Signal)->Unit) {
    val toGo = Queue<Signal>()
    toGo.offer(Signal("button", "broadcaster", false))
    while (toGo.isNotEmpty()) {
        val (from, module, high) = toGo.poll().also(op)
        val s = this[module] ?: continue
        val o = input[module]!!
        when (o.first) {
            Broadcaster -> o.second.forEach { toGo.offer(Signal(module, it, high)) }
            FlipFlop -> if (!high) {
                val on = (s as FlipFlopState).on
                o.second.forEach { toGo.offer(Signal(module, it, !on)) }
                s.on = !on
            }

            Conjunction -> {
                (s as ConjunctionState).last[from] = high
                val h2 = !s.last.values.all { it }
                o.second.forEach { toGo.offer(Signal(module, it, h2)) }
            }
        }
    }
}

fun part1(input: Input): Any {
    val state = initial(input)
    var lowPulses = 0
    var highPulses = 0

    repeat(1000) {
        state.perform(input) {
            if (it.high) highPulses++ else lowPulses++
        }
    }
    return highPulses * lowPulses
}

private fun initial(input: Input): State {
    val inputs = input.keys.associateWith { module ->
        input.filterValues { module in it.second }.keys
    }
    return input.mapValues { (module, v) ->
        when (v.first) {
            Broadcaster -> BroadcasterState
            FlipFlop -> FlipFlopState(false)
            Conjunction -> ConjunctionState(inputs[module]!!.associateWith { false }.toMutableMap())
        }
    }
}

fun part2(input: Input): Any {
    val edges: List<Pair<String, String>> = input.flatMap { (src, v) ->
        v.second.map { src to it }
    }
    val vertices: Set<String> = input.flatMap { (src, v) -> v.second + src }.toSet()

    vertices.forEach { module ->
        edges.indirectTo(module).logged { "$module (${it.size})" }
    }

    val viable = edges.indirectTo("rx")
    viable.logged(viable.size)

    TODO()


    val state = initial(input)
    TODO()
    var count = 0
    val toGo = Queue<Signal>()

    while (true) {
        count++
        toGo.offer(Signal("button", "broadcaster", false))
        var rxLow = 0
        var rxHigh = 0
        while (toGo.isNotEmpty()) {
            val (from, module, high) = toGo.poll()
            if (module == "rx") if (high) rxHigh++ else rxLow++
            val s = state[module] ?: continue
            val o = input[module]!!
            when (o.first) {
                Broadcaster -> o.second.forEach { toGo.offer(Signal(module, it, high)) }
                FlipFlop -> if (!high) {
                    val on = (s as FlipFlopState).on
                    o.second.forEach { toGo.offer(Signal(module, it, !on)) }
                    s.on = !on
                }

                Conjunction -> {
                    (s as ConjunctionState).last[from] = high
                    val h2 = !s.last.values.all { it }
                    o.second.forEach { toGo.offer(Signal(module, it, h2)) }
                }
            }
        }
        if (rxLow > 0) (rxLow to rxHigh).logged(count)
        if (rxHigh == 0 && rxLow == 1) return count
    }
}

private fun List<Pair<String, String>>.indirectTo(module: String): Set<String> {
    val reachable = mutableSetOf<String>()
    DeepRecursiveFunction<String, Unit> { module ->
        forEach { (src,dest)->
            if (dest == module && src !in reachable) {
                reachable+=src
                callRecursive(src)
            }
        }
    }(module)
    return reachable.toSet()
}

