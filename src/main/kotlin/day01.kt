package day01

val input = util.readInput("day01.txt")
    .split("\n")

fun String.replaceTextDigits() = mapIndexed { index, char ->
    when {
        substring(index).startsWith("one") -> '1'
        substring(index).startsWith("two") -> '2'
        substring(index).startsWith("three") -> '3'
        substring(index).startsWith("four") -> '4'
        substring(index).startsWith("five") -> '5'
        substring(index).startsWith("six") -> '6'
        substring(index).startsWith("seven") -> '7'
        substring(index).startsWith("eight") -> '8'
        substring(index).startsWith("nine") -> '9'
        else -> char
    }
}.toString()

fun String.extractFirstAndLastDigit() =
    filter { it.isDigit() }.let { "${it.first()}${it.last()}"}.toInt()

fun part1() = input.map { it.extractFirstAndLastDigit() }.sum()
fun part2() = input.map { it.replaceTextDigits().extractFirstAndLastDigit() }.sum()

fun main() {
    println("part 1 = ${part1()}")
    println("part 2 = ${part2()}")
}