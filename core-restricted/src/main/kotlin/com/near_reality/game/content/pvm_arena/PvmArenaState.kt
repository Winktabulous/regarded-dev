package com.near_reality.game.content.pvm_arena

import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import kotlin.time.Duration
import kotlin.time.Duration.Companion.hours
import kotlin.time.Duration.Companion.minutes

/**
 * Represents the state of the PvM Arena activity.
 *
 * @author Stan van der Bend
 */
internal sealed class PvmArenaState {

    /**
     * Whether players can join the fight arena through the team portals.
     */
    val canJoinArena : Boolean
        get() = this is Open || this is StartingSoon

    /**
     * Activity is waiting to be opened by a staff members, players can not yet join a team.
     */
    class Idle : Timed(6.hours) {

        fun isOpening() = isTimeOver()
    }

    /**
     * Players can now join a team.
     */
    class Open : Timed(10.minutes) {

        fun isStartingSoon() = isTimeOver()
    }

    /**
     * The activity will start soon, players can still join a team.
     */
    class StartingSoon : Timed(1.minutes) {

        fun isStarting() = isTimeOver()
    }

    /**
     * The activity has started, waves of monsters will start spawning.
     */
    class Started : Timed(30.minutes) {

        fun isTimedOut() = isTimeOver()
    }

    /**
     * The activity ended, either because one of the teams defeated all monster waves first, or ran out of time.
     */
    sealed class Ended : PvmArenaState() {

        data class WonBy(val winningTeam: PvmArenaTeam) : Ended()

        data object Tie : Ended()

        data object TimedOut : Ended()
        data object Canceled : Ended()
    }

    internal sealed class Timed(duration: Duration) : PvmArenaState() {
        var maxDuration = duration
        val startTime: Instant =Clock.System.now()
        val endTime get() = startTime + maxDuration
        val timeLeft get() = endTime - Clock.System.now()

        fun isTimeOver() = Clock.System.now() > endTime
    }
}
