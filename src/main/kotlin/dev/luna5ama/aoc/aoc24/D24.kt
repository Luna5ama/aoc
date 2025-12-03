package dev.luna5ama.aoc.aoc24

import dev.luna5ama.aoc.flatten

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
        .associate {
            val (left, right) = it.split(" -> ")
            val c = right
            val (a, opStr, b) = left.split(" ")
            c to Operation(BitWiseOperator.valueOf(opStr), a, b, c)
        }

    fun Sequence<Boolean>.bitsToNumber() = map { if (it) "1" else "0" }
        .joinToString("")
        .toLong(2)

    fun evalCircuit(operations: Map<String, Operation>): Long {
        val cache = initialValues.toMutableMap()

        fun eval(c: String): Boolean {
            return cache.getOrPut(c) {
                val operation = operations[c]!!
                val a = eval(operation.a)
                val b = eval(operation.b)
                when (operation.op) {
                    BitWiseOperator.OR -> a or b
                    BitWiseOperator.XOR -> a xor b
                    BitWiseOperator.AND -> a and b
                }
            }
        }

        val part1 = operations.keys.asSequence()
            .filter { it.startsWith("z") }
            .sortedDescending()
            .map { eval(it) }
            .bitsToNumber()

        return part1
    }

    val part1 = evalCircuit(operations)
    println("Part 1:")
    println(part1)

    val xs = initialValues.entries.asSequence()
        .filter { it.key.startsWith("x") }
        .sortedByDescending { it.key }
        .map { it.key }
        .toList()

    val ys = initialValues.entries.asSequence()
        .filter { it.key.startsWith("y") }
        .sortedByDescending { it.key }
        .map { it.key }
        .toList()

    val x = xs.asSequence()
        .map { initialValues[it]!! }
        .bitsToNumber()

    val y = ys.asSequence()
        .map { initialValues[it]!! }
        .bitsToNumber()

    val expectedZ = x + y

    fun getTree(curr: List<Operation>): List<List<Operation>> {
        if (curr.isEmpty()) return emptyList()

        val next = curr.flatMap {
            listOf(it.a, it.b)
        }.mapNotNull {
            operations[it]
        }

        return listOf(curr) + getTree(next)
    }

    val swaps = listOf(
        "z12" to "qdg",
        "vvf" to "z19",
        "dck" to "fgn",
        "nvh" to "z37",
//        "z45" to "z37",
    )

    val swapMap = (swaps + swaps.map { it.second to it.first }).toMap()

    val newOperation = operations.values
        .map { it.copy(c = swapMap[it.c] ?: it.c) }


    val goodOutputs = mutableSetOf<String>()
    val badOutputs = mutableSetOf<String>()
    val asInputs = newOperation.groupByTo(mutableMapOf()) { it.a }
    newOperation.groupBy { it.b }.forEach { (key, value) ->
        asInputs.getOrPut(key, ::mutableListOf).addAll(value)
    }

    fun findOp(a: String, b: String, op: BitWiseOperator): List<Operation> {
        return (asInputs[a]?.asSequence() ?: emptySequence())
            .filter { it.op == op }
            .filter { it.a == b || it.b == b }
            .toList()
    }

    fun List<Operation>.processOps(validate: (String) -> Boolean): String? {
        var found: String? = null
        forEach {
            val output = it.c
            if (found == null && validate(output)) {
                goodOutputs.add(output)
                found = output
            } else {
                badOutputs.add(output)
            }
        }
        return found
    }

    fun findAndProcess(a: String, b: String, op: BitWiseOperator, validate: (String) -> Boolean = { true }): String? {
        return findOp(a, b, op).processOps(validate)
    }

    val missingCarry = mutableSetOf<Int>()
    val missingAXorB = mutableSetOf<Int>()
    val missingCinXorAB = mutableSetOf<Int>()
    val missingCinAndAB = mutableSetOf<Int>()
    val missingAAndB = mutableSetOf<Int>()
    val missingOr = mutableSetOf<Int>()

    fun <T> T.addToIfNull(target: MutableSet<Int>, index: Int): T {
        if (this == null) check(target.add(index))
        return this
    }

    fun numPadded(index: Int): String = index.toString().padStart(2, '0')

    fun checkHalfAdder(index: Int): String? {
        fun <T> T.addToIfNull(target: MutableSet<Int>): T = addToIfNull(target, index)

        val num = numPadded(index)
        val a = "x$num"
        val b = "y$num"
        val s = "z$num"
        findAndProcess(a, b, BitWiseOperator.XOR) { it == s }.addToIfNull(missingAXorB)
        return findAndProcess(a, b, BitWiseOperator.AND).addToIfNull(missingAAndB)
    }

    fun <A : Any, B : Any, R> A?.joinLet(b: B?, block: (A, B) -> R): R? {
        return if (this != null && b != null) {
            block(this, b)
        } else {
            null
        }
    }

    fun checkFullAdder(index: Int, cIn: String?): String? {
        fun <T> T.addToIfNull(target: MutableSet<Int>): T = addToIfNull(target, index)

        cIn.addToIfNull(missingCarry)

        val num = numPadded(index)
        val a = "x$num"
        val b = "y$num"
        val s = "z$num"
        val aXorB = findAndProcess(a, b, BitWiseOperator.XOR).addToIfNull(missingAXorB)

        val cinAndAB = aXorB
            .joinLet(cIn) { aXorB, cIn ->
                findAndProcess(cIn, aXorB, BitWiseOperator.XOR) { it == s }.addToIfNull(missingCinXorAB)
                findAndProcess(cIn, aXorB, BitWiseOperator.AND).addToIfNull(missingCinAndAB)
            }

        return findAndProcess(a, b, BitWiseOperator.AND).addToIfNull(missingAAndB)
            .joinLet(cinAndAB) { aAndB, cinAndAB ->
                findAndProcess(aAndB, cinAndAB, BitWiseOperator.OR).addToIfNull(missingOr)
            }
    }

    var last = 0

    (1..xs.size).fold(checkHalfAdder(0)) { acc, i ->
       if (acc == null) return@fold null
        println("$i: $acc")
        last = i
        checkFullAdder(i, acc)
    }



    operations.keys.asSequence()
        .filter { it.startsWith("z") }
        .filter { it.substring(1).toInt() in last..last + 1 }
        .sortedDescending()
        .forEach {
            println(it)
            getTree(listOf(operations[it]!!)).forEach {
                println(it)
            }
            println()
        }


    println("Good")
    println(goodOutputs)

    println("Bad")
    println(badOutputs)
    println()

    println("missingCarry")
    println(missingCarry)
    println()

    println("missingAXorB")
    println(missingAXorB)
    println("missingCinXorAB")
    println(missingCinXorAB)
    println("missingCinAndAB")
    println(missingCinAndAB)
    println("missingAAndB")
    println(missingAAndB)
    println("missingOr")
    println(missingOr)
    println()
    println(swaps)

    val part2 = swaps.flatten().sorted().joinToString(",")
    println("Part 2:")
    println(part2)
}