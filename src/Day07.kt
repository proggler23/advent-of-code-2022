import java.util.*

fun main() {
    fun part1(input: List<String>): Long {
        val dir = input.parse7()
        return dir.getAllDirs { it.size < 100000 }.sumOf { it.size }
    }

    fun part2(input: List<String>): Long {
        val dir = input.parse7()
        val freeSpace = 70000000L - dir.size
        val sizeToFree = 30000000L - freeSpace
        return dir.getAllDirs { it.size > sizeToFree }.minOf { it.size }
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day07_test")
    check(part1(testInput) == 95437L)
    check(part2(testInput) == 24933642L)

    val input = readInput("Day07")
    println(part1(input))
    println(part2(input))
}

fun List<String>.parse7(): Dir {
    val dir = Dir("/")

    val stack = Stack<String>()
    reversed().forEach { stack.push(it) }
    dir.parseCommands(stack, dir)

    return dir
}

data class Dir(val name: String, val parent: Dir? = null) {
    var dirs = mutableMapOf<String, Dir>()
    var files = mutableMapOf<String, File>()

    val size: Long get() = dirs.values.sumOf { it.size } + files.values.sumOf { it.size }

    fun getAllDirs(predicate: (Dir) -> Boolean): List<Dir> {
        val subDirs = dirs.values.flatMap { it.getAllDirs(predicate) }
        return dirs.values.filter(predicate) + subDirs
    }

    fun parseCommands(stack: Stack<String>, rootDir: Dir) {
        if (stack.isEmpty()) return
        val command = stack.pop()
        val cd = if (command.startsWith("$ cd ")) {
            when (val newDir = command.drop("$ cd ".length)) {
                "/" -> rootDir
                ".." -> parent!!
                else -> dirs.computeIfAbsent(newDir) { Dir(newDir, this) }
            }
        } else if (command.startsWith("$ ls")) {
            while (stack.isNotEmpty()) {
                val top = stack.pop()
                if (top.startsWith("$")) {
                    stack.push(top)
                    break
                } else if (top.startsWith("dir")) {
                    val newDir = top.substringAfter(" ")
                    dirs.putIfAbsent(newDir, Dir(newDir, this))
                } else {
                    val (size, file) = top.split(" ")
                    files.putIfAbsent(file, File(file, size.toLong()))
                }
            }
            this
        } else this

        cd.parseCommands(stack, rootDir)
    }
}

data class File(val name: String, val size: Long)
