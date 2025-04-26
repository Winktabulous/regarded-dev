package com.near_reality.game.content.wilderness.event

import com.near_reality.game.util.WorldTimer
import com.near_reality.util.collection.RefillPool
import com.zenyte.game.util.Colour
import com.zenyte.game.world.WorldThread
import com.zenyte.game.world.broadcasts.BroadcastType
import com.zenyte.game.world.broadcasts.WorldBroadcasts
import com.zenyte.utils.TimeUnit
import org.slf4j.LoggerFactory
import kotlin.time.Duration
import kotlin.time.Duration.Companion.seconds

/**
 * Manages the wilderness events, scheduling and starting them.
 *
 * @author Stan van der Bend
 */
object WildernessEventManager {

    private val logger = LoggerFactory.getLogger(WildernessEventManager::class.java)

    /**
     * The [RefillPool] that contains all the wilderness events.
     */
    internal val randomEventPool = RefillPool<WildernessEvent>()
    internal val standaloneEventsWithState = mutableMapOf<WildernessEvent, State>()

    private val firstRandomEventStartTimer = WorldTimer(20.seconds)
    private val firstStandaloneEventStartTimer = WorldTimer(30.seconds)

    /**
     * The current state of the wilderness event manager.
     */
    internal var randomEventState: State = State.None
        private set

    fun registerEvent(event: WildernessEvent) {
        when(event.source()) {
            WildernessEventSource.RandomPool -> randomEventPool.register(event)
            WildernessEventSource.Standalone -> standaloneEventsWithState[event] = State.None
        }
    }

    /**
     * Determine the [state][WildernessEvent.State] of the provided [event],
     * returning `null` if the event is not scheduled or active.
     */
    fun stateOf(event: WildernessEvent): WildernessEvent.State? {
        val state: State? = when(event.source()) {
            WildernessEventSource.RandomPool -> randomEventState.takeIf { state -> state.belongsTo(event) }
            WildernessEventSource.Standalone -> standaloneEventsWithState[event]
        }
        return when (state) {
            is State.Scheduled -> WildernessEvent.State.Scheduled(state.startTick)
            is State.Active -> WildernessEvent.State.Active
            else -> null
        }
    }

    /**
     * Processes the wilderness events, scheduling and starting them, invoked every world cycle.
     */
    internal fun process() {
        if (firstRandomEventStartTimer.elapsed())
            processRandomEvent()
        if (firstStandaloneEventStartTimer.elapsed())
            processStandaloneEvents()
    }

    internal fun startedUp() =
        firstRandomEventStartTimer.elapsed() && firstStandaloneEventStartTimer.elapsed()

    private fun processRandomEvent() {
        when(val state = randomEventState) {
            is State.None -> {
                scheduleNextRandomEvent()
            }
            is State.Scheduled -> {
                if (state.ready())
                    startEvent(state.event)
            }
            is State.Active -> {
                if (state.event.completed())
                    randomEventState = State.None
            }
        }
    }

    private fun processStandaloneEvents() {
        // Make a copy first to avoid concurrent modification.
        val currentEventsWithState = standaloneEventsWithState.toMap()
        currentEventsWithState.forEach { (event, state) ->
            when(state) {
                is State.None -> {
                    scheduleEvent(event)
                }
                is State.Scheduled -> {
                    if (state.ready())
                        startEvent(event)
                }
                is State.Active -> {
                    if (event.completed())
                        standaloneEventsWithState[event] = State.None
                }
            }
        }
    }


    /**
     * Schedules the next wilderness event, if any are available.
     */
    internal fun scheduleNextRandomEvent(): Boolean {
        randomEventState = State.None
        val event = randomEventPool.poll()
        if (event == null) {
            logger.warn("No wilderness events available.")
            return false
        }
        scheduleEvent(event)
        return true
    }

    /**
     * Schedules the argued [event] to start after the argued [delay].
     */
    internal fun scheduleEvent(event: WildernessEvent, delay: Duration = event.delayUntilStart()) {
        setState(
            event,
            State.Scheduled(event = event, startTick = WorldThread.getCurrentCycle() + delay.inWholeTicks)
        )
        logger.info("Scheduled wilderness event: ${event.name()}.")
    }

    /**
     * Starts the argued [event], setting the [randomEventState] to [State.Active].
     */
    internal fun startEvent(event: WildernessEvent) {
        setState(event, State.Active(event))
        val optionalBroadcast = event.start()
        optionalBroadcast.ifPresent {
            WorldBroadcasts.broadcast(null, BroadcastType.WILDERNESS_EVENT, Colour.RED.wrap(it))
        }
    }

    private fun setState(event: WildernessEvent, newState: State) {
        when (event.source()) {
            WildernessEventSource.RandomPool -> randomEventState = newState
            WildernessEventSource.Standalone -> standaloneEventsWithState[event] = newState
        }
    }

    private val Duration.inWholeTicks get(): Long = this.inWholeMilliseconds / TimeUnit.TICKS.toMillis(1)

    /**
     * The internal state of the wilderness event manager.
     */
    sealed interface State {

        fun toDialogueString(): String =
            "<img=68> <col=B22222><shad=000000>${toString()}"

        /**
         * The [event] is scheduled to start [at a later time][startTick] which is when it becomes [Active].
         */
        data class Scheduled(val event: WildernessEvent, val startTick: Long) : State {

            /**
             * Returns `true` if the event is ready to start.
             */
            fun ready() = WorldThread.getCurrentCycle() >= startTick

            override fun toString(): String =
                event.stateDialogue(WildernessEvent.State.Scheduled(startTick))
        }

        /**
         * The [event] is currently active.
         */
        data class Active(val event: WildernessEvent) : State {

            override fun toString(): String =
                event.stateDialogue(WildernessEvent.State.Active)
        }

        /**
         * No wilderness events scheduled.
         */
        data object None : State {
            override fun toString(): String =
                "No wilderness events scheduled."
        }

        fun belongsTo(event: WildernessEvent) = when(this) {
            is Scheduled -> event == this.event
            is Active -> event == this.event
            else -> false
        }
    }
}
