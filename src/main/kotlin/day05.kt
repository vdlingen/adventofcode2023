package day05

val input = util.readInput("day05.txt")
    .split("\n\n")

val seeds = input[0].substring(7).split(" ").map { it.toLong() }
val maps = input.subList(1, input.size).map {
    val lines = it.split("\n")

    (1..<lines.size).map {
        val (destination, source, length) = lines[it].split(" ").map { it.toLong() }

        Range(source, length, destination - source)
    }.sortedBy { it.start }
}

data class Range(val start: Long, val length: Long, val offset: Long = 0) {
    val end = start + length
}

fun part1() = seeds.minOf { seed ->
    maps.fold(seed) { value, mapping ->
        mapping.forEach {
            if (value >= it.start && value < it.end) return@fold value + it.offset
        }

        value
    }
}

fun part2() = seeds.chunked(2).map { Range(it.first(), it.last()) }.let { ranges ->
    maps.fold(ranges) { input, mapping ->
        input.flatMap { source ->
            val applicable = mapping.filter { it.start <= source.end && it.end >= source.start }
            if (applicable.isEmpty()) return@flatMap listOf(source)

            buildList {
                if (applicable.first().start > source.start) {
                    add(Range(source.start, applicable.first().start - source.start))
                }

                applicable.forEachIndexed { index, mapping ->
                    val start = maxOf(mapping.start, source.start)
                    val end = minOf(mapping.end, source.end)

                    add(Range(start + mapping.offset, end - start))

                    if (index < applicable.size - 1) {
                        val nextRange = applicable[index + 1]

                        if (end < nextRange.start) {
                            add(Range(end, nextRange.start - end))
                        }
                    }
                }

                if (applicable.last().end < source.end) {
                    add(Range(applicable.last().end, source.end - applicable.last().end))
                }
            }
        }
    }.minOf { it.start }
}

fun main() {
    println("part 1 = ${part1()}")
    println("part 2 = ${part2()}")
}