fun main() {

    fun getStack(input: List<String>): Pair<Int, Map<Int, ArrayDeque<String>>> {
        var stackIndex = 0
        val stackRows: List<List<String>> = input.takeWhile { it.isNotBlank() }.mapIndexed { runningIndex, row ->
            stackIndex = runningIndex
            row.windowed(size = 3, step = 4, partialWindows = true)
        }

        val stack: MutableMap<Int, MutableList<String>> = mutableMapOf<Int, MutableList<String>>()
        val stackKeys: List<Int> = stackRows[stackIndex].map { it.trim(' ', '\"').toInt() }
        stackRows.dropLast(1).forEach { row: List<String> ->
            row.forEachIndexed { index, item ->
                val itemsAtStack: MutableList<String> = stack[stackKeys[index]] ?: mutableListOf()
                itemsAtStack.add(item)
                stack[stackKeys[index]] = itemsAtStack
            }
        }
        stack.forEach { entry -> entry.value.removeIf { it.isBlank() } }
        return Pair(stackIndex, stack.toSortedMap(compareByDescending { it }).mapValues { entry ->
            val trimmed = entry.value.map { it.trim('[', ']', ' ') }
            ArrayDeque(trimmed)
        })
    }

    fun getInstructions(rawInstructions: List<String>): List<Instruction> = rawInstructions.map {
        val instruction = it.split(" ")
        Instruction(
            quantity = instruction[1].toInt(),
            origin = instruction[3].toInt(),
            destination = instruction[5].toInt()
        )
    }

    fun Map<Int, ArrayDeque<String>>.move(from: Int, to: Int) {
        if (isEmpty()) return
        val removed = this[from]?.removeFirst()
        this[to]?.addFirst(removed!!)
    }

    fun Map<Int, ArrayDeque<String>>.moveMany(times: Int, from: Int, to: Int) {
        if (isEmpty()) return
        val tempQueue: ArrayDeque<String> = ArrayDeque(initialCapacity = times)
        repeat(times) {
            tempQueue.addFirst(this[from]!!.removeFirst())
        }
        repeat(times) {
            this[to]?.addFirst(tempQueue.removeFirst())
        }
    }

    fun Map<Int, ArrayDeque<String>>.topOfTheStack(): String = asIterable()
        .runningFold("") { _, entry -> entry.value.first() }
        .filterNot { it.isEmpty() }
        .joinToString(separator = "")

    fun part1(input: List<String>): String {
        val (indexOfInstructions, stack) = getStack(input)
        val instructions = getInstructions(input.subList(fromIndex = indexOfInstructions + 2, toIndex = input.size))
        instructions.forEach { instruction ->
            repeat(instruction.quantity) {
                stack.move(from = instruction.origin, to = instruction.destination)
            }
        }
        return stack.topOfTheStack().reversed()
    }

    fun part2(input: List<String>): String {
        val (indexOfInstructions, stack) = getStack(input)
        val instructions = getInstructions(input.subList(fromIndex = indexOfInstructions + 2, toIndex = input.size))
        instructions.forEach { instruction ->
            stack.moveMany(times = instruction.quantity, from = instruction.origin, to = instruction.destination)
        }
        return stack.topOfTheStack().reversed()
    }

    val input = readInput("Day05")
    println(part1(input))
    println(part2(input))
}

data class Instruction(val quantity: Int, val origin: Int, val destination: Int)
