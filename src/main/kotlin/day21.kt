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
    val start = cells.find { it.char == 'S' } ?: error("could not find start")

    fun Coord.safeChar() = Coord(Math.floorMod(x, width), Math.floorMod(y, height)).char

    val visited = mutableSetOf<Coord>()

    var frontier = mutableSetOf<Coord>()
    frontier.add(start)

    var count = 0L
    var countOther = 0L

    val frontiers = Array(width) { 0 }
    val delta1 = Array(width) { 0 }
    val delta2 = Array(width) { 0 }

    var step = 0
    while (true) {
        val newFrontier = mutableSetOf<Coord>()

        frontier.forEach { p ->
            p.unvalidatedNeighbors(Directions.Orthogonal).forEach { n ->
                if (n.safeChar() != '#') {
                    if (visited.add(n)) newFrontier.add(n)
                }
            }
        }

        count = (countOther + newFrontier.size).also { countOther = count }

        val index = step % width
        if (step >= width) {
            val delta = newFrontier.size - frontiers[index]

            delta2[index] = delta - delta1[index]
            delta1[index] = delta
        }
        frontiers[index] = newFrontier.size

        frontier = newFrontier
        step++

        // iterate until the deltas are stable
        if (step >= 2 * width && delta2.all { it == 0 }) break
    }

    // interpolate the remaining steps
    (step..<26501365).forEach {
        (it % width).let { index ->
            frontiers[index] += delta1[index]
            count = (countOther + frontiers[index]).also { countOther = count }
        }
    }

    count
}

fun main() {
    println("part 1 = ${part1()}")
    println("part 2 = ${part2()}")
}
