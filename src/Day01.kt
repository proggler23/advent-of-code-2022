private fun List<String>.partitionBy(separator: String): List<List<String>> {
    return buildList {
        val list = mutableListOf<String>()
        for (value in this@partitionBy) {
            if (value == separator) {
                add(list.toList())
                list.clear()
            } else {
                list.add(value)
            }
        }
        add(list.toList())
    }.filter { it.isNotEmpty() }
}

fun main() {
    fun part1(input: List<String>): Int {
        return input.partitionBy("").maxOf {
            it.sumOf { line -> line.toInt() }
        }
    }

    fun part2(input: List<String>): Int {
        return input.partitionBy("").map {
            it.sumOf { line -> line.toInt() }
        }.sortedDescending().take(3).sum()
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day01_test")
    check(part1(testInput) == 24000)
    check(part2(testInput) == 45000)

    val input = readInput("Day01")
    println(part1(input))
    println(part2(input))
}
