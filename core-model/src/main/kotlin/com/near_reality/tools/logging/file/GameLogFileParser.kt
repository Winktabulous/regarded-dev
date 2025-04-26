package com.near_reality.tools.logging.file

import com.near_reality.tools.logging.GameLogMessage
import kotlinx.datetime.Clock
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.InternalSerializationApi

@ExperimentalSerializationApi
@InternalSerializationApi
fun main() {
    val publicMessageLogs = readGameLogsFromTime<GameLogMessage.Message.Public>(Clock.System.now())
    publicMessageLogs.forEach {
        println(it)
    }
}
