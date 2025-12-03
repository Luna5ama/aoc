package dev.luna5ama.aoc

import kotlin.text.digitToInt

class IntMatrix {
    val rows: Int
    val cols: Int
    val data: IntArray
    val xRange: IntRange
    val yRange: IntRange

    private constructor(rows: Int, cols: Int, data: IntArray) {
        this.rows = rows
        this.cols = cols
        this.data = data
        xRange = 0..<cols
        yRange = 0..<rows
    }

    constructor(rows: Int, cols: Int, init: (Int, Int) -> Int) {
        this.rows = rows
        this.cols = cols
        data = IntArray(rows * cols) { index -> init(index % cols, index / cols) }
        xRange = 0..<cols
        yRange = 0..<rows
    }

    constructor(rows: Int, cols: Int) {
        this.rows = rows
        this.cols = cols
        data = IntArray(rows * cols)
        xRange = 0..<cols
        yRange = 0..<rows
    }

    fun checkBounds(x: Int, y: Int): Boolean {
        return x in xRange && y in yRange
    }

    fun checkBounds(v: IntVec2): Boolean {
        return checkBounds(v.x, v.y)
    }

    fun index(x: Int, y: Int): Int {
        return y * cols + x
    }

    operator fun get(x: Int, y: Int): Int {
        return data[index(x, y)]
    }

    operator fun get(v: IntVec2): Int {
        return get(v.x, v.y)
    }

    operator fun set(x: Int, y: Int, value: Int) {
        data[index(x, y)] = value
    }

    operator fun set(v: IntVec2, value: Int) {
        set(v.x, v.y, value)
    }

    fun xy(): Sequence<IntVec2> {
        return sequence {
            for (y in yRange) {
                for (x in xRange) {
                    yield(IntVec2(x, y))
                }
            }
        }
    }

    fun withXY(): Sequence<XYValue> {
        return sequence {
            for (y in yRange) {
                for (x in xRange) {
                    yield(XYValue(IntVec2(x, y), get(x, y)))
                }
            }
        }
    }

    fun scale(factor: Int): IntMatrix {
        require(factor > 1)
        val newRows = rows * factor
        val newCols = cols * factor
        return IntMatrix(newRows, newCols) { x, y -> get(x / factor, y / factor) }
    }

    fun copy(): IntMatrix {
        return IntMatrix(rows, cols, data.copyOf())
    }

    inline fun print(mapper: (Int) -> Char) {
        for (y in yRange) {
            for (x in xRange) {
                print(mapper(this[x, y]))
            }
            println()
        }
    }

    fun print() {
        for (y in yRange) {
            for (x in xRange) {
                print(this[x, y])
            }
            println()
        }
    }

    data class XYValue(val xy: IntVec2, val value: Int)

    data class XValue(val x: Int, val value: Int)
}

fun String.toIntMatrix(): IntMatrix {
    return lines().toIntMatrix()
}

fun List<String>.toIntMatrix(): IntMatrix {
    val rows = this.size
    val cols = this[0].length
    return IntMatrix(rows, cols) { x, y -> this[y][x].digitToInt() }
}