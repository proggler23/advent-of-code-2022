import kotlin.math.abs
import kotlin.math.sign

fun main() {
    fun part1(input: List<String>) = RopeGrid().apply { move(input.parse9()) }.visited.size
    fun part2(input: List<String>) = RopeGrid(10).apply { move(input.parse9()) }.visited.size

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day09_test")
    check(part1(testInput) == 13)
    check(part2(testInput) == 1)

    val input = readInput("Day09")
    println(part1(input))
    println(part2(input))
}

fun List<String>.parse9() = map { line ->
    line.split(" ").let { (d, l) -> RopeMovement(Direction.values().first { it.name.startsWith(d) }, l.toInt()) }
}

enum class Direction(val dx: Int = 0, val dy: Int = 0) {
    UP(dy = -1),
    DOWN(dy = 1),
    LEFT(dx = -1),
    RIGHT(dx = 1)
}

data class RopeMovement(val direction: Direction, var length: Int)

class RopeGrid(val knotCount: Int = 2) {
    private val knots = MutableList(knotCount) { 0 to 0 }

    val visited = mutableSetOf(knots[knotCount - 1])

    fun move(movements: List<RopeMovement>) {
        movements.forEach { moveHead(it) }
    }

    private fun moveHead(movement: RopeMovement) {
        repeat(movement.length) {
            knots[0] = knots[0].first + movement.direction.dx to knots[0].second + movement.direction.dy
            (1 until knotCount).forEach { moveKnot(it) }
            visited.add(knots[knotCount - 1])
        }
    }

    private fun moveKnot(i: Int) {
        val (hx, hy) = knots[i - 1]
        val (tx, ty) = knots[i]
        val dx = hx - tx
        val dy = hy - ty
        if (abs(dx) > 1 || abs(dy) > 1) {
            knots[i] = tx + dx.sign to ty + dy.sign
        }
    }
}
