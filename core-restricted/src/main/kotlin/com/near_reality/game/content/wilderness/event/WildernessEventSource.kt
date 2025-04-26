package com.near_reality.game.content.wilderness.event

/**
 * Represents the source of a [WildernessEvent], this determines how the event is started.
 *
 * @author Stan van der Bend
 */
sealed class WildernessEventSource {

    /**
     * Represents a standalone event, meaning it runs parallel to other events.
     */
    data object Standalone : WildernessEventSource()

    /**
     * Represents a random pool event, meaning it is randomly selected from a pool of events,
     * only one event from the pool will be active at a time.
     */
    data object RandomPool : WildernessEventSource()
}
