package day15

val input = util.readInput("day15.txt").split(",")

fun String.hash() = fold(0) { acc, c -> ((acc + c.code) * 17) % 256 }

fun part1() = input.sumOf { it.hash() }
fun part2(): Int {
    val boxes = mutableMapOf<Int, List<Pair<String, Int>>>()

    input.forEach {
        val label = it.filter { it.isLetter() }
        val box = label.hash()

        val list = boxes[box] ?: emptyList()

        if (it.contains('-')) {
            boxes[box] = list.filterNot { it.first == label }
        } else {
            val new = Pair(label, it.last().digitToInt())

            if (list.find { it.first == label } != null) {
                boxes[box] = list.map { if (it.first == label) new else it }
            } else {
                boxes[box] = list + new
            }
        }
    }

    return boxes.entries.flatMap { (box, list) ->
        list.mapIndexed { index, entry -> (box + 1) * (index + 1) * entry.second }
    }.sum()
}

fun main() {
    println("part 1 = ${part1()}")
    println("part 2 = ${part2()}")
}