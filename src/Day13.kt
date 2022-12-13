import kotlin.math.max

fun main() {
    fun part1(input: List<String>): Int {
        return input.parse13().withIndex().sumOf {
            val (left, right) = it.value
            if (left < right) it.index + 1
            else 0
        }
    }

    fun part2(input: List<String>): Int {
        val dividerPackets = listOf("[[2]]".parse13(), "[[6]]".parse13())
        val packets = (input.parse13().flatten() + dividerPackets).sorted()
        return dividerPackets.productOf { packets.indexOf(it) + 1 }
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day13_test")
    check(part1(testInput) == 13)
    check(part2(testInput) == 140)

    val input = readInput("Day13")
    println(part1(input))
    println(part2(input))
}

data class Packet(val packets: List<Packet> = emptyList(), val value: Int? = null) : Comparable<Packet> {
    override fun toString() = value?.toString() ?: packets.joinToString(separator = ",", prefix = "[", postfix = "]")

    override operator fun compareTo(other: Packet): Int {
        if (value != null && other.value != null) {
            return value.compareTo(other.value)
        } else if (value == null && other.value == null) {
            for (i in 0 until max(packets.size, other.packets.size)) {
                val left = packets.getOrNull(i)
                val right = other.packets.getOrNull(i)
                if (left == null) {
                    return -1
                } else if (right == null) {
                    return 1
                } else {
                    val comp = left.compareTo(right)
                    if (comp != 0) return comp
                }
            }
            return 0
        } else if (value != null) {
            return Packet(listOf(this)).compareTo(other)
        } else {
            return compareTo(Packet(listOf(other)))
        }
    }
}

fun List<String>.parse13() = partitionBy("").map { (p1, p2) -> listOf(p1.parse13(), p2.parse13()) }

fun String.parse13(): Packet {
    var index = 0
    val packets = mutableListOf<Packet>()
    while (index < length) {
        if (this[index] == '[') {
            packets += substring(index + 1).parse13()
            index += packets.last().toString().length
        } else if (this[index] == ']') {
            break
        } else if (this[index] == ',') {
            index++
        } else {
            var v = this[index].digitToInt()
            while (++index < this.length && this[index].isDigit()) {
                v *= 10
                v += this[index].digitToInt()
            }
            packets += Packet(value = v)
        }
    }
    return Packet(packets = packets)
}