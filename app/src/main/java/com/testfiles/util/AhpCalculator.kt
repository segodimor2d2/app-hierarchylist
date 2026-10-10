package com.testfiles.util

import kotlin.math.abs

/** Ratios follow the same i < j pair order as SharedViewModel.itemPairs. */
fun calculateAhpWeights(size: Int, ratios: List<Double>): List<Double> {
    require(size > 0)
    require(ratios.size == size * (size - 1) / 2)
    require(ratios.all { it.isFinite() && it in (1.0 / 9.0)..9.0 })
    val matrix = Array(size) { DoubleArray(size) { 1.0 } }
    var pair = 0
    for (i in 0 until size) {
        for (j in i + 1 until size) {
            matrix[i][j] = ratios[pair++]
            matrix[j][i] = 1.0 / matrix[i][j]
        }
    }
    var weights = DoubleArray(size) { 1.0 / size }
    // Power iteration gives the normalized principal eigenvector.
    repeat(1000) {
        val next = DoubleArray(size) { i ->
            (0 until size).sumOf { j -> matrix[i][j] * weights[j] }
        }
        val sum = next.sum()
        for (i in next.indices) next[i] /= sum
        val difference = next.indices.maxOf { abs(next[it] - weights[it]) }
        weights = next
        if (difference < 1e-12) return weights.toList()
    }
    return weights.toList()
}
