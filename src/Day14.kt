import kotlin.math.max
import kotlin.math.min

fun main() {
    fun part1(input: List<String>): Int {
        return input.parse14().dropSand1()
    }

    fun part2(input: List<String>): Int {
        return input.parse14().dropSand2()
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day14_test")
    check(part1(testInput) == 24)
    check(part2(testInput) == 93)

    val input = readInput("Day14")
    println(part1(input))
    println(part2(input))
}

enum class Material {
    AIR,
    ROCK,
    SAND
}

fun List<String>.parse14(): Cave {
    val grid = Array(1000) { Array(500) { Material.AIR } }
    for (line in this) {
        line.split("->")
            .map { coords -> coords.trim().split(",").let { (x, y) -> x.toInt() to y.toInt() } }
            .zipWithNext().forEach { (coord1, coord2) ->
                (min(coord1.first, coord2.first)..max(coord1.first, coord2.first)).forEach { x ->
                    (min(coord1.second, coord2.second)..max(coord1.second, coord2.second)).forEach { y ->
                        grid[x][y] = Material.ROCK
                    }
                }
            }
    }
    return Cave(grid)
}

data class Cave(val grid: Array<Array<Material>>) {
    private val lowestRock =
        grid.indices.flatMap { x -> grid[x].indices.filter { y -> grid[x][y] == Material.ROCK } }.max()

    private fun dropSand(predicate: (Pair<Int, Int>) -> Boolean): Int {
        for (i in 0..Int.MAX_VALUE) {
            var sand = 500 to 0
            while (sand.second <= lowestRock) {
                val newSand = sand.move()
                if (newSand == sand) break
                else sand = newSand
            }
            if (predicate(sand)) return i
            else grid[sand.first][sand.second] = Material.SAND
        }
        return -1
    }

    fun dropSand1() = dropSand { (_, y) -> y > lowestRock }
    fun dropSand2() = dropSand { (x, y) -> grid[x][y] == Material.SAND }

    private fun Pair<Int, Int>.move(): Pair<Int, Int> {
        return when (Material.AIR) {
            grid[first][second + 1] -> first to second + 1
            grid[first - 1][second + 1] -> first - 1 to second + 1
            grid[first + 1][second + 1] -> first + 1 to second + 1
            else -> this
        }
    }
}