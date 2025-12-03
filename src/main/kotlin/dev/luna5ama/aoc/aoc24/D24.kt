package dev.luna5ama.aoc.aoc24

private enum class BitWiseOperator {
    OR, XOR, AND
}

fun main() {
    val input = readInput("D24.txt")
    val inputLines = input.lines()
    val emptyLineIdx = inputLines.indexOf("")

    data class Operation(val op: BitWiseOperator, val a: String, val b: String, val c: String)

    val initialValues = inputLines.subList(0, emptyLineIdx)
        .associate { it.substring(0, 3) to (it.last() == '1') }

    val operations = inputLines.subList(emptyLineIdx + 1, inputLines.size)
        .map {
            val (left, right) = it.split(" -> ")
            val c = right
            val (a, opStr, b) = left.split(" ")
            Operation(BitWiseOperator.valueOf(opStr), a, b, c)
        }

    val operationLookupByC = operations.associateBy { it.c }

    run {
        val cache = initialValues.toMutableMap()

        fun eval(c: String): Boolean {
            return cache.getOrPut(c) {
                val operation = operationLookupByC[c]!!
                val a = eval(operation.a)
                val b = eval(operation.b)
                when (operation.op) {
                    BitWiseOperator.OR -> a or b
                    BitWiseOperator.XOR -> a xor b
                    BitWiseOperator.AND -> a and b
                }
            }
        }

        val part1 = operationLookupByC.keys.asSequence()
            .filter { it.startsWith("z") }
            .sortedDescending()
            .map { eval(it) }
            .map { if (it) "1" else "0" }
            .joinToString("")

        println(part1.toLong(2))
    }
}