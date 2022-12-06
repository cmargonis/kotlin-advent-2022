package day04

import readInput

fun main() {

    fun IntRange.contains(other: IntRange): Boolean = first <= other.first && last >= other.last

    /**
     * Converts e.g. 22-34 to a [IntRange]
     */
    fun textToRange(rawRange: String): IntRange = rawRange
        .split("-")
        .map { it.toInt() }
        .zipWithNext { a, b -> a..b }
        .first()

    fun groupToRanges(it: String) = it.split(",").map { range -> textToRange(range) }

    fun part1(input: List<String>): Int = input.map {
        groupToRanges(it)
            .zipWithNext { a: IntRange, b: IntRange -> a.contains(b) or b.contains(a) }
            .first()
    }.count { it }

    fun part2(input: List<String>): Int = input.map {
        groupToRanges(it)
            .zipWithNext { a: IntRange, b: IntRange -> a.intersect(b).isNotEmpty() }
            .first()
    }.count { it }

    val input = readInput("Day04")
    println(part1(input))
    println(part2(input))
}
