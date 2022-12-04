fun main() {
    fun part1(input: List<String>): Int {
        return input.sumOf {
            it.split(" ").map { dec -> Decision.parseFrom(dec) }.let { (opp, self) -> self.scoreAgainst(opp) }
        }
    }

    fun part2(input: List<String>): Int {
        return input.sumOf {
            it.split(" ").let { (opp, self) -> Strategy.parseFrom(self).scoreAgainst(Decision.parseFrom(opp)) }
        }
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day02_test")
    check(part1(testInput) == 15)
    check(part2(testInput) == 12)

    val input = readInput("Day02")
    println(part1(input))
    println(part2(input))
}

enum class Decision {
    ROCK,
    PAPER,
    SCISSOR;

    val winsAgainst: Decision
        get() = when (this) {
            ROCK -> SCISSOR
            PAPER -> ROCK
            SCISSOR -> PAPER
        }

    val loosesAgainst: Decision
        get() = when (this) {
            ROCK -> PAPER
            PAPER -> SCISSOR
            SCISSOR -> ROCK
        }

    fun scoreAgainst(decision: Decision): Int {
        val score = if (this.loosesAgainst == decision) 0 else if (this.winsAgainst == decision) 6 else 3
        return 1 + ordinal + score
    }

    companion object {
        fun parseFrom(str: String) = when (str) {
            "A", "X" -> ROCK
            "B", "Y" -> PAPER
            "C", "Z" -> SCISSOR
            else -> error("")
        }
    }
}

enum class Strategy {
    LOSS,
    DRAW,
    WIN;

    fun scoreAgainst(decision: Decision) = when (this) {
        DRAW -> decision.scoreAgainst(decision)
        LOSS -> decision.winsAgainst.scoreAgainst(decision)
        WIN -> decision.loosesAgainst.scoreAgainst(decision)
    }

    companion object {
        fun parseFrom(str: String) = when (str) {
            "X" -> LOSS
            "Y" -> DRAW
            "Z" -> WIN
            else -> error("")
        }
    }
}