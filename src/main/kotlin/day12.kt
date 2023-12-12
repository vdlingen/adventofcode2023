package day12

val input = util.readInput("day12.txt")
    .split("\n")
    .map { it.split(" ").let { items -> Line(items[0], items[1].split(",").map { it.toInt() }) } }

data class Line(val springs: String, val groups: List<Int>)

fun Line.unfold() = Line(
    listOf(springs, springs, springs, springs, springs).joinToString("?"),
    groups + groups + groups + groups + groups
)

fun Line.countArrangements(cache: MutableMap<Line, Long> = mutableMapOf()): Long = when {
    groups.isEmpty() -> if (springs.any { it == '#' }) 0 else 1
    else -> cache.getOrPut(this) {
        springs.substring(
            0,
            springs.indexOf('#').takeIf { it >= 0 }?.let { it + 1 } ?: springs.length).indices.filter {
            springs[it] == '?' || springs[it] == '#'
        }.filter {
            val group = springs.substring(it, minOf(it + groups.first(), springs.length))

            group.length == groups.first() && group.all { it == '?' || it == '#' } && springs.getOrNull(it + groups.first()) != '#'
        }.sumOf {
            Line(
                springs.substring(minOf(it + groups.first() + 1, springs.length)),
                groups.drop(1)
            ).countArrangements(cache)
        }
    }
}

fun part1() = input.sumOf { it.countArrangements() }
fun part2() = input.sumOf { it.unfold().countArrangements() }

fun main() {
    println("part 1 = ${part1()}")
    println("part 2 = ${part2()}")
}