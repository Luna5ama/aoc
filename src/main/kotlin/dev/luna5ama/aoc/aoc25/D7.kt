package dev.luna5ama.aoc.aoc25

fun main() {
    data class State(val beams: List<Int>, val splits: Int)

    val input = readInput("D7.txt")
    val inputLines = input.lines()

    val (_, part1) = inputLines.drop(1).fold(State(listOf(inputLines.first().indexOf("S")), 0)) { state, line ->
        val newBeams = state.beams.flatMap {
            if (line.getOrNull(it) == '^') listOf(it - 1, it + 1) else listOf(it)
        }

        State(newBeams.distinct(), state.splits + newBeams.size - state.beams.size)
    }

    println("Part 1:")
    println(part1)

    val endBeams = inputLines.drop(1).fold(listOf(inputLines.first().indexOf("S") to 1L)) { beams, line ->
        val newBeams = beams.flatMap {
            if (line.getOrNull(it.first) == '^') listOf(
                it.first - 1 to it.second,
                it.first + 1 to it.second
            ) else listOf(it)
        }

        newBeams.groupingBy { it.first }
            .reduce { x, a, b -> x to (a.second + b.second) }
            .values
            .toList()
    }
    val part2 = endBeams.sumOf { it.second }
    println("Part 2:")
    println(part2)
}