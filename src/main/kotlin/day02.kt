package day02

val input = util.readInput("day02.txt")
    .split("\n")
    .map {
        val (game, revealed) = it.split(": ")
        val (_, index) = game.split(" ")

        val sets = revealed.split("; ")

        Game(index.toInt(), sets.map {
            val entries = it.split(", ")

            entries.map {
                val (amount, color) = it.split(" ")
                Reveal(amount.toInt(), color)
            }
        })
    }

data class Reveal(
    val amount: Int,
    val color: String,
)

data class Game(
    val index: Int,
    val revealed: List<List<Reveal>>,
)

fun part1() = input.filter {
    it.revealed.all {
        val counts = it.groupBy { it.color }.mapValues { (_, list) -> list.sumOf { it.amount } }

        counts.getOrDefault("red", 0) <= 12 &&
                counts.getOrDefault("green", 0) <= 13 &&
                counts.getOrDefault("blue", 0) <= 14
    }
}.sumOf { it.index }

fun part2() = input.map { game ->
    buildMap<String, Int> {
        game.revealed.forEach {  set ->
            set.forEach {
               put(it.color, maxOf(it.amount, get(it.color) ?: 0))
            }
        }
    }
}.sumOf { it.values.reduce { acc, i -> acc * i } }

fun main() {
    println("part 1 = ${part1()}")
    println("part 2 = ${part2()}")
}