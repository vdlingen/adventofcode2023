package day02

val input = util.readInput("day02.txt").split("\n")
val games = input.mapNotNull { line ->
    "Game (\\d+): (.+)".toRegex().matchEntire(line)?.let { result ->
        Game(result.groupValues[1].toInt(), result.groupValues[2].split("; ").map {
            it.split(", ").map {
                val (amount, color) = it.split(" ")
                Reveal(amount.toInt(), color)
            }
        })
    }
}

data class Reveal(val amount: Int, val color: String)
data class Game(val index: Int, val revealed: List<List<Reveal>>)

val colorCount1 = mapOf("red" to 12, "green" to 13, "blue" to 14)

fun part1() = games.filter { game ->
    game.revealed.all { reveals ->
        reveals.groupBy { it.color }
            .mapValues { (_, list) -> list.sumOf { it.amount } }
            .all { (color, count) -> count <= colorCount1.getOrDefault(color, 0) }
    }
}.sumOf { it.index }

fun part2() = games.map { game ->
    game.revealed.flatten().fold(mapOf<String, Int>()) { acc, reveal ->
        acc + mapOf(reveal.color to maxOf(reveal.amount, acc.getOrDefault(reveal.color, 0)))
    }
}.sumOf { it.values.reduce { acc, i -> acc * i } }

fun main() {
    println("part 1 = ${part1()}")
    println("part 2 = ${part2()}")
}