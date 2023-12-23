package day23

import util.Coord
import util.Directions
import util.Grid

val input = util.readInput("day23.txt")
    .split("\n").let { Grid(it) }

fun part1() = with(input) {
    val start = rows.first().find { it.char == '.' } ?: error("no start")
    val end = rows.last().find { it.char == '.' } ?: error("no end")

    fun Coord.longestPath(visited: List<Coord> = emptyList()): List<Coord>? {
        val paths = neighbors(directions = Directions.Orthogonal)
            .filter {
                it !in visited && it != start &&
                        when (it.char) {
                            '.' -> true
                            '>' -> it.x > x
                            '<' -> it.x < x
                            'v' -> it.y > y
                            '^' -> it.y < y
                            else -> false
                        }
            }.mapNotNull { if (it == end) listOf(it) else it.longestPath(visited + it) }

        return paths.maxByOrNull { it.size }?.let { listOf(this) + it }
    }

    start.longestPath()!!.size - 1
}

fun part2() = with(input) {
    val start = rows.first().find { it.char == '.' } ?: error("no start")
    val end = rows.last().find { it.char == '.' } ?: error("no end")

    val nodes = cells.filter {
        it.char != '#' && it.neighbors(Directions.Orthogonal).count { it.char != '#' } > 2
    } + start + end

    val distances = mutableMapOf<Coord, MutableMap<Coord, Int>>()
    nodes.forEach { node ->
        node.neighbors(Directions.Orthogonal).filter { it.char != '#' }.forEach { next ->
            val path = mutableListOf(next)
            var current: Coord? = next

            while (current != null && current !in nodes) {
                current = current.neighbors(Directions.Orthogonal).filter { it.char != '#' }.firstOrNull { it !in path && it != node }
                    ?.also { path += it }
            }

            if (current != null)
                distances[node] = (distances[node] ?: mutableMapOf()).also { it.put(current, path.size) }
        }
    }

    fun Coord.longestPath(visited: List<Coord> = listOf(this), steps: Int = 0): Int? =
        distances[this]?.mapNotNull { (destination, distance) ->
            destination.takeIf { it !in visited }?.let { node ->
                if (node == end)
                    steps + distance
                else
                    node.longestPath(visited + node, steps + distance)
            }
        }?.maxOrNull()

    start.longestPath()
}

fun main() {
    println("part 1 = ${part1()}")
    println("part 2 = ${part2()}")
}