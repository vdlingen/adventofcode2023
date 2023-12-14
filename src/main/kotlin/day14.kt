package day14

import util.Grid

val input = util.readInput("day14.txt")
    .split("\n")

fun Grid.north(): Grid {
    val updated = columns.map {
        it.map { it.char }.joinToString("").split("#").map { it.toList().sorted().reversed().joinToString("") }
            .joinToString("#")
    }

    return Grid(updated.first().indices.map { index -> updated.map { it[index] }.joinToString("") })
}

fun Grid.south(): Grid {
    val updated = columns.map {
        it.map { it.char }.joinToString("").split("#").map { it.toList().sorted().joinToString("") }
            .joinToString("#")
    }

    return Grid(updated.first().indices.map { index -> updated.map { it[index] }.joinToString("") })
}

fun Grid.east(): Grid {
    val updated = rows.map {
        it.map { it.char }.joinToString("").split("#").map { it.toList().sorted().joinToString("") }
            .joinToString("#")
    }

    return Grid(updated)
}

fun Grid.west(): Grid {
    val updated = rows.map {
        it.map { it.char }.joinToString("").split("#").map { it.toList().sorted().reversed().joinToString("") }
            .joinToString("#")
    }

    return Grid(updated)
}

fun Grid.cycle() = north().west().south().east()

fun Grid.load() = columns.sumOf { column ->
    column.mapIndexed { index, coord -> if (coord.char == 'O') column.size - index else 0 }.sum()
}

fun part1() = Grid(input).north().load()

fun part2(): Int {
    var grid = Grid(input)

    val seen = mutableListOf(grid)
    val loads = mutableListOf(grid.load())

    var index = 0
    val repeat = 1000000000

    while (index < repeat) {
        index++

        grid = grid.cycle()

        if (grid in seen) {
            println("detected loop in run $index - ${seen.indexOf(grid)}")

            break
        }
        seen.add(grid)
        loads.add(grid.load())
    }

    val loopStart = seen.indexOf(grid)
    val loop = loads.drop(loopStart)

    return loop.get((repeat - loopStart) % loop.size)
}

fun main() {
    println("part 1 = ${part1()}")
    println("part 2 = ${part2()}")
}