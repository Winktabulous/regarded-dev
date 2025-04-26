package com.zenyte.game.util

import com.google.common.base.Preconditions

/**
 * Utility class for calculating the similarity between [strings][String].
 */
object Levensthein {

    fun<T> findNearestMatchOrNull(
        input: String,
        collection: Array<T>,
        ignoreCase: Boolean = true,
        toString: (T) -> String = { it.toString() }
    ) : T {
        Preconditions.checkArgument(input.isNotBlank(), "Input {$input} must not be blank!")
        Preconditions.checkArgument(collection.isNotEmpty(), "Collection {$collection} must not be empty!")
        val a = if (ignoreCase) input.lowercase() else input
        return collection
            .minByOrNull { element ->
                val b = toString(element).let { if (ignoreCase) it.lowercase() else it }
                val distance = calculateLevensteinDistance(
                    a,
                    b
                )
                distance
            }!!
    }

    /**
     * This is an implementation of the Levenstein algorithm.
     *
     * In essence, this can be used to numerically represent the similarity in two given strings.
     *
     * @param a first [input][String]
     * @param b second [input][String]
     *
     * @return an [Int] representing the numerical similarity of two given inputs.
     */
    fun calculateLevensteinDistance(a: String, b: String): Int {
        val dp = Array(a.length + 1) {
            IntArray(b.length + 1)
        }
        for (i in 0..a.length) {
            for (j in 0..b.length) {
                dp[i][j] =  when {
                    i == 0 -> j
                    j == 0 -> i
                    else -> minOf(
                        dp[i - 1][j - 1] + costOfSubstitution(a[i - 1], b[j - 1]),
                        dp[i - 1][j] + 1,
                        dp[i][j - 1] + 1
                    )
                }
            }
        }
        return dp[a.length][b.length]
    }

    private fun costOfSubstitution(a: Char, b: Char) = if (a == b) 0 else 1
}