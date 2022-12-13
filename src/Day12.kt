fun main() {
    fun part1(input: List<String>): Int {
        return HeightMap.fromLines(input).solvePart1()
    }

    fun part2(input: List<String>): Int {
        return HeightMap.fromLines(input).solvePart2()
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day12_test")
    check(part1(testInput) == 31)
    check(part2(testInput) == 29)

    val input = readInput("Day12")
    println(part1(input))
    println(part2(input))
}

data class HeightMap(
    private val heights: Array<Array<Int>>,
    private val start: Pair<Int, Int>,
    private val finish: Pair<Int, Int>
) {
    fun solvePart1() = solve(listOf(start))

    fun solvePart2(): Int {
        val starts = heights.indices.flatMap { x ->
            heights[x].mapIndexedNotNull { y, h ->
                if (h == 0) x to y else null
            }
        }
        return solve(starts)
    }

    private fun solve(starts: List<Pair<Int, Int>>): Int {
        val paths = starts.map { listOf(it) }.toMutableList()
        val visited = starts.toMutableSet()
        while (paths.isNotEmpty()) {
            val path = paths.removeFirst()
            val currentPos = path.last()
            if (currentPos == finish) {
                return path.size - 1
            }
            val (curX, curY) = currentPos
            val curH = heights[curX][curY]
            Direction.values()
                .map { d -> curX + d.dx to curY + d.dy }
                .filter { pos -> pos !in visited }
                .filter { (x, y) -> x in heights.indices && y in heights[x].indices }
                .filter { (x, y) -> heights[x][y] <= curH + 1 }
                .forEach {
                    visited += it
                    paths += path + it
                }
        }
        return 0
    }

    companion object {
        fun fromLines(lines: List<String>): HeightMap {
            var start = 0 to 0
            var finish = 0 to 0
            val heights = Array(lines[0].length) { x ->
                Array(lines.size) { y ->
                    when (val c = lines[y][x]) {
                        'S' -> 'a'.also { start = x to y }
                        'E' -> 'z'.also { finish = x to y }
                        else -> c
                    } - 'a'
                }
            }
            return HeightMap(heights, start, finish)
        }
    }
}