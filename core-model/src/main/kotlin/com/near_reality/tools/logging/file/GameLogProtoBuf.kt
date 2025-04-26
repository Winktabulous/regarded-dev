@file:OptIn(InternalSerializationApi::class, ExperimentalSerializationApi::class)

package com.near_reality.tools.logging.file

import com.near_reality.tools.logging.GameLogMessage
import com.near_reality.tools.logging.nrSerializerModule
import kotlinx.datetime.*
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.InternalSerializationApi
import kotlinx.serialization.decodeFromByteArray
import kotlinx.serialization.json.Json
import kotlinx.serialization.protobuf.ProtoBuf
import java.io.File
import java.nio.ByteBuffer
import java.nio.file.Paths
import java.time.format.DateTimeFormatter
import kotlin.reflect.KClass
import kotlin.time.Duration
import kotlin.time.Duration.Companion.days

val nrProtoBuf = ProtoBuf {
    serializersModule = nrSerializerModule
    encodeDefaults = true
}

@OptIn(InternalSerializationApi::class)
val nrJson = Json {
    serializersModule = nrSerializerModule
    encodeDefaults = true
}

inline fun<reified M : GameLogMessage> readGameLogsSince(lookBackDuration: Duration): Map<Instant, List<M>> {
    val now = Clock.System.now()
    val previous = now - lookBackDuration
    var current : Instant = previous
    val map = mutableMapOf<Instant, List<M>>()
    do {
        map[current] = readGameLogsFromTime(current)
        current += 1.days
    } while (current < now)
    return map
}

inline fun<reified M : GameLogMessage> readGameLogsFromTime(instant: Instant): List<M> {
    val file = getFile(instant.formatAsFolderName(), M::class)
    return file.readGameLogMessages()
}

inline fun<reified M : GameLogMessage> File.readGameLogMessages(): List<M> {
    val messages = mutableListOf<M>()
    val messagesBytes = ByteBuffer.wrap(readBytes())
    while (messagesBytes.hasRemaining()) {
        val messageSize = messagesBytes.int
        val messageBytes = ByteArray(messageSize).apply(messagesBytes::get)
        messages += nrProtoBuf.decodeFromByteArray<GameLogMessage>(messageBytes) as M

    }
    return messages
}

fun getFile(folderName: String, kClass: KClass<out GameLogMessage>): File =
    Paths.get("logs", "game", folderName, "${kClass.simpleName!!}.proto.dat")
        .toFile()
        .apply { if (!parentFile.exists()) parentFile.mkdirs() }

private val folderFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy")

fun Instant.formatAsFolderName(): String =
    folderFormatter.format(toLocalDateTime(TimeZone.UTC).toJavaLocalDateTime())
