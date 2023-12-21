package day21

import util.Coord
import util.Directions
import util.Grid

val input = util.readInput("day21.txt")
    .split("\n").let { Grid(it) }

fun part1() = with(input) {
    var plots = setOf(cells.find { it.char == 'S' } ?: error("start position not found"))

    val options = mutableMapOf<Coord, Set<Coord>>()

    repeat(64) {
        plots = plots.flatMap {
            options.getOrPut(it) { it.neighbors(Directions.Orthogonal).filter { it.char != '#' }.toSet() }
        }.toSet()
    }

    plots.size
}

fun part2() = with(input) {
    0L
}

fun main() {
    println("part 1 = ${part1()}")
    println("part 2 = ${part2()}")
}