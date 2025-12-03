package dev.luna5ama.aoc.aoc25

fun main() {
    val input = readInput("D2.txt")
    val numbers = input.splitToSequence(",")
        .map {
            val vals = it.splitToSequence("-").map(String::toLong).toList()
            LongRange(vals.first(), vals.last())
        }
        .flatten()
        .toList()

    fun check(str: String, parts: Int): Boolean {
        if (str.length % parts != 0) return false
        val partLen = str.length / parts
        return str.chunked(partLen).toSet().size == 1
    }

    val part1 = numbers.asSequence()
        .filter {
            check(it.toString(), 2)
        }
        .sum()

    println("Part 1:")
    println(part1)

    val part2 = numbers.asSequence()
        .filter { num ->
            val str = num.toString()
            (2..str.length).any { check(str, it) }
        }
        .sum()

    println("Part 2:")
    println(part2)
}