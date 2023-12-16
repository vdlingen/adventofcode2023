package day16

import util.Coord
import util.Direction
import util.Grid
import java.util.LinkedList
import java.util.Queue

val input = util.readInput("day16.txt")
    .split("\n")
    .let { Grid(it) }

fun energized(start: Pair<Coord, Direction>) = with(input) {
    val visited = mutableSetOf<Pair<Coord, Direction>>()

    val queue: Queue<Pair<Coord, Direction>> = LinkedList()
    queue.add(start)

    fun Coord.addNextToQueue(direction: Direction) = move(direction)?.let {
        if (!visited.contains(it to direction)) queue.add(it to direction)
    }

    while (queue.isNotEmpty()) {
        val (position, direction) = queue.remove()

        when (position.char) {
            '/' -> when (direction) {
                Direction.Right -> Direction.Up
                Direction.Left -> Direction.Down
                Direction.Up -> Direction.Right
                Direction.Down -> Direction.Left
                else -> null
            }?.let { position.addNextToQueue(it) }

            '\\' -> when (direction) {
                Direction.Right -> Direction.Down
                Direction.Left -> Direction.Up
                Direction.Up -> Direction.Left
                Direction.Down -> Direction.Right
                else -> null
            }?.let { position.addNextToQueue(it) }

            '|' -> when (direction) {
                Direction.Right, Direction.Left -> {
                    position.addNextToQueue(Direction.Up)
                    position.addNextToQueue(Direction.Down)
                }

                else -> position.addNextToQueue(direction)
            }

            '-' -> when (direction) {
                Direction.Up, Direction.Down -> {
                    position.addNextToQueue(Direction.Left)
                    position.addNextToQueue(Direction.Right)
                }

                else -> position.addNextToQueue(direction)
            }

            else -> position.addNextToQueue(direction)
        }

        visited.add(position to direction)
    }

    visited.map { it.first }.distinct().count()
}

fun part1() = energized(Coord(0, 0) to Direction.Right)
fun part2() = with(input) { buildList {
    addAll(columns.first().map { it to Direction.Right })
    addAll(columns.last().map { it to Direction.Left})
    addAll(rows.first().map { it to Direction.Down })
    addAll(rows.last().map { it to Direction.Up })
}.maxOf { energized(it) } }

fun main() {
    println("part 1 = ${part1()}")
    println("part 2 = ${part2()}")
}