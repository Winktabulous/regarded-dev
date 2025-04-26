@file:JvmName("StringUtils")

package com.near_reality.util

import com.near_reality.util.charset.Cp1252Charset

fun CharSequence.indefiniteArticle(): String {
    require(isNotEmpty())

    return when (first().lowercaseChar()) {
        'a', 'e', 'i', 'o', 'u' -> "an"
        else -> "a"
    }
}

fun CharSequence.krHashCode(): Int {
    var hash = 0
    for (c in this) {
        hash = ((hash shl 5) - hash) + Cp1252Charset.encode(c)
    }
    return hash
}

fun CharSequence.jagHashCode(): Int {
    var hash = 0
    for (c in this) {
        hash = (hash * 61) + (c.code - 32)
    }
    return hash
}

fun String.capitalize() = replaceFirstChar { it.titlecase() }