package day11

import util.Coord
import util.Grid
import util.upOrDownTo

val input = util.readInput("day11.txt")
    .split("\n")

val expanded = with(Grid(input)) {
    buildList {
        rows.forEachIndexed { rowIndex, row ->
            repeat(if (row.all { it.char == '.' }) 2 else 1) {
                add(
                    buildList {
                        columns.forEachIndexed { columnIndex, column ->
                            val cell = column.get(rowIndex)

                            repeat(if (columns[columnIndex].all { it.char == '.' }) 2 else 1) {
                                add(cell.char)
                            }
                        }
                    }.joinToString("")
                )
            }
        }
    }
}

val marked = input.map { row ->
    val isEmpty = row.all { it == '.' }

    row.mapIndexed { index, c ->
        when {
            input.map { it[index] }.all { it == '.' } -> '%'
            isEmpty -> '%'
            else -> c
        }
    }.joinToString("")
}

fun part1(): Int = with(Grid(expanded)) {
    val galaxies = cells.filter { it.char == '#' }
    val pairs = galaxies.flatMapIndexed { index, galaxy ->
        galaxies.drop(index + 1).map { galaxy to it }
    }

    pairs.sumOf { it.first.distance(it.second) }
}

fun part2(): Long = with(Grid(marked)) {
    val galaxies = cells.filter { it.char == '#' }
    val pairs = galaxies.flatMapIndexed { index, galaxy ->
        galaxies.drop(index + 1).map { galaxy to it }
    }

    pairs.sumOf { (first, second) ->
        var distance = 0L

        first.x.upOrDownTo(second.x).forEach { x ->
            if (x != first.x) distance += if (Coord(x, first.y).char == '%') 1000000 else 1
        }

        first.y.upOrDownTo(second.y).forEach { y ->
            if (y != first.y) distance += if (Coord(first.x, y).char == '%') 1000000 else 1
        }

        distance
    }
}

fun main() {
    println("part 1 = ${part1()}")
    println("part 2 = ${part2()}")
}