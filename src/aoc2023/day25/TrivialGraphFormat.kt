package aoc2023.day25

import java.io.File

fun Map<String, List<String>>.tgf(filename: String) {
    File(filename).writer().use { writer ->
        keys.forEach { writer.appendLine("$it $it") }
        writer.appendLine("#")
        forEach { (p, l) ->
            l.forEach { p2 -> writer.appendLine("$p $p2") }
        }
    }
}
