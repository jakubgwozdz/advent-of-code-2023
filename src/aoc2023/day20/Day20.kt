package aoc2023.day20

import aoc2023.Puzzle
import aoc2023.Queue
import aoc2023.day20.Type.Broadcaster
import aoc2023.day20.Type.Conjunction
import aoc2023.day20.Type.FlipFlop
import aoc2023.expect
import aoc2023.getDay
import aoc2023.lcm
import aoc2023.readAndParse

fun main() {
    val input = readAndParse("local/${getDay {}}_input.txt", ::parse)
    val puzzle = Puzzle(input, ::part1, ::part2)
    puzzle.part1().expect(883726240)
    puzzle.part2().expect(211712400442661)
}

enum class Type { Broadcaster, FlipFlop, Conjunction }
typealias Input = Map<String, Pair<Type, List<String>>>

sealed interface ModuleState

data class FlipFlopState(var on: Boolean) : ModuleState
data class ConjunctionState(val lastSignals: MutableMap<String, Boolean>) : ModuleState
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

data class Signal(val from: String, val dest: String, val high: Boolean)

fun State.perform(input: Input, op: (Signal) -> Unit = {}) {
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
                (s as ConjunctionState).lastSignals[from] = high
                val h2 = !s.lastSignals.values.all { it }
                o.second.forEach { toGo.offer(Signal(module, it, h2)) }
            }
        }
    }
}

fun Input.inputsOf(module: String) = filterValues { (_, l) -> module in l }.keys

private fun Input.initial(): State = mapValues { (module, v) ->
    when (v.first) {
        Broadcaster -> BroadcasterState
        FlipFlop -> FlipFlopState(false)
        Conjunction -> ConjunctionState(inputsOf(module).associateWith { false }.toMutableMap())
    }
}

fun part1(input: Input): Any {
    val state = input.initial()
    var lowPulses = 0
    var highPulses = 0
    repeat(1000) {
        state.perform(input) { if (it.high) highPulses++ else lowPulses++ }
    }
    return highPulses * lowPulses
}

fun part2(input: Input): Any {
    val state = input.initial()
    val prev = input.inputsOf("rx").single()
    val toFind = input.inputsOf(prev).toMutableSet()
    var result = 1L
    var c = 0L
    while (toFind.isNotEmpty()) {
        c++
        state.perform(input) { (from, _, high) ->
            if (high && from in toFind) {
                toFind -= from
                result = lcm(result, c)
            }
        }
    }
    return result
}
