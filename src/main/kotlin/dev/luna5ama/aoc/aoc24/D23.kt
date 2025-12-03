package dev.luna5ama.aoc.aoc24

import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap
import it.unimi.dsi.fastutil.objects.ObjectArrayList
import it.unimi.dsi.fastutil.objects.ObjectOpenHashSet

fun main() {
    val input = readInput("D23.txt")
    val adjList = Object2ObjectOpenHashMap<String, ObjectArrayList<String>>()
    fun insert(k: String, v: String) {
        adjList.computeIfAbsent(k) { ObjectArrayList() }.add(v)
    }

    input.lineSequence()
        .forEach {
            val left = it.substring(0, 2)
            val right = it.substring(3)
            insert(left, right)
            insert(right, left)
        }

    val set3s = ObjectOpenHashSet<List<String>>()

    adjList.forEach l1@{ (k, aNeighbors) ->
        val a = k
        aNeighbors.forEach l2@{ b ->
            adjList[b]?.forEach l3@{ c ->
                if (c in aNeighbors) {
                    val s = mutableListOf<String>()
                    s.add(a)
                    s.add(b)
                    s.add(c)
                    s.sort()
                    set3s.add(s)
                }
            }
        }
    }

    val part1 = set3s.count { set ->
        set.any { it.startsWith('t') }
    }

    println("Part 1: ")
    println(part1)

    var sets = ObjectOpenHashSet<ObjectOpenHashSet<String>>()
    adjList.keys.mapTo(sets) {
        ObjectOpenHashSet<String>().apply { add(it) }
    }

    do {
        val newSets = ObjectOpenHashSet<ObjectOpenHashSet<String>>()

        sets.forEach { oldSet ->
            oldSet.forEach { eInOldSet ->
                adjList[eInOldSet]!!.forEach {
                    if (adjList[it]!!.containsAll(oldSet)) {
                        val newSet = ObjectOpenHashSet(oldSet)
                        if (newSet.add(it)) {
                            newSets.add(newSet)
                        }
                    }
                }
            }
        }

        if (newSets.isNotEmpty()) {
            sets = newSets
        }
    } while (newSets.isNotEmpty())

    check(sets.size == 1)
    println(sets.first().sorted().joinToString(","))
}