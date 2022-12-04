fun main() {
    fun part1(input: List<String>): Int {
        return input.sumOf {
            it.chunked(it.length / 2)
                .map { half -> half.toSet() }
                .let { (left, right) -> left intersect right }
                .single().priority
        }
    }

    fun part2(input: List<String>): Int {
        return input.chunked(3).sumOf {
            it.map { line -> line.toSet() }
                .let { (elf1, elf2, elf3) -> elf1 intersect elf2 intersect elf3 }
                .single().priority
        }
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day03_test")
    check(part1(testInput) == 157)
    check(part2(testInput) == 70)

    val input = readInput("Day03")
    println(part1(input))
    println(part2(input))
}

val Char.priority: Int get() = if (isLowerCase()) (this - 'a') + 1 else (this - 'A') + 27