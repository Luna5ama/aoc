package dev.luna5ama.aoc.aoc25

import kotlin.math.max
import kotlin.math.min

private tailrec fun mergeRange(ranges: List<LongRange?>, currIdx: Int): List<LongRange?> {
    val curr = ranges[currIdx] ?: return ranges
    val overlappedIdx = ranges.indices.find { otherIdx ->
        if (otherIdx == currIdx) return@find false
        val other = ranges[otherIdx] ?: return@find false
        curr.last in other || curr.first in other
    }

    return if (overlappedIdx == null) {
        ranges
    } else {
        val newList = ranges.indices.map {
            val other = ranges[it] ?: return@map null
            when (it) {
                currIdx -> null
                overlappedIdx -> min(curr.first, other.first)..max(curr.last, other.last)
                else -> other
            }
        }
        mergeRange(newList, overlappedIdx)
    }
}

fun main() {
    val input = readInput("D5.txt")
    val inputLines = input.lines()
    val emptyIndex = inputLines.indexOf("")
    val ranges = inputLines.subList(0, emptyIndex)
        .map {
            val (start, end) = it.split("-")
            start.toLong()..end.toLong()
        }
    val ingredients = inputLines.subList(emptyIndex + 1, inputLines.size)
        .map { it.toLong() }

    val part1 = ingredients
        .count { ing -> ranges.any { ing in it } }

    println("Part 1:")
    println(part1)

    val mergedRanges = ranges.indices.fold(ranges.map { it as LongRange? }) { acc, i ->
        mergeRange(acc, i)
    }.mapNotNull {
        it
    }

    val part2 = mergedRanges.sumOf {
        it.last - it.first + 1
    }
    println("Part 2:")
    println(part2)
}