package day18

import util.Coord
import util.Direction
import kotlin.math.abs

val input = util.readInput("day18.txt")
    .split("\n")
    .map {
        val parts = it.split(" ")
        val direction = when (parts[0]) {
            "R" -> Direction.Right
            "L" -> Direction.Left
            "U" -> Direction.Up
            "D" -> Direction.Down
            else -> error("boom")
        }

        Dig(direction, parts[1].toInt(), parts[2].substring(2, parts[2].length - 1))
    }

data class Dig(val direction: Direction, val distance: Int, val color: String)

fun List<Coord>.shoelace(): Long {
    var leftSum = 0L
    var rightSum = 0L

    indices.forEach { i ->
        val j = (i + 1) % size
        leftSum += get(i).x.toLong() * get(j).y.toLong()
        rightSum += get(j).x.toLong() * get(i).y.toLong()
    }

    return abs(leftSum - rightSum) / 2
}

fun List<Dig>.trenchSize(): Long {
    val points = mutableListOf<Coord>()

    var current = Coord(0, 0)
    forEach {
        current = when (it.direction) {
            Direction.Right -> current.copy(x = current.x + it.distance)
            Direction.Left -> current.copy(x = current.x - it.distance)
            Direction.Down -> current.copy(y = current.y + it.distance)
            Direction.Up -> current.copy(y = current.y - it.distance)
            else -> current
        }

        points.add(current)
    }

    return points.shoelace() + sumOf { it.distance } / 2 + 1
}

fun part1() = input.trenchSize()
fun part2() = input.map {
    Dig(
        when (it.color.last()) {
            '0' -> Direction.Right
            '1' -> Direction.Down
            '2' -> Direction.Left
            '3' -> Direction.Up
            else -> error("boom")
        },
        it.color.substring(0, 5).toInt(16),
        ""
    )
}.trenchSize()

fun main() {
    println("part 1 = ${part1()}")
    println("part 2 = ${part2()}")
}