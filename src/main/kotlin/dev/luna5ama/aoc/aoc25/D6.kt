package dev.luna5ama.aoc.aoc25

import dev.luna5ama.aoc.transposed

private enum class Operator(val identity: Long, val func: (Long, Long) -> Long) {
    ADD(0L, Long::plus),
    MUL(1L, Long::times)
}

fun main() {
    val input = readInput("D6.txt")
    val inputLines = input.lines()
    val splitRegex = "\\s+".toRegex()

    data class Problem(val numbers: List<Long>, val op: Operator)

    val problems1 = inputLines.asSequence()
        .map { it.trim().split(splitRegex) }
        .toList()
        .transposed()
        .map {
            val op = when (val opStr = it.last()) {
                "*" -> Operator.MUL
                "+" -> Operator.ADD
                else -> error("Unsupported: $opStr")
            }
            val numbers = it.dropLast(1).map(String::toLong)
            Problem(numbers, op)
        }

    val part1 = problems1.asSequence()
        .sumOf { it.numbers.fold(it.op.identity, it.op.func) }

    println("Part 1:")
    println(part1)

    val idx = inputLines.last().asSequence().withIndex()
        .filter { (i, c) -> !c.isWhitespace() }
        .map { it.index }
        .toList() + (inputLines.last().length + 1)


    val problems2 = inputLines.asSequence()
        .map { line ->
            idx.asSequence()
                .windowed(2, 1)
                .map { line.substring(it[0], it[1] - 1) }
                .toList()
        }
        .toList()
        .transposed()
        .map { col ->
            val op = when (val opStr = col.last().trim()) {
                "*" -> Operator.MUL
                "+" -> Operator.ADD
                else -> error("Unsupported: $opStr")
            }

            val numbers = col.dropLast(1)
                .map { it.toList() }
                .transposed()
                .map { it.joinToString("").trim().toLong() }


            Problem(numbers, op)
        }

    val part2 = problems2.asSequence()
        .sumOf { it.numbers.fold(it.op.identity, it.op.func) }

    println("Part 2:")
    println(part2)
}