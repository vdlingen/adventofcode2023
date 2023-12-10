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

val start = with(input) { cells.find { it.char == 'S' } ?: error("No start point") }
val loop = with(input) {
    buildList {
        add(start)

        var next: Coord? = start.neighbors(Directions.Orthogonal).first { coord ->
            coord.pipe()?.takeIf { start in coord.neighbors(it.directions) } != null
        }

        while (next != null) {
            add(next)
            next = next.neighbors(next.pipe()?.directions ?: emptyList()).firstOrNull { !(it in this) }
        }
    }
}

fun part1() = loop.size / 2

fun part2() = with(input) {
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