package dev.luna5ama.aoc

class CharMatrix {
    val rows: Int
    val cols: Int
    val data: CharArray
    val xRange: IntRange
    val yRange: IntRange

    private constructor(rows: Int, cols: Int, data: CharArray) {
        this.rows = rows
        this.cols = cols
        this.data = data
        xRange = 0..<cols
        yRange = 0..<rows
    }

    constructor(rows: Int, cols: Int) {
        this.rows = rows
        this.cols = cols
        data = CharArray(rows * cols)
        xRange = 0..<cols
        yRange = 0..<rows
    }

    constructor(rows: Int, cols: Int, init: (Int, Int) -> Char) {
        this.rows = rows
        this.cols = cols
        data = CharArray(rows * cols) { index -> init(index % cols, index / cols) }
        xRange = 0..<cols
        yRange = 0..<rows
    }

    constructor(data: Array<CharArray>) {
        rows = data.size
        cols = data[0].size
        this.data = CharArray(rows * cols) { index -> data[index / cols][index % cols] }
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

    operator fun get(x: Int, y: Int): Char {
        return data[index(x, y)]
    }

    operator fun get(v: IntVec2): Char {
        return get(v.x, v.y)
    }

    operator fun set(x: Int, y: Int, value: Char) {
        data[index(x, y)] = value
    }

    operator fun set(v: IntVec2, value: Char) {
        set(v.x, v.y, value)
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

    fun xy(): Sequence<IntVec2> {
        return sequence {
            for (y in yRange) {
                for (x in xRange) {
                    yield(IntVec2(x, y))
                }
            }
        }
    }


    fun scale(factor: Int): CharMatrix {
        require(factor > 1)
        val newRows = rows * factor
        val newCols = cols * factor
        return CharMatrix(newRows, newCols) { x, y -> get(x / factor, y / factor) }
    }

    fun copy(): CharMatrix {
        return CharMatrix(rows, cols, data.copyOf())
    }

    inline fun print(mapper: (Char) -> Char) {
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

    data class XYValue(val xy: IntVec2, val value: Char)
}

fun String.toCharMatrix(): CharMatrix {
    return lines().toCharMatrix()
}

fun List<String>.toCharMatrix(): CharMatrix {
    val rows = this.size
    val cols = this[0].length
    return CharMatrix(Array(rows) { y -> CharArray(cols) { x -> this[y][x] } })
}