fun main() {
    fun part1(input: List<String>): Int {
        return input.sumOf {
            val (left, right) = it.toCharArray().toList().chunked(it.length / 2)
            val common = left.intersect(right.toSet()).first()
            if (common >= 'a') (common - 'a') + 1
            else (common - 'A') + 27
        }
    }

    fun part2(input: List<String>): Int {
        return input.chunked(3).sumOf {
            val (elf1, elf2, elf3) = it.map { line -> line.toCharArray().toSet() }
            val common = elf1.intersect(elf2).intersect(elf3).first()
            if (common >= 'a') (common - 'a') + 1
            else (common - 'A') + 27
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