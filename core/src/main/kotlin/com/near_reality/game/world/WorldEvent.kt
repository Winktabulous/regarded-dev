package com.near_reality.game.world

/**
 * Represents an event instantiated in the [WorldThread].
 *
 * @author Stan van der Bend
 */
sealed interface WorldEvent {

    /**
     * Represents a world tick, posted at the very start of a new world cycle.
     */
    class Tick(val tick: Long): WorldEvent

}
