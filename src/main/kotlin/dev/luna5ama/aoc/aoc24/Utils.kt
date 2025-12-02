package dev.luna5ama.aoc.aoc24

import dev.luna5ama.aoc.CharMatrix
import java.io.File

fun readInput(fileName: String): String {
    return File("inputs/aoc24/$fileName").readText().trimEnd('\n')
}

fun String.toCharMatrix(): CharMatrix {
    return lines().toCharMatrix()
}

fun List<String>.toCharMatrix(): CharMatrix {
    val rows = this.size
    val cols = this[0].length
    return CharMatrix(Array(rows) { y -> CharArray(cols) { x -> this[y][x] } })
}