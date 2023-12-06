package day06

val input = util.readInput("day06.txt").split("\n").map {
    val (_, numbers) = it.split(":")
    numbers.trim().split("\\s+".toRegex()).map { it.toLong() }
}.let { it.first().indices.map { index -> Race(it.first()[index], it.last()[index]) } }

data class Race(val duration: Long, val record: Long)
fun Race.waysToBeat() = (1..<duration).count { speed -> (duration - speed) * speed > record }

fun part1() = input.map { it.waysToBeat() }.reduce { acc, i -> acc * i }
fun part2() = input.reduce { acc, race ->
    Race(
        "${acc.duration}${race.duration}".toLong(),
        "${acc.record}${race.record}".toLong()
    )
}.waysToBeat()

fun main() {
    println("part 1 = ${part1()}")
    println("part 2 = ${part2()}")
}