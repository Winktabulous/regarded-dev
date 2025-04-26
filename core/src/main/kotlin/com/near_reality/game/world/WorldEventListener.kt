package com.near_reality.game.world

/**
 * Represents a functional interface that can listens to [world events][T].
 *
 * @author Stan van der Bend
 */
fun interface WorldEventListener<T : WorldEvent> {

    /**
     * Invoked when the [event] is posted.
     */
    fun on(event: T)
}
