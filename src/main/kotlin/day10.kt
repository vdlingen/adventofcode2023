package day10

import util.Coord
import util.Direction
import util.Directions
import util.Grid

val input = util.readInput("day10.txt")
    .split("\n")
    .let { Grid(it) }

enum class Pipe(val char: Char, val directions: List<Direction>) {
    Vertical('|', listOf(Direction.North, Direction.South)),
    Horizontal('-', listOf(Direction.East, Direction.West)),
    BendL('L', listOf(Direction.North, Direction.East)),
    BendJ('J', listOf(Direction.North, Direction.West)),
    Bend7('7', listOf(Direction.South, Direction.West)),
    BendF('F', listOf(Direction.South, Direction.East)),
}

fun Coord.pipe() = with(input) { Pipe.entries.find { it.char == char } }

fun part1() = with(input) {
    val start = cells.find { it.char == 'S' } ?: error("No start point")

    val distances = mutableMapOf<Coord, Int>()

    fun Coord.explore(step: Int) {
        if ((distances[this] ?: Int.MAX_VALUE) < step) return

        distances[this] = step

        pipe()?.let { pipe ->
            neighbors(pipe.directions).filter { it.pipe() != null }.forEach { it.explore(step + 1) }
        }
    }

    start.neighbors(Directions.Orthogonal)
        .filter { coord -> coord.pipe()?.takeIf { start in coord.neighbors(it.directions) } != null }
        .forEach { it.explore(1) }

    distances.values.max()
}

fun part2() = with(input) {
    val start = cells.find { it.char == 'S' } ?: error("No start point")
    val loop = mutableListOf(start)

    var next: Coord? = start.neighbors(Directions.Orthogonal)
        .first { coord -> coord.pipe()?.takeIf { start in coord.neighbors(it.directions) } != null }

    while (next != null) {
        loop.add(next)
        next = next.neighbors(next.pipe()?.directions ?: emptyList()).firstOrNull { !(it in loop) }
    }

    cells.filterNot { it in loop }.count { coord ->
        coord.lineOfSight(Direction.East)
            .let { if ('S' in it.map { it.char }) coord.lineOfSight(Direction.West).reversed() else it }
            .filter { it in loop && it.char != '-' }
            .map { it.char }
            .joinToString("")
            .replace("L7", "|")
            .replace("FJ", "|")
            .count { it == '|' }
            .let { it % 2 == 1 }
    }
}

fun main() {
    println("part 1 = ${part1()}")
    println("part 2 = ${part2()}")
}