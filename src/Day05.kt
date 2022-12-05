import java.util.*

fun main() {
    fun part1(input: List<String>): String {
        val cargo = input.parse5()
        cargo.performOperations()
        return cargo.cratesOnTop.joinToString("")
    }

    fun part2(input: List<String>): String {
        val cargo = input.parse5()
        cargo.performOperations(true)
        return cargo.cratesOnTop.joinToString("")
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day05_test")
    check(part1(testInput) == "CMZ")
    check(part2(testInput) == "MCD")

    val input = readInput("Day05")
    println(part1(input))
    println(part2(input))
}

fun List<String>.parse5(): Cargo {
    val separator = indexOf("")
    val stacks = this[separator - 1].chunked(4).map { Stack<Char>() }
    take(separator - 1).reversed().forEach { line ->
        line.chunked(4).map { it.trim() }.forEachIndexed { index, slot ->
            slot.getOrNull(1)?.let { stacks[index].push(it) }
        }
    }
    val operations = drop(separator + 1).mapNotNull { line ->
        operationRegex.find(line)?.let {
            Operation(it.groupValues[1].toInt(), it.groupValues[2].toInt() - 1, it.groupValues[3].toInt() - 1)
        }
    }
    return Cargo(stacks, operations)
}

data class Cargo(val stacks: List<Stack<Char>>, val operations: List<Operation>) {
    val cratesOnTop: List<Char> get() = stacks.mapNotNull { it.peek() }
    fun performOperations(part2: Boolean = false) = operations.forEach { it.perform(stacks, part2) }
}

data class Operation(val amount: Int, val from: Int, val to: Int) {
    fun perform(stacks: List<Stack<Char>>, part2: Boolean = false) {
        val crates = buildList {
            repeat(amount) { add(stacks[from].pop()) }
            if (part2) reverse()
        }
        crates.forEach { stacks[to].push(it) }
    }
}

val operationRegex = Regex("move (\\d+) from (\\d+) to (\\d+)")