package day08

import util.lcm

val input = util.readInput("day08.txt").split("\n\n")

val steps = input[0]
val nodes = input[1].split("\n").map {
    val result = "(\\w+) = \\((\\w+), (\\w+)\\)".toRegex().matchEntire(it)!!

    Node(result.groupValues[1], result.groupValues[2], result.groupValues[3])
}.associateBy { it.name }

data class Node(val name: String, val left: String, val right: String)

fun countSteps(start: String, endCondition: (String) -> Boolean): Int {
    var index = 0
    var node = start

    while (!endCondition(node)) {
        node = when (steps[index.mod(steps.length)]) {
            'L' -> nodes[node]!!.left
            'R' -> nodes[node]!!.right
            else -> error("boom")
        }

        index++
    }

    return index
}

fun part1() = countSteps("AAA") { it == "ZZZ" }
fun part2() = nodes.keys.filter { it.endsWith("A") }
    .map { countSteps(it) { it.endsWith("Z") } }.lcm()

fun main() {
    println("part 1 = ${part1()}")
    println("part 2 = ${part2()}")
}