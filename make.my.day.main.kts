#!/usr/bin/env kscript

import java.nio.file.Path
import java.time.LocalDate
import kotlin.io.path.Path
import kotlin.io.path.readText
import kotlin.io.path.writeText

val day = args.firstOrNull() ?: LocalDate.now().dayOfMonth.toString()
val event = "aoc2023"

makeMyDay(Path("src", event, "day0", "Day0.kt"), day)
makeMyDay(Path("test", event, "day0", "Day0Test.kt"), day)

fun String.atDay(day: String) = replace("day0", "day$day").replace("Day0", "Day$day")
fun makeMyDay(templatePath: Path, day: String) {
    val outputPath = templatePath.toString().atDay(day).let { Path(it) }
    outputPath.parent.toFile().mkdirs()
    val code = templatePath.readText().atDay(day)
    outputPath.writeText(code)
}

