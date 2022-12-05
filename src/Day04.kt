fun main() {
    fun part1(input: List<String>): Int {
        return input.parse4().count { (elf1, elf2) ->
            (elf1.first in elf2 && elf1.last in elf2) || (elf2.first in elf1 && elf2.last in elf1)
        }
    }

    fun part2(input: List<String>): Int {
        return input.parse4().count { (elf1, elf2) ->
            (elf1.first in elf2 || elf1.last in elf2) || (elf2.first in elf1 || elf2.last in elf1)
        }
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day04_test")
    check(part1(testInput) == 2)
    check(part2(testInput) == 4)

    val input = readInput("Day04")
    println(part1(input))
    println(part2(input))
}

fun List<String>.parse4() = map { line ->
    line.split(',').map { range ->
        range.split('-')
            .map { it.toInt() }
            .let { (from, to) -> from..to }
    }
}