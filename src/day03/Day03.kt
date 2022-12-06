package day03

import readInput

fun main() {
    fun toCommons(it: String): Set<Char> {
        val inter = it.chunked(it.length / 2)
        return inter.first().toSet().intersect(inter[1].toSet())
    }

    fun toPriority(c: Char): Int {
        val lowerCaseRange = 'a'..'z'
        val upperCaseRange = 'A'..'Z'
        return if (c in lowerCaseRange) lowerCaseRange.indexOf(c).plus(1)
        else upperCaseRange.indexOf(c).plus(27)
    }

    fun findCommonsBetweenTwo(acc: String, v: String) = if (acc.isEmpty()) v else acc.filter { v.contains(it) }

    fun findBadge(group: List<String>): Char = group
        .runningReduce(::findCommonsBetweenTwo)
        .last()
        .first()

    fun part1(input: List<String>): Int = input
        .fold(0) { l: Int, r: String -> toCommons(r).sumOf { toPriority(it) } + l }

    fun part2(input: List<String>): Int = input
        .asSequence()
        .chunked(3)
        .map { toPriority(findBadge(it)) }
        .sum()

    val input = readInput("Day03")
    println(part1(input))
    println(part2(input))
}
