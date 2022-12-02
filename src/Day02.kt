fun main() {
    fun part1(input: List<String>): Int {
        val selections = input.map {
            val (opp, self) = it.split(" ")
            SelectionStrategy(Decision.valueOf(opp), Strategy.valueOf(self))
        }
        return selections.sumOf { it.score1 }
    }

    fun part2(input: List<String>): Int {
        val selections = input.map {
            val (opp, self) = it.split(" ")
            SelectionStrategy(Decision.valueOf(opp), Strategy.valueOf(self))
        }
        return selections.sumOf { it.score2 }
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day02_test")
    check(part1(testInput) == 15)
    check(part2(testInput) == 12)

    val input = readInput("Day02")
    println(part1(input))
    println(part2(input))
}

data class SelectionStrategy(val opp: Decision, val self: Strategy) {
    val score1: Int get() = self.value + self.scoreAgainst(opp)
    val score2: Int get() = opp.scoreAgainst(self)
}

enum class Decision(val description: String) {
    A("Rock"),
    B("Paper"),
    C("Scissor");

    fun scoreAgainst(strategy: Strategy): Int {
        val self = Strategy.values()[(ordinal + when (strategy) {
            Strategy.X -> 2
            Strategy.Y -> 0
            Strategy.Z -> 1
        }) % 3]
        return self.value + self.scoreAgainst(this)
    }
}

enum class Strategy(val description: String, val value: Int) {
    X("Loss", 1),
    Y("Draw", 2),
    Z("Win", 3);

    fun scoreAgainst(opp: Decision) = when ((3 + ordinal - opp.ordinal) % 3) {
        0 -> 3
        1 -> 6
        2 -> 0
        else -> error("cannot happen")
    }
}