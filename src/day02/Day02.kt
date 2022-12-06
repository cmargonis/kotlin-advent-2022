package day02

import readInput

private const val DIRECTORY = "./day02"

fun main() {

    fun calculateScoreForRound(elfPlay: ElfHand, playerResponse: PlayerResponse): Int {
        val outcome = playerResponse.playAgainst(elfPlay)
        return outcome.score + playerResponse.score
    }

    fun toPair(it: String) = it.split(" ", ignoreCase = true).zipWithNext().first()

    fun part1(input: List<String>): Int = input
        .map { toPair(it) }
        .sumOf { calculateScoreForRound(ElfHand(it.first), PlayerResponse(it.second)) }

    fun getMissingPlayerResponse(elfHand: ElfHand, outcome: Outcome): ElfHand = when {
        elfHand.play == "A" && outcome == Outcome.DEFEAT -> ElfHand("C")
        elfHand.play == "A" && outcome == Outcome.DRAW -> ElfHand("A")
        elfHand.play == "A" && outcome == Outcome.VICTORY -> ElfHand("B")
        elfHand.play == "B" && outcome == Outcome.DEFEAT -> ElfHand("A")
        elfHand.play == "B" && outcome == Outcome.DRAW -> ElfHand("B")
        elfHand.play == "B" && outcome == Outcome.VICTORY -> ElfHand("C")
        elfHand.play == "C" && outcome == Outcome.DEFEAT -> ElfHand("B")
        elfHand.play == "C" && outcome == Outcome.DRAW -> ElfHand("C")
        elfHand.play == "C" && outcome == Outcome.VICTORY -> ElfHand("A")
        else -> error("Unknown match!")
    }

    fun calculateScoreForSecondRound(elfPlay: ElfHand, outcome: Outcome): Int =
        getMissingPlayerResponse(elfPlay, outcome).score + outcome.score

    fun part2(input: List<String>): Int = input
        .map { toPair(it) }
        .sumOf { calculateScoreForSecondRound(ElfHand(it.first), Outcome.fromEncryptedMessage(it.second)) }

    val input = readInput("${DIRECTORY}/Day02")
    println(part1(input))
    println(part2(input))
}

private enum class Outcome(val score: Int) {
    DEFEAT(0),
    DRAW(3),
    VICTORY(6)
    ;

    companion object {

        fun fromEncryptedMessage(s: String) = when (s) {
            "X" -> DEFEAT
            "Y" -> DRAW
            "Z" -> VICTORY
            else -> error("Unknown encrypted outcome!")
        }
    }
}

private data class ElfHand(val play: String) {

    val score: Int = when (play) {
        "A" -> 1
        "B" -> 2
        "C" -> 3
        else -> 0
    }
}

private data class PlayerResponse(val play: String) {

    val score: Int = when (play) {
        "X" -> 1
        "Y" -> 2
        "Z" -> 3
        else -> 0
    }

    fun playAgainst(elfHand: ElfHand): Outcome = when {
        elfHand.play == "A" && play == "X" -> Outcome.DRAW
        elfHand.play == "A" && play == "Y" -> Outcome.VICTORY
        elfHand.play == "A" && play == "Z" -> Outcome.DEFEAT
        elfHand.play == "B" && play == "X" -> Outcome.DEFEAT
        elfHand.play == "B" && play == "Y" -> Outcome.DRAW
        elfHand.play == "B" && play == "Z" -> Outcome.VICTORY
        elfHand.play == "C" && play == "X" -> Outcome.VICTORY
        elfHand.play == "C" && play == "Y" -> Outcome.DEFEAT
        elfHand.play == "C" && play == "Z" -> Outcome.DRAW
        else -> error("Unknown match.")
    }
}
