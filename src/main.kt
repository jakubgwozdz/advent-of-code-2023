fun main() = (1..25).forEach {
    try {
        Class.forName("aoc2023.day$it.Day${it}Kt").getMethod("main").invoke(null)
    } catch (_: ClassNotFoundException) {
    }
}
