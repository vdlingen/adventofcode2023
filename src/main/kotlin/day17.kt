package day17

import util.Coord
import util.Direction
import util.Grid
import java.util.LinkedList

val input = util.readInput("day17.txt")
    .split("\n")
    .let { Grid(it) }

data class State(val position: Coord, val direction: Direction, val line: Int)

fun part1() = with(input) {
    fun Coord.loss(): Int = char.digitToInt()

    fun State.nextDirections() = (if (line < 3) listOf(direction) else emptyList()) + when (direction) {
        Direction.Down, Direction.Up -> listOf(Direction.Right, Direction.Left)
        Direction.Right, Direction.Left -> listOf(Direction.Down, Direction.Up)
        else -> emptyList()
    }

    fun State.nextStates() = nextDirections().mapNotNull { nextDirection ->
        position.move(nextDirection)?.let { nextPosition ->
            State(nextPosition, nextDirection, if (nextDirection == direction) line + 1 else 1)
        }
    }

    val heatLoss = mutableMapOf<State, Int>()

    val start = Coord(0, 0)
    val destination = rows.last().last()

    heatLoss.put(State(start, Direction.Right, 0), 0)

    val queue = LinkedList<State>()
    queue.add(State(start, Direction.Right, 0))

    while (queue.isNotEmpty()) {
        val state = queue.remove()
        val loss = heatLoss[state] ?: Int.MAX_VALUE

        state.nextStates().forEach { nextState ->
            val nextLoss = loss + nextState.position.loss()

            if (nextLoss < heatLoss.getOrDefault(nextState, Int.MAX_VALUE)) {
                heatLoss.put(nextState, nextLoss)

                if (nextState.position != destination)
                    queue.add(nextState)
            }
        }
    }

    heatLoss.filter { it.key.position == destination }.minOf { it.value }
}

fun part2() = with(input) {
    fun Coord.loss(): Int = char.digitToInt()

    fun State.nextDirections() = (if (line < 10) listOf(direction) else emptyList()) +
            if (line >= 4) when (direction) {
                Direction.Down, Direction.Up -> listOf(Direction.Right, Direction.Left)
                Direction.Right, Direction.Left -> listOf(Direction.Down, Direction.Up)
                else -> emptyList()
            } else emptyList()

    fun State.nextStates() = nextDirections().mapNotNull { nextDirection ->
        position.move(nextDirection)?.let { nextPosition ->
            State(nextPosition, nextDirection, if (nextDirection == direction) line + 1 else 1)
        }
    }

    val heatLoss = mutableMapOf<State, Int>()

    val start = Coord(0, 0)
    val destination = rows.last().last()

    heatLoss.put(State(start, Direction.Right, 0), 0)
    heatLoss.put(State(start, Direction.Down, 0), 0)

    val queue = LinkedList<State>()
    queue.add(State(start, Direction.Right, 0))
    queue.add(State(start, Direction.Down, 0))

    while (queue.isNotEmpty()) {
        val state = queue.remove()
        val loss = heatLoss[state] ?: Int.MAX_VALUE

        state.nextStates().forEach { nextState ->
            val nextLoss = loss + nextState.position.loss()

            if (nextLoss < heatLoss.getOrDefault(nextState, Int.MAX_VALUE)) {
                heatLoss[nextState] = nextLoss

                if (nextState.position != destination)
                    queue.add(nextState)
            }
        }
    }

    heatLoss.filter { it.key.position == destination }.minOf { it.value }
}

fun main() {
    println("part 1 = ${part1()}")
    println("part 2 = ${part2()}")
}