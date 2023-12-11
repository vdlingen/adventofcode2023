@file:Suppress("unused")

package util

import kotlin.math.abs

fun readInput(filename: String) = object {}.javaClass.getResource("/$filename")!!.readText().trimEnd()

infix fun Int.upOrDownTo(to: Int): IntProgression {
    val step = if (this > to) -1 else 1
    return IntProgression.fromClosedRange(this, to, step)
}

data class Coord(val x: Int, val y: Int)

enum class Direction(val transform: (Coord) -> Coord) {
    North({ Coord(it.x, it.y - 1) }),
    NorthEast({ Coord(it.x + 1, it.y - 1) }),
    East({ Coord(it.x + 1, it.y) }),
    SouthEast({ Coord(it.x + 1, it.y + 1) }),
    South({ Coord(it.x, it.y + 1) }),
    SouthWest({ Coord(it.x - 1, it.y + 1) }),
    West({ Coord(it.x - 1, it.y) }),
    NorthWest({ Coord(it.x - 1, it.y - 1) }),

    Left(West.transform),
    Right(East.transform),
    Up(North.transform),
    Down(South.transform),
}

class Directions {
    companion object {
        val Orthogonal = listOf(Direction.North, Direction.East, Direction.West, Direction.South)
        val Diagonal = listOf(Direction.NorthEast, Direction.SouthEast, Direction.SouthWest, Direction.NorthWest)
        val All = Orthogonal + Diagonal
    }
}

class Grid(private val data: List<String>) {
    val width = data.minOf { it.length }
    val height = data.size

    fun row(y: Int) = (0..<width).map { x -> Coord(x, y) }
    fun column(x: Int) = (0..<height).map { y -> Coord(x, y) }

    val rows = (0..<height).map { y -> row(y) }
    val columns = (0..<width).map { x -> column(x) }
    val cells = rows.flatten()

    val Coord.char get() = data[y][x]
    val Coord.isValid get() = x in 0..<width && y in 0..<height

    fun Coord.neighbors(directions: List<Direction> = Directions.All) =
        directions.map { it.transform(this) }.filter { it.isValid }

    fun Coord.move(direction: Direction) = direction.transform(this).takeIf { it.isValid }

    fun Coord.left() = Direction.Left.transform(this).takeIf { it.isValid }
    fun Coord.right() = Direction.Right.transform(this).takeIf { it.isValid }
    fun Coord.up() = Direction.Up.transform(this).takeIf { it.isValid }
    fun Coord.down() = Direction.Down.transform(this).takeIf { it.isValid }

    fun Coord.distance(other: Coord) = abs(other.x - x) + abs(other.y - y)

    fun Coord.lineOfSight(direction: Direction) = buildList {
        var item = move(direction)
        while (item != null) {
            add(item)
            item = item.move(direction)
        }
    }
}

fun lcm(a: Long, b: Long) = maxOf(a, b).let { largest ->
    (largest..a * b step largest).find { it % a == 0L && it % b == 0L } ?: (a * b)
}

fun List<Int>.lcm() = drop(1).fold(first().toLong()) { a, b -> lcm(a, b.toLong()) }