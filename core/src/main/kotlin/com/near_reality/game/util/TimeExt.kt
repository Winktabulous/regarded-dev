package com.near_reality.game.util

import com.zenyte.game.world.WorldThread
import kotlin.time.Duration
import kotlin.time.Duration.Companion.days
import kotlin.time.Duration.Companion.hours
import kotlin.time.Duration.Companion.minutes
import kotlin.time.Duration.Companion.seconds

val Duration.inWholeTicks: Long get() = (inWholeMilliseconds / 600L)

class WorldTimer(private val duration: Duration) {

    private var start: Long = WorldThread.getCurrentCycle()

    fun start() {
        start = WorldThread.getCurrentCycle()
    }

    fun ticksRemaining(): Long {
        return duration.inWholeTicks - (WorldThread.getCurrentCycle() - start)
    }

    fun secondsRemaining(): Long {
        return (ticksRemaining() * 600) / 1000
    }

    fun minutesRemaining(): Long {
        return secondsRemaining() / 60
    }

    fun durationRemaining(): Duration {
        return secondsRemaining().seconds
    }

    fun ifDurationRemainingIsExactlyOneOf(durations: List<Duration>, action: (Duration) -> Unit) {
        val ticks = ticksRemaining()
        val duration = durations.find { it.inWholeTicks == ticks }?:return
        action(duration)
    }

    fun every(duration: Duration, action: () -> Unit) {
        if (ticksRemaining() % duration.inWholeTicks == 0L)
            action()
    }

    fun elapsed(): Boolean {
        return WorldThread.getCurrentCycle() - start >= duration.inWholeTicks
    }
}

val Duration.formattedString: String
    get() {
        val daysLeft = inWholeDays
        val hoursLeft = inWholeHours - daysLeft.days.inWholeHours
        val minutesLeft = inWholeMinutes - hoursLeft.hours.inWholeMinutes
        val secondsLeft = inWholeSeconds - minutesLeft.minutes.inWholeSeconds
        return when {
            daysLeft > 0 -> buildString {
                append(" $daysLeft ${if(daysLeft > 1) "days" else "day"}")
                if (hoursLeft > 0) append(" and $hoursLeft ${if(hoursLeft > 1) "hours" else "hour"}")
            }
            hoursLeft > 0 -> buildString {
                append(" $hoursLeft ${if(hoursLeft > 1) "hours" else "hour"}")
                if (minutesLeft > 0) append(" and $minutesLeft ${if(minutesLeft > 1) "minutes" else "minute"}")
            }
            minutesLeft > 0 -> buildString {
                append(" $minutesLeft ${if(minutesLeft > 1) "minutes" else "minute"}")
                if (secondsLeft > 0) append(" and $secondsLeft ${if(secondsLeft > 1) "seconds" else "second"}")
            }
            secondsLeft > 0 -> " $secondsLeft ${if(secondsLeft > 1) "seconds" else "second"}"
            else -> " less than a second"
        }
    }
