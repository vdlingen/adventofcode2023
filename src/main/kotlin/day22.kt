package day22

import util.upOrDownTo

val input = util.readInput("day22.txt")
    .split("\n")
    .mapIndexed { i, brick -> brick.toBrick('A' + i) }

fun String.toCoord() = split(",").let { Coord3D(it[0].toInt(), it[1].toInt(), it[2].toInt()) }
fun String.toBrick(id: Char) = split("~").let { Brick(id, it[0].toCoord(), it[1].toCoord()) }

data class Coord3D(val x: Int, val y: Int, val z: Int)
data class Brick(val id: Char, val start: Coord3D, val end: Coord3D)

data class Coord2D(val x: Int, val y: Int)

val stacks = mutableMapOf<Coord2D, List<Brick?>>()
val supportedBy = mutableMapOf<Brick, List<Brick>>()

fun prepareData() = input.sortedBy { minOf(it.start.z, it.end.z) }.forEach { brick ->
    val coords = brick.start.x.upOrDownTo(brick.end.x).flatMap { x ->
        brick.start.y.upOrDownTo(brick.end.y).map { y -> Coord2D(x, y) }
    }

    val height = coords.maxOf { stacks[it]?.size ?: 0 }

    val add = brick.start.z.upOrDownTo(brick.end.z).map { brick }

    coords.forEach { coord ->
        val current = stacks[coord] ?: emptyList()

        val filler = List<Brick?>(height - current.size) { null }

        stacks[coord] = current + filler + add
    }

    if (height > 0) {
        supportedBy[brick] = coords.mapNotNull { stacks[it]?.get(height - 1) }.distinct()
    }
}

fun part1() = input.count { brick -> supportedBy.count { it.value.contains(brick) && it.value.size == 1 } == 0 }
fun part2() = input.sumOf { brick ->
    val removed = mutableListOf(brick)

    while (true) {
        val toRemove = supportedBy.filter { it.key !in removed && it.value.all { it in removed } }
        removed += toRemove.map { it.key }

        if (toRemove.isEmpty()) break
    }

    removed.size - 1
}

fun main() {
    prepareData()

    println("part 1 = ${part1()}")
    println("part 2 = ${part2()}")
}