package dev.luna5ama.aoc.aoc25

import java.io.File

fun readInput(fileName: String): String {
    return File("inputs/aoc25/$fileName").readText().trimEnd('\n')
}