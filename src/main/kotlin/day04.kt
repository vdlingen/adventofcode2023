package day04

val input = util.readInput("day04.txt").split("\n")
    .mapNotNull { line ->
        "Card\\s+(\\d+): (.*)\\|(.*)".toRegex().matchEntire(line)?.let { result ->
            Card(
                result.groupValues[1].toInt(),
                result.groupValues[2].trim().split("\\s+".toRegex()).map { it.toInt() },
                result.groupValues[3].trim().split("\\s+".toRegex()).map { it.toInt() })
        }
    }

data class Card(val index: Int, val numbers: List<Int>, val winning: List<Int>)

fun part1() = input.sumOf { card ->
    fun points(count: Int) = if (count == 0) 0 else (1..count).reduce { acc, _ -> acc * 2 }

    points(card.numbers.count { it in card.winning })
}

fun part2(): Int {
    val copies = mutableMapOf<Int, Int>()

    input.forEach { card ->
        val matching = card.numbers.count { it in card.winning }

        repeat(1 + copies.getOrDefault(card.index, 0)) {
            (card.index + 1..card.index + matching).forEach { index ->
                copies[index] = copies.getOrDefault(index, 0) + 1
            }
        }
    }

    return copies.values.sum() + input.size
}

fun main() {
    println("part 1 = ${part1()}")
    println("part 2 = ${part2()}")
}