package dev.luna5ama.aoc.aoc25

import dev.luna5ama.aoc.Side2
import kotlin.math.abs
import kotlin.math.max

fun main() {
    data class Instruction(
        val side: Side2,
        val rotations: Long
    )

    val input = readInput("D1.txt")
    val insts = input.lineSequence()
        .map { Instruction(Side2.fromChar(it[0]), it.substring(1).toLong()) }
        .toList()

    data class State(
        val position: Long,
        val count: Long
    )

    val part1 = insts.fold(State(50, 0)) { acc, inst ->
        val rotation = if (inst.side == Side2.LEFT) -inst.rotations else inst.rotations
        val newPosition = Math.floorMod(acc.position + rotation, 100L)
        acc.copy(position = newPosition, count = acc.count + if (newPosition == 0L) 1 else 0)
    }

    println("Part 1:")
    println(part1.count)

    val part2 = insts.fold(State(50, 0)) { acc, inst ->
        val rotation = if (inst.side == Side2.LEFT) -inst.rotations else inst.rotations
        val newPosition = acc.position + rotation
        val finalPosition = Math.floorMod(newPosition, 100L)
        var count = abs(Math.floorDiv(newPosition, 100))
        if (rotation < 0) {

            if (acc.position == 0L) {
                count -= 1
            }

            if (finalPosition == 0L) {
                count += 1
            }
        }
        count = max(0, count)

        acc.copy(position = finalPosition, count = acc.count + count)
    }

    println("Part 2:")
    println(part2.count)
}