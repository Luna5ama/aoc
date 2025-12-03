package dev.luna5ama.aoc

enum class Side2 {
    LEFT, RIGHT;

    companion object {
        fun fromChar(c: Char): Side2 {
            return when(c.lowercaseChar()) {
                'l' -> LEFT
                'r' -> RIGHT
                else -> error("Invalid side char: $c")
            }
        }
    }
}