package dev.luna5ama.aoc



fun <T> List<T>.permutation(n: Int): Sequence<List<T>> {
    return if (n == 0) {
        sequenceOf(emptyList())
    } else {
        val indices = IntArray(n)
        sequence {
            while (true) {
                yield(indices.map { this@permutation[it] })
                var i = 0
                while (i < n && indices[i] == size - 1) {
                    indices[i] = 0
                    i++
                }
                if (i == n) break
                indices[i]++
            }
        }
    }
}

infix fun <A, B> List<A>.uniquePairs(b: List<B>): Sequence<Pair<A, B>> {
    return sequence {
        forEachIndexed { ia, ea ->
            for (ib in ia..<b.size) {
                yield(ea to b[ib])
            }
        }
    }
}

infix fun <A, B> List<A>.cartesianProduct(b: List<B>): Sequence<Pair<A, B>> {
    return sequence {
        this@cartesianProduct.forEach { ea ->
            b.forEach { eb ->
                yield(ea to eb)
            }
        }
    }
}