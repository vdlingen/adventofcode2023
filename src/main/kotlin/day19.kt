package day19

val input = util.readInput("day19.txt")
    .split("\n\n")

val workflows = input.first().split("\n").associate {
    val result = "(\\w+)\\{(.+)}".toRegex().matchEntire(it) ?: error("boom")

    val name = result.groupValues[1]
    val rules = result.groupValues[2].split(",")

    val default = rules.last()

    val items = rules.dropLast(1).map {
        val v = it[0]
        val op = it[1]
        val (value, dest) = it.substring(2).split(":")

        Rule(v, op, value.toInt(), dest)
    }

    name to Workflow(items, default)
}

data class Workflow(val conditions: List<Rule>, val default: String)
data class Rule(val variable: Char, val operator: Char, val value: Int, val result: String)

fun Workflow.matches(part: Map<Char, Int>): Boolean =
    when (val result = conditions.firstOrNull { it.matches(part) }?.result ?: default) {
        "A" -> true
        "R" -> false
        else -> workflows[result]!!.matches(part)
    }

fun Rule.matches(part: Map<Char, Int>) = when (operator) {
    '>' -> part[variable]!! > value
    '<' -> part[variable]!! < value
    else -> false
}

val parts = input.last().split("\n").map {
    val items = it.substring(1, it.length - 1).split(",")

    buildMap {
        items.forEach {
            val (name, value) = it.split("=")
            put(name[0], value.toInt())
        }
    }
}

fun part1() = parts.filter { workflows["in"]!!.matches(it) }.sumOf { it.values.sum() }

fun Rule.splitRanges(ranges: Map<Char, IntRange>): Pair<Map<Char, IntRange>, Map<Char, IntRange>> {
    val matches = ranges.mapValues { (key, range) ->
        when {
            key == variable -> if (operator == '<') range.first..<value else (value + 1)..range.last
            else -> range
        }
    }

    val other = ranges.mapValues { (key, range) ->
        when {
            key == variable -> if (operator == '>') range.first..value else value..range.last
            else -> range
        }
    }

    return matches to other
}

fun Workflow.validRanges(ranges: Map<Char, IntRange>): List<Map<Char, IntRange>> =
    buildList {
        var other = ranges

        conditions.forEach {
            val (matches, remaining) = it.splitRanges(other)
            other = remaining

            when (it.result) {
                "A" -> add(matches)
                "R" -> {}
                else -> addAll(workflows[it.result]!!.validRanges(matches))
            }
        }

        when (default) {
            "A" -> add(other)
            "R" -> {}
            else -> addAll(workflows[default]!!.validRanges(other))
        }
    }

fun part2() = workflows["in"]!!.validRanges(
    mapOf(
        'x' to 1..4000,
        'm' to 1..4000,
        'a' to 1..4000,
        's' to 1..4000,
    )
).sumOf {
    it.values.map { 1 + it.last.toLong() - it.first.toLong() }.reduce { acc, v -> acc * v }
}

fun main() {
    println("part 1 = ${part1()}")
    println("part 2 = ${part2()}")
}