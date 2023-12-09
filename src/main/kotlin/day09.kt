package day0

val input = util.readInput("day09.txt")
    .split("\n")
    .map { it.split(" ").map { it.toInt() } }

fun List<Int>.diffs() = (1..<size).map { get(it) - get(it-1)}
fun List<Int>.diffList() = buildList {
    var diff = this@diffList

    do {
        add(diff)
        diff = diff.diffs()
    } while ( diff.any { it != 0 })
}

fun List<Int>.next() = diffList().sumOf { it.last() }
fun List<Int>.previous() = diffList().reversed().map { it.first() }.reduce { a, b -> b - a }

fun part1() = input.map { it.next() }.sum()
fun part2() = input.map { it.previous() }.sum()

fun main() {
    println("part 1 = ${part1()}")
    println("part 2 = ${part2()}")
}