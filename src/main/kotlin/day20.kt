package day20

import util.lcm
import java.util.LinkedList

val input = util.readInput("day20.txt")
    .split("\n")
    .map {
        val (name, destinations) = it.split(" -> ")

        Item(
            name.first().takeUnless { it.isLetter() },
            if (name.first().isLetter()) name else name.drop(1),
            destinations.split(", ")
        )
    }.associateBy { it.name }

data class Item(val type: Char?, val name: String, val destinations: List<String>)

fun part1(): Int {
    var high = 0
    var low = 0

    val state = mutableMapOf<String, Boolean>()
    val nodeInputs = input.mapValues { (key, _) ->
        input.values.filter { it.destinations.contains(key) }.map { it.name }
    }

    repeat(1000) {
        val queue = LinkedList<Pair<Boolean, String>>()
        queue.add(false to "broadcaster")

        while (queue.isNotEmpty()) {
            val (signal, destination) = queue.remove()

            if (signal) high++ else low++

            input[destination]?.let { rule ->
                when (rule.type) {
                    '%' -> {
                        if (!signal) {
                            val output = !(state[destination] ?: false)
                            state[destination] = output

                            rule.destinations.forEach { queue.add(output to it) }
                        }
                    }

                    '&' -> {
                        val output = (nodeInputs[destination]?.all { state[it] ?: false } ?: false).not()
                        state[destination] = output

                        rule.destinations.forEach { queue.add(output to it) }
                    }

                    else -> rule.destinations.forEach { queue.add(signal to it) }
                }
            }
        }
    }

    println("high = $high, low = $low")

    return high * low
}

fun part2(): Long {
    var buttonPress = 0

    val state = mutableMapOf<String, Boolean>()
    val nodeInputs = input.mapValues { (key, _) ->
        input.values.filter { it.destinations.contains(key) }.map { it.name }
    }

    val lh = mutableListOf<Int>()
    val fk = mutableListOf<Int>()
    val ff = mutableListOf<Int>()
    val mm = mutableListOf<Int>()

    repeat(10000) {
        val queue = LinkedList<Pair<Boolean, String>>()
        queue.add(false to "broadcaster")

        buttonPress++

        while (queue.isNotEmpty()) {
            val (signal, destination) = queue.remove()

            input[destination]?.let { rule ->
                when (rule.type) {
                    '%' -> {
                        if (!signal) {
                            val output = !(state[destination] ?: false)
                            state[destination] = output

                            rule.destinations.forEach { queue.add(output to it) }
                        }
                    }

                    '&' -> {
                        val output = (nodeInputs[destination]?.all { state[it] ?: false } ?: false).not()
                        state[destination] = output

                        if (output) {
                            when (destination) {
                                "lh" -> lh.add(buttonPress)
                                "fk" -> fk.add(buttonPress)
                                "ff" -> ff.add(buttonPress)
                                "mm" -> mm.add(buttonPress)
                            }
                        }

                        rule.destinations.forEach { queue.add(output to it) }
                    }

                    else -> rule.destinations.forEach { queue.add(signal to it) }
                }
            }
        }
    }

    return listOf(lh.first(), fk.first(), ff.first(), mm.first()).lcm()
}

fun main() {
    println("part 1 = ${part1()}")
    println("part 2 = ${part2()}")
}