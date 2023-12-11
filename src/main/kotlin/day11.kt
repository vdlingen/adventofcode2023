package day11

import util.Grid
import util.upOrDownTo

val input = util.readInput("day11.txt")
    .split("\n")
    .let { Grid(it) }

val expandedRows = with(input) { rows.indices.filter { rows[it].all { it.char == '.' } } }
val expandedColumns = with(input) { columns.indices.filter { columns[it].all { it.char == '.' } } }

fun distanceSum(expand: Long) = with(input) {
    val galaxies = cells.filter { it.char == '#' }
    val pairs = galaxies.flatMapIndexed { index, galaxy -> galaxies.drop(index + 1).map { galaxy to it } }

    pairs.sumOf { (first, second) ->
        first.x.upOrDownTo(second.x).drop(1)
            .sumOf { x -> if (x in expandedColumns) expand else 1 } +
                first.y.upOrDownTo(second.y).drop(1)
                    .sumOf { y -> if (y in expandedRows) expand else 1 }
    }
}

fun part1() = distanceSum(2)
fun part2() = distanceSum(1000000)

fun main() {
    println("part 1 = ${part1()}")
    println("part 2 = ${part2()}")
}