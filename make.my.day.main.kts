#!/usr/bin/env kscript
import java.net.URI
import java.net.http.HttpClient
import java.net.http.HttpRequest
import java.net.http.HttpResponse.BodyHandlers
import java.nio.file.Path
import java.time.Duration
import java.time.LocalDate
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit
import kotlin.io.path.Path
import kotlin.io.path.readText
import kotlin.io.path.writeText

val day = args.firstOrNull() ?: LocalDate.now().dayOfMonth.toString()
val event = "2023"
val waitUntil: LocalTime = args.getOrNull(1)?.let { LocalTime.parse(it) } ?: LocalTime.of(6, 0)

makeMyDay(Path("src", "aoc$event", "day0", "Day0.kt"), day)
makeMyDay(Path("test", "aoc$event", "day0", "Day0Test.kt"), day)

fun String.atDay(day: String) = replace("day0", "day$day").replace("Day0", "Day$day")
fun makeMyDay(templatePath: Path, day: String) {
    val outputPath = templatePath.toString().atDay(day).let { Path(it) }
    outputPath.parent.toFile().mkdirs()
    val code = templatePath.readText().atDay(day)
    outputPath.writeText(code)
    println("created $outputPath from template")
}

val inputPath = Path("local", "day${day}_input.txt")
inputPath.parent.toFile().mkdirs()
inputPath.toFile().writeText("Text will be here soon\n")
println("created basic $inputPath")
ticks(waitUntil).forEach { (time, bell) -> clock(time, bell) }
downloadInput(event, day)

fun downloadInput(event: String, day: String) {
    val session = Path("local", "cookie").toFile().readText().trim()
    val request: HttpRequest = HttpRequest.newBuilder(URI("https://adventofcode.com/$event/day/$day/input"))
        .GET().header("Cookie", session).build()
    val response = HttpClient.newHttpClient().send(request, BodyHandlers.ofString())
    val input = response.body()
    inputPath.toFile().writeText(input)
    println("STATUS ${response.statusCode()}, INPUT has ${input.lines().size} lines.")
    if (input.lines().size < 10) println(input)
}

fun ticks(until: LocalTime) = generateSequence(LocalTime.now()) { now ->
    val nextTick = now.truncatedTo(ChronoUnit.SECONDS).plusSeconds(1)
    if (nextTick > until) null
    else nextTick
}.map { it to (it > until.minusSeconds(5)) }

var deletePrevious = false
fun clock(time: LocalTime, bell: Boolean) {
    val sleepTime = Duration.between(LocalTime.now(), time).toMillis() + 1
    if (sleepTime > 0) Thread.sleep(sleepTime)
    if (deletePrevious) {
        print("\u001b[1A")
    }
    if (bell) print("\u0007")
    println(time.format(DateTimeFormatter.ofPattern("HH:mm:ss")))
    deletePrevious = true
}

