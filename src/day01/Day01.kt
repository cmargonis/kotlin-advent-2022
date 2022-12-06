package day01

import readInput

fun main() {
    fun getElves(input: List<String>): List<Elf> {
        var calories = 0
        val elves = mutableListOf<Elf>()
        input.forEach {
            if (it.isNotBlank()) {
                calories += it.toInt()
            } else {
                elves.add(Elf(calories))
                calories = 0
            }
        }
        return elves
    }

    fun part1(input: List<String>): Int {
        val elves = getElves(input)
        return elves.maxBy { it.totalCalories }.totalCalories
    }

    fun part2(input: List<String>): Int = getElves(input)
        .asSequence()
        .sortedByDescending { it.totalCalories }
        .take(3)
        .sumOf { it.totalCalories }

    val input = readInput("Day01")
    println(part1(input))
    println(part2(input))
}

data class Elf(val totalCalories: Int)
