package day03

val input = util.readInput("day03.txt")
    .split("\n")

data class Coord(val x: Int, val y: Int) {
    val adjacent: List<Coord>
        get() = buildList {
            ((x - 1)..(x + 1)).forEach { x2 ->
                ((y - 1)..(y + 1)).forEach { y2 ->
                    if (x2 == x && y2 == y) {
                        return@forEach
                    }

                    if (x2 >= 0 && x2 < input.first().length && y2 >= 0 && y2 < input.size)
                        add(Coord(x2, y2))
                }
            }
        }

    val value = input[y][x]
}

fun Char.isSymbol() = !isDigit() && this != '.'

fun Coord.seesSymbol() = adjacent.any { it.value.isSymbol() }

data class PartNumber(
    val number: Int,
    val coords: List<Coord>
)

fun findPartNumbers() = buildList {
    input.indices.forEach { line ->
        var index = 0
        var number = 0
        var seesSymbol = false

        var coords = mutableListOf<Coord>()

        while (index < input[line].length) {
            val coord = Coord(index, line)

            if (coord.value.isDigit()) {
                number = number * 10 + coord.value.digitToInt()
                seesSymbol = seesSymbol || coord.seesSymbol()
                coords.add(coord)
            } else {
                if (seesSymbol) {
                    add(PartNumber(number, coords))
                }

                number = 0
                seesSymbol = false
                coords = mutableListOf()
            }

            index++
        }

        if (seesSymbol) {
            add(PartNumber(number, coords))
        }
    }

}

fun part1() = findPartNumbers().sumOf { it.number }

fun part2(): Int {
    val parts = findPartNumbers()
    var sum = 0

    (0 until input[0].length).forEach { x ->
        (0 until input.size).forEach { y ->
            val coord = Coord(x, y)

            if (coord.value == '*') {
                val adjacent = coord.adjacent

                parts.filter {
                    it.coords.any { it in adjacent }
                }.takeIf { it.size == 2 }
                    ?.let {
                        sum += it.first().number * it.last().number
                    }
            }
        }
    }

    return sum
}

fun main() {
    println("part 1 = ${part1()}")
    println("part 2 = ${part2()}")
}