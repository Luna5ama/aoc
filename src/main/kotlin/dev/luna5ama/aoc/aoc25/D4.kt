package dev.luna5ama.aoc.aoc25

import dev.luna5ama.aoc.CharMatrix
import dev.luna5ama.aoc.IntVec2
import dev.luna5ama.aoc.cartesianProduct
import dev.luna5ama.aoc.toCharMatrix

private tailrec fun part2Remove(matrix: CharMatrix, accum: Int = 0) : Int {
    val canRemove = matrix.withXY()
        .filter { it.value == '@' }
        .filter { center ->
            ((-1..1) cartesianProduct (-1..1))
                .map { IntVec2(it.first, it.second) }
                .filter { it != IntVec2.ZERO }
                .map { center.xy + it }
                .filter { matrix.checkBounds(it)}
                .filter { matrix[it] == '@' }
                .count() < 4
        }
        .toList()

    if (canRemove.isEmpty()) return accum

    val newMatrix = matrix.copy()
    canRemove.forEach {
        newMatrix[it.xy] = '.'
    }
    return part2Remove(newMatrix, accum + canRemove.size)
}

fun main() {
    val input = readInput("D4.txt")
    val matrix = input.toCharMatrix()

    val part1 = matrix.withXY()
        .filter { it.value == '@' }
        .filter { center ->
            ((-1..1) cartesianProduct (-1..1))
                .map { IntVec2(it.first, it.second) }
                .filter { it != IntVec2.ZERO }
                .map { center.xy + it }
                .filter { matrix.checkBounds(it)}
                .filter { matrix[it] == '@' }
                .count() < 4
        }
        .count()

    println("Part 1:")
    println(part1)

    val part2 = part2Remove(matrix)

    println("Part 2:")
    println(part2)
}