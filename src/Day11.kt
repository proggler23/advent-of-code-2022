fun main() {
    fun part1(input: List<String>): Long {
        val monkeys = parse11(input)
        repeat(20) {
            monkeys.forEach { it.takeTurn(monkeys) }
        }
        return monkeys.sortedByDescending { it.inspections }.take(2).productOfLong { it.inspections }
    }

    fun part2(input: List<String>): Long {
        val monkeys = parse11(input)
        repeat(10000) {
            monkeys.forEach { it.takeTurn(monkeys, false) }
        }
        return monkeys.sortedByDescending { it.inspections }.take(2).productOfLong { it.inspections }
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day11_test")
    check(part1(testInput) == 10605L)
    check(part2(testInput) == 2713310158L)

    val input = readInput("Day11")
    println(part1(input))
    println(part2(input))
}

fun parse11(lines: List<String>): List<Monkey> {
    val operationRegex = Regex("new = old (.) (\\d+|old)")
    var i = 0
    return buildList {
        while (++i < lines.size) {
            val startingItems = lines[i++].substringAfter("Starting items: ").split(", ").map { it.toLong() }
            val operation = operationRegex.find(lines[i++])!!.let {
                val v = it.groupValues[2].toLongOrNull()
                if (it.groupValues[1] == "*") { item: Long -> item * (v ?: item) }
                else { item: Long -> item + (v ?: item) }
            }
            val divisableBy = lines[i++].substringAfter("divisible by ").toInt()
            val positiveThrow = lines[i++].substringAfter("throw to monkey ").toInt()
            val negativeThrow = lines[i++].substringAfter("throw to monkey ").toInt()
            val test = { item: Long -> if (item.mod(divisableBy) == 0) positiveThrow else negativeThrow }
            add(Monkey(startingItems, operation, test, divisableBy))
            i++
        }
    }
}

fun <T> List<T>.productOfLong(transform: (T) -> Long) = fold(1L) { acc, t -> acc * transform(t) }

class Monkey(
    startingItems: List<Long>,
    private val operation: (Long) -> Long,
    private val test: (Long) -> Int,
    private val divisor: Int
) {
    private val items = startingItems.toMutableList()

    var inspections = 0L

    fun takeTurn(monkeys: List<Monkey>, relief: Boolean = true) {
        while (items.isNotEmpty()) {
            inspections++
            var item = items.removeFirst()
            item = operation(item)
            if (relief) item /= 3
            else item = item.mod(monkeys.productOfLong { it.divisor.toLong() })
            monkeys[test(item)].items += item
        }
    }
}