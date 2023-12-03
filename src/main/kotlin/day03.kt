package day03

import util.Coord
import util.Grid

val input = util.readInput("day03.txt").split("\n")
val grid = Grid(input)

data class PartNumber(
    val number: Int,
    val coords: List<Coord>
)

val partNumbers = with(grid) {
    fun Char.isSymbol() = !isDigit() && this != '.'
    fun Coord.seesSymbol() = neighbors().any { it.value().isSymbol() }
    fun Coord.isFirstDigit() = value().isDigit() && left()?.value()?.isDigit() != true

    buildList {
        rows().forEach { row ->
            row.filter { it.isFirstDigit() }.map { start ->
                row.subList(start.x, row.size)
                    .takeWhile { it.value().isDigit() }
                    .takeIf { it.any { it.seesSymbol() } }
                    ?.let { coords ->
                        val number = coords.map { it.value() }.joinToString(separator = "").toInt()
                        add(PartNumber(number, coords))
                    }
            }
        }
    }
}

fun part1() = partNumbers.sumOf { it.number }

fun part2() = with(grid) {
    cells().filter { it.value() == '*' }
        .map { gear ->
            val neighbors = gear.neighbors()
            partNumbers.filter { it.coords.any { it in neighbors } }
        }.filter { it.size == 2 }
        .sumOf { it.first().number * it.last().number }
}

fun main() {
    println("part 1 = ${part1()}")
    println("part 2 = ${part2()}")
}