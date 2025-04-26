package com.runespawn.util

import java.util.*

fun<T> weakMutableSetOf(vararg elements: T): MutableSet<T> {
    return Collections.newSetFromMap(WeakHashMap<T, Boolean>()).apply { addAll(elements) }
}

fun<T> weakSetOf(vararg elements: T): Set<T> {
    return weakMutableSetOf(*elements)
}
