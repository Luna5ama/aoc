package dev.luna5ama.aoc.aoc25

fun main() {
    val input = readInput("D3.txt")

    val part1 = input.lineSequence()
        .map { row -> row.map { it.digitToInt() } }
        .map { row ->
            val leftIdx = (0..row.size - 2)
                .maxBy { row[it] }

            val rightIdx = (leftIdx + 1..row.lastIndex)
                .maxBy { row[it] }

            (row[leftIdx] * 10 + row[rightIdx])
        }
        .sumOf { it.toLong() }

    println("Part 1:")
    println(part1)
    println()

    val maxDigits = 12

    val part2 = input.lineSequence()
        .map { row -> row.map { it.digitToInt() } }
        .map { row ->
            (maxDigits downTo 1).fold(listOf<Int>()) { acc, i ->
                val start = acc.lastOrNull() ?: -1
                acc + (start + 1..row.size - i).maxBy {
                    row[it]
                }
            }.map { row[it] }
        }
        .onEach(::println)
        .map { it.fold(0L) { acc, v -> acc * 10 + v } }
        .sumOf { it }

    println("Part 2:")
    println(part2)
}