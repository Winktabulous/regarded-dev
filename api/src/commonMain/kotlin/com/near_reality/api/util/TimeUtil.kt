package com.near_reality.api.util

import kotlinx.datetime.Clock
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toInstant
import kotlinx.datetime.toLocalDateTime
import kotlin.time.Duration

/**
 * Represents the default [TimeZone] used by the Database, that being [TimeZone.UTC].
 */
val defaultTimeZone = TimeZone.UTC

/**
 * Returns the [current time][Clock.System.now] as an instance of [LocalDateTime] in the [defaultTimeZone].
 */
val currentTime
    get() = Clock.System.now().toLocalDateTime(defaultTimeZone)

fun currentTimePlus(duration: Duration) = (Clock.System.now() + duration).toLocalDateTime(defaultTimeZone)

operator fun LocalDateTime.minus(other: LocalDateTime): Duration = Clock.System.now() - other.toInstant(defaultTimeZone)
