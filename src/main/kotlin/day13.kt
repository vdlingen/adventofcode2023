package day13

import util.Grid

val input = util.readInput("day13.txt")
    .split("\n\n")
    .map { Grid(it.split("\n")) }

fun Grid.findMirror(maxSmudges: Int = 0): Int {
    val horizontal = columns.firstOrNull { column ->
        var smudges = column.count { it.char != it.right()?.char }

        var left = column
        var right = column.mapNotNull { it.right() }

        while (smudges <= maxSmudges && left.isNotEmpty() && right.isNotEmpty()) {
            left = left.mapNotNull { it.left() }
            right = right.mapNotNull { it.right() }

            if (left.size == right.size) {
                left.indices.forEach {
                    if (left[it].char != right[it].char) smudges++
                }
            }
        }

        smudges == maxSmudges
    }?.let { it[0].x + 1 }

    val vertical = rows.firstOrNull { row ->
        var smudges = row.count { it.char != it.down()?.char }

        var top = row
        var bottom = row.mapNotNull { it.down() }

        while (smudges <= maxSmudges && top.isNotEmpty() && bottom.isNotEmpty()) {
            top = top.mapNotNull { it.up() }
            bottom = bottom.mapNotNull { it.down() }

            if (top.size == bottom.size) {
                top.indices.forEach {
                    if (top[it].char != bottom[it].char) smudges++
                }
            }
        }

        smudges == maxSmudges
    }?.let { it[0].y + 1 }

    return (horizontal ?: 0) + (vertical?.let { it * 100 } ?: 0)
}

fun part1() = input.sumOf {it.findMirror() }
fun part2() = input.sumOf { it.findMirror(1) }

fun main() {
    println("part 1 = ${part1()}")
    println("part 2 = ${part2()}")
}