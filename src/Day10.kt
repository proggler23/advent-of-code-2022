import kotlin.math.abs

fun main() {
    fun part1(input: List<String>): Long {
        val cycles = input.cycles()
        val checkMarks = listOf(IndexedValue(19, cycles[19])) +
                cycles.asSequence().withIndex().drop(20).take(200).windowed(size = 40, step = 40).map { it.last() }
        return checkMarks.sumOf { v ->
            (1 + v.index) * v.value
        }
    }

    fun part2(input: List<String>) {
        val cycles = input.cycles()
        repeat(6) {y ->
            repeat(40) { x->
                if (abs(cycles[y*40+x] - x) <= 1)                    print('#')
                 else                     print('.')
            }
            println()
        }
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day10_test")
    check(part1(testInput) == 13140L)

    println()

    val input = readInput("Day10")
    println(part1(input))
    println(part2(input))
}

fun List<String>.cycles(): List<Long> {
    return flatMap {
        if (it == "noop") listOf(0L)
        else listOf(0, it.substringAfter("addx ").toLong())
    }.runningFold(1L) { acc, register -> acc + register }
}