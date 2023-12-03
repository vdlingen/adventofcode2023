@file:Suppress("unused")
package util

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

    fun rows() = (0..<height).map { y -> row(y) }
    fun columns() = (0..<width).map { x -> column(x) }
    fun cells() = rows().flatten()

    fun Coord.value() = data[y][x]
    fun Coord.valid() = x in 0..<width && y in 0..<height

    fun Coord.neighbors(directions: List<Direction> = Directions.All) =
        directions.map { it.transform(this) }.filter { it.valid() }

    fun Coord.left() = Direction.Left.transform(this).takeIf { it.valid() }
    fun Coord.right() = Direction.Right.transform(this).takeIf { it.valid() }
    fun Coord.up() = Direction.Up.transform(this).takeIf { it.valid() }
    fun Coord.down() = Direction.Down.transform(this).takeIf { it.valid() }
}