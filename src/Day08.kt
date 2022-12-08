fun main() {
    fun part1(input: List<String>) = input.parse8().visibleTreeCount
    fun part2(input: List<String>) = input.parse8().bestScenicView

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day08_test")
    check(part1(testInput) == 21)
    check(part2(testInput) == 8)

    val input = readInput("Day08")
    println(part1(input))
    println(part2(input))
}

fun List<String>.parse8() = TreeGrid(Array(this[0].length) { x -> Array(size) { y -> this[y][x].digitToInt() } })

fun <T> List<T>.productOf(transform: (T) -> Int) = fold(1) { acc, t -> acc * transform(t) }

class TreeGrid(private val heights: Array<Array<Int>>) {
    val visibleTreeCount: Int
        get() = heights.indices.sumOf { x ->
            heights[x].indices.count { y ->
                isTreeVisible(x, y)
            }
        }

    val bestScenicView: Int
        get() = heights.indices.maxOf { x ->
            heights[x].indices.maxOf { y ->
                getScenicScore(x, y)
            }
        }

    private fun viewRanges(x: Int, y: Int) = listOf(
        (x - 1 downTo 0).map { heights[it][y] },
        (x + 1 until heights.size).map { heights[it][y] },
        (y - 1 downTo 0).map { heights[x][it] },
        (y + 1 until heights[x].size).map { heights[x][it] }
    )

    private fun isTreeVisible(x: Int, y: Int) = viewRanges(x, y).any { it.all { height -> height < heights[x][y] } }

    private fun getScenicScore(x: Int, y: Int) = viewRanges(x, y).productOf { range ->
        val filteredRange = range.takeWhile { height -> height < heights[x][y] }
        if (range.isEmpty()) 0
        else if (filteredRange.size == range.size) range.size
        else filteredRange.size + 1
    }
}
