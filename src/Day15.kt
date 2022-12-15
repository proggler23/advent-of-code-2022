import kotlin.math.abs
import kotlin.math.max
import kotlin.math.min

fun main() {
    fun part1(input: List<String>, y: Int): Int {
        return input.parse15().getPositionsInRange(y)
    }

    fun part2(input: List<String>, boundary: Int): Long {
        return input.parse15().getTuningFrequency(boundary)
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day15_test")
    check(part1(testInput, 10) == 26)
    check(part2(testInput, 20) == 56000011L)

    val input = readInput("Day15")
    println(part1(input, 2000000))
    println(part2(input, 4000000))
}

val SENSOR_REGEX = Regex("Sensor at x=(-?\\d+), y=(-?\\d+): closest beacon is at x=(-?\\d+), y=(-?\\d+)")

fun List<String>.parse15(): SensorGrid {
    return SensorGrid(
        mapNotNull { line ->
            SENSOR_REGEX.find(line)?.let { result ->
                Sensor(
                    position = result.groupValues[1].toInt() to result.groupValues[2].toInt(),
                    beacon = result.groupValues[3].toInt() to result.groupValues[4].toInt()
                )
            }
        }
    )
}

data class Sensor(val position: Pair<Int, Int>, val beacon: Pair<Int, Int>) {
    private val sensorRange = (beacon - position).length
    fun getRange(y: Int) = abs(y - position.second).let { yDistance ->
        if (yDistance > sensorRange) null
        else position.first - (sensorRange - yDistance)..position.first + (sensorRange - yDistance)
    }
}

data class SensorGrid(private val sensors: List<Sensor>) {
    fun getPositionsInRange(y: Int): Int {
        return sensors.mapNotNull { it.getRange(y) }.reduce().sumOf { it.last - it.first }
    }

    fun getTuningFrequency(boundary: Int): Long {
        return (0..boundary).firstNotNullOf { y ->
            sensors.mapNotNull { it.getRange(y) }.reduce()
                .takeIf { ranges -> ranges.size > 1 }
                ?.let { ranges -> 4000000L * (ranges.minOf { it.last } + 1) + y }
        }
    }
}

val Pair<Int, Int>.length: Int get() = abs(first) + abs(second)
operator fun Pair<Int, Int>.minus(other: Pair<Int, Int>) = first - other.first to second - other.second
operator fun Pair<Int, Int>.plus(other: Pair<Int, Int>) = first + other.first to second + other.second

fun List<IntRange>.reduce(): List<IntRange> {
    infix fun IntRange.intersect(other: IntRange) = first <= other.last + 1 && last >= other.first - 1
    infix fun IntRange.join(other: IntRange) = min(first, other.first)..max(last, other.last)

    val ranges = this.toMutableList()
    loop@ while (ranges.size > 1) {
        for (i1 in ranges.indices) {
            for (i2 in (i1 + 1) until ranges.size) {
                if (ranges[i1] intersect ranges[i2]) {
                    ranges += ranges.removeAt(i2).join(ranges.removeAt(i1))
                    continue@loop
                }
            }
        }
        break
    }
    return ranges
}
