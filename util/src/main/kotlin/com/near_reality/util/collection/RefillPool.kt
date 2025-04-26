package com.near_reality.util.collection

fun<T : Any> refillPoolOf(vararg elements: T) = RefillPool<T>().apply { elements.forEach { register(it) } }


/**
 * Manages a [pool] of [elements][T] that is automatically [refilled][refill] with [all] when empty.
 *
 * @author Stan van der Bend
 */
class RefillPool<T : Any> {

    private val all = mutableSetOf<T>()
    private val disabled = mutableSetOf<T>()
    private val pool = ArrayDeque<T>()
    private lateinit var lastPolled: T

    /**
     * Registers a [event] to the [all] and [pool] list.
     */
    fun register(event: T) {
        all += event
        pool += event
    }

    /**
     * Disables a [event], removing it from the [pool] and adding it to the [disabled] list.
     * Disabled events won't be added to the [pool] on a [refill].
     */
    fun disable(event: T) {
        disabled += event
        pool -= event
    }

    /**
     * Enables a [event], removing it from the [disabled] list and adding it to the [pool].
     */
    fun enable(event: T) {
        disabled -= event
        pool += event
    }

    /**
     * Checks if a [event] is disabled, i.e. it is in the [disabled] list.
     */
    fun isDisabled(event: T): Boolean =
        disabled.contains(event)

    /**
     * Returns all [T]s.
     */
    fun all() : Set<T> = all

    /**
     * Removes and returns the first [T] from the [pool],
     * if empty the [pool] is [refilled][refill] first, if still empty `null` is returned.
     *
     * No event is polled consecutively (assuming there are at least 2 unique events).
     */
    fun poll(): T? {
        if (pool.isEmpty())
            refill()
        if (pool.isEmpty())
            return null
        return ensureNoConsecutivePoll()
    }

    private fun ensureNoConsecutivePoll(): T {
        var next = pool.removeFirst()
        if (this::lastPolled.isInitialized) {
            if (lastPolled == next) {
                if (pool.isNotEmpty()) {
                    next = pool.removeFirst()
                    pool.addLast(lastPolled)
                }
            }
        }
        lastPolled = next
        return next
    }

    /**
     * Refills the [pool] with [all] [T]s that are not [disabled],
     * this always shuffles the [pool] before returning.
     */
    private fun refill() {
        pool.clear()
        pool += (all - disabled)
        pool.shuffle()
    }
}
