package dev.luna5ama.aoc.aoc24

import dev.luna5ama.aoc.CharMatrix
import dev.luna5ama.aoc.cartesianProduct
import dev.luna5ama.aoc.uniquePairs

fun main() {
    val input = readInput("D25.txt")

    val patterns = input.lineSequence()
        .filter { it.isNotEmpty() }
        .chunked(7)
        .map { it.toCharMatrix() }
        .toList()

    fun processPattern(m: CharMatrix): List<Int> {
        return m.withXY()
            .filter { it.value == '#' }
            .groupingBy { it.xy.x }
            .eachCount()
            .toList()
            .sortedBy { it.first }
            .map { it.second - 1 }
    }

    val locks = patterns.filter { it[0, 0] == '#' }
        .map { processPattern(it) }

    println()
    val keys = patterns.filter { it[0, 0] == '.' }
        .map { processPattern(it) }

    println()
    val part1 = (locks cartesianProduct keys).count { p ->
        (p.first zip p.second).map {
            it.first + it.second
        }.all {
            it <= 5
        }
    }

    println("Part 1:")
    println(part1)
}