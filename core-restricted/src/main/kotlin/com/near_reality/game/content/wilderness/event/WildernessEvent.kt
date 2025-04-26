package com.near_reality.game.content.wilderness.event

import com.zenyte.game.world.WorldThread
import com.zenyte.game.world.entity.Location
import java.util.*
import kotlin.time.Duration
import kotlin.time.Duration.Companion.milliseconds
import kotlin.time.Duration.Companion.minutes

/**
 * Represents a wilderness event, this should be a singleton object.
 *
 * @author Stan van der Bend
 */
interface WildernessEvent {

    /**
     * Start the event, this is invoked after [delayUntilStart] has passed after the even is scheduled.
     * Returns an optional message that is broadcast to the world.
     */
    fun start(): Optional<String>

    /**
     * Cancel the event, this is invoked when an administrator manually cancels the event.
     */
    fun cancel()

    /**
     * Check if the event has been completed, if so the next one is scheduled.
     */
    fun completed(): Boolean

    /**
     * The delay until the event starts after it has been scheduled.
     */
    fun delayUntilStart(): Duration = defaultDelayUntilStart

    /**
     * The name of the event.
     */
    fun name(): String = javaClass.simpleName

    /**
     * The dialogue that is displayed when the event is in a certain state, used for player dialogue.
     */
    fun stateDialogue(state: State): String

    /**
     * The teleport location for the event, if any.
     */
    fun teleportLocation() : Optional<Location> = Optional.empty()

    /**
     * The source of the event.
     */
    fun source(): WildernessEventSource = WildernessEventSource.RandomPool

    companion object {

        /**
         * The default delay until the event starts, can be modified with the `::managewevents` command.
         */
        var defaultDelayUntilStart = 60.minutes
    }

    /**
     * The internal state of the event.
     */
    sealed interface State {

        /**
         * The event is scheduled to start [at a later time][startTick].
         */
        data class Scheduled(val startTick: Long) : State {

            /**
             * The duration until the event starts.
             */
            val timeLeft get() = ((startTick - WorldThread.getCurrentCycle()) * 600).milliseconds
        }

        /**
         * The event is currently active.
         */
        data object Active : State
    }
}
