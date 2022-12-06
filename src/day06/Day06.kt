package day06

import readInput

private const val DIRECTORY = "./day06"

fun main() {

    fun String.areCharactersUnique(): Boolean = all(hashSetOf<Char>()::add)

    fun findFirstOccurrence(packetSize: Int, input: String): Int =
        input.windowedSequence(size = packetSize, step = 1).indexOfFirst { it.areCharactersUnique() } + packetSize

    fun part1(input: List<String>): Int = findFirstOccurrence(packetSize = 4, input = input.first())

    fun part2(input: List<String>): Int = findFirstOccurrence(packetSize = 14, input = input.first())

    val input = readInput("$DIRECTORY/Day06")
    println(part1(input))
    println(part2(input))
}
