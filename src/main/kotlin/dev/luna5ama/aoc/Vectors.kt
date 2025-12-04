package dev.luna5ama.aoc

import kotlin.math.abs

data class IntVec2(val x: Int, val y: Int) {
    operator fun plus(other: IntVec2) = IntVec2(x + other.x, y + other.y)
    operator fun plus(other: Int) = IntVec2(x + other, y + other)
    operator fun minus(other: IntVec2) = IntVec2(x - other.x, y - other.y)
    operator fun minus(other: Int) = IntVec2(x - other, y - other)
    operator fun times(scalar: Int) = IntVec2(x * scalar, y * scalar)
    operator fun times(other: IntVec2) = IntVec2(x * other.x, y * other.y)
    operator fun div(scalar: Int) = IntVec2(x / scalar, y / scalar)
    operator fun div(other: IntVec2) = IntVec2(x / other.x, y / other.y)
    operator fun unaryMinus() = IntVec2(-x, -y)
    fun manhattan() = abs(x) + abs(y)

    fun toLongVec2() = LongVec2(x.toLong(), y.toLong())

    fun pack() = (x.toLong() shl 32) or y.toLong()

    companion object {
        val ZERO = IntVec2(0, 0)
    }
}

enum class Direction4(val dx: Int, val dy: Int, val oppositeIndex: Int, val bitMask: Int) {
    UP(0, -1, 2, 0b0001),
    RIGHT(1, 0, 3, 0b0010),
    DOWN(0, 1, 0, 0b0100),
    LEFT(-1, 0, 1, 0b1000);

    val vec = IntVec2(dx, dy)
    val opposite get() = entries[oppositeIndex]
    val right get() = entries[(ordinal + 1) % 4]
    val left get() = entries[(ordinal + 3) % 4]
    val back get() = entries[(ordinal + 2) % 4]
}

fun Char.arrowToDir() = when (this) {
    '^' -> Direction4.UP
    '>' -> Direction4.RIGHT
    'v' -> Direction4.DOWN
    '<' -> Direction4.LEFT
    else -> error("Unknown arrow: $this")
}

fun Direction4.toArrowChar() = when (this) {
    Direction4.UP -> '^'
    Direction4.RIGHT -> '>'
    Direction4.DOWN -> 'v'
    Direction4.LEFT -> '<'
}

fun Long.unpack(): IntVec2 {
    return IntVec2((this shr 32).toInt(), this.toInt())
}

operator fun IntVec2.plus(other: Direction4) = this + other.vec
operator fun IntVec2.minus(other: Direction4) = this - other.vec

operator fun Int.plus(other: IntVec2) = IntVec2(this + other.x, this + other.y)
operator fun Int.minus(other: IntVec2) = IntVec2(this - other.x, this - other.y)
operator fun Int.times(other: IntVec2) = IntVec2(this * other.x, this * other.y)
operator fun Int.div(other: IntVec2) = IntVec2(this / other.x, this / other.y)

data class LongVec2(val x: Long, val y: Long) {
    operator fun plus(other: LongVec2) = LongVec2(x + other.x, y + other.y)
    operator fun plus(other: Long) = LongVec2(x + other, y + other)
    operator fun minus(other: LongVec2) = LongVec2(x - other.x, y - other.y)
    operator fun minus(other: Long) = LongVec2(x - other, y - other)
    operator fun times(scalar: Long) = LongVec2(x * scalar, y * scalar)
    operator fun times(other: LongVec2) = LongVec2(x * other.x, y * other.y)
    operator fun div(scalar: Long) = LongVec2(x / scalar, y / scalar)
    operator fun div(other: LongVec2) = LongVec2(x / other.x, y / other.y)
    operator fun unaryMinus() = LongVec2(-x, -y)
    fun manhattan() = abs(x) + abs(y)

    fun toIntVec2() = IntVec2(x.toInt(), y.toInt())
}

operator fun Long.plus(other: LongVec2) = LongVec2(this + other.x, this + other.y)
operator fun Long.minus(other: LongVec2) = LongVec2(this - other.x, this - other.y)
operator fun Long.times(other: LongVec2) = LongVec2(this * other.x, this * other.y)
operator fun Long.div(other: LongVec2) = LongVec2(this / other.x, this / other.y)

fun gcd(a: Int, b: Int): Int {
    if (b == 0) return a
    return gcd(b, a % b)
}

fun gcd(a: Long, b: Long): Long {
    if (b == 0L) return a
    return gcd(b, a % b)
}

fun lcm(a: Int, b: Int): Int {
    return if (a == 0 || b == 0) {
        0
    } else {
        (a * b) / gcd(a, b)
    }
}

fun lcm(a: Long, b: Long): Long {
    return if (a == 0L || b == 0L) {
        0L
    } else {
        (a * b) / gcd(a, b)
    }
}