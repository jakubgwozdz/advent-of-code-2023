package aoc2023.day23

import java.io.File

fun Map<Pos, List<Pair<Pos, Int>>>.tgf() {
    val indices = keys.withIndex().associate { (i, k) -> k to i }
    File("local/day23.tgf").writer().use { writer ->
        keys.forEach { writer.appendLine("${indices[it]} ${it.first},${it.second}") }
        writer.appendLine("#")
        forEach { (p, l) ->
            l.forEach { (p2, c) -> writer.appendLine("${indices[p]} ${indices[p2]} $c") }
        }
    }
}
