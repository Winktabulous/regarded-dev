@file:OptIn(ExperimentalSerializationApi::class, InternalSerializationApi::class)

package com.near_reality.tools.logging.file

import com.near_reality.tools.logging.GameLogMessage
import io.netty.buffer.ByteBuf
import io.netty.buffer.Unpooled
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.InternalSerializationApi
import kotlinx.serialization.encodeToByteArray
import org.slf4j.LoggerFactory
import java.util.concurrent.ConcurrentHashMap
import kotlin.reflect.KClass

/**
 * Writes [GameLogMessage] instances to a designated file.
 *
 * @author Stan van der Bend
 */
@Suppress("unused")
object GameLoggerFileAppender {

    /**
     * If buffer size is reached for a specific message type, it is written to the file.
     */
    private const val BUFFER_SIZE = 8192

    /**
     * A map containing buffers for each message type.
     */
    private val messageWriteBuffers = ConcurrentHashMap<KClass<out GameLogMessage>, ByteBuf>()

    /**
     * A logger instance.
     */
    private val logger = LoggerFactory.getLogger(GameLoggerFileAppender::class.java)

    /**
     * The last name of the folder that a file was written to,
     * this is derived from [formatAsFolderName].
     */
    private lateinit var lastFolderName: String

    fun writeMaybeFlush(message: GameLogMessage) {

        val messageClass = message::class
        val folderName = message.time.formatAsFolderName()

        if (!this::lastFolderName.isInitialized || lastFolderName != folderName) {
            if (this::lastFolderName.isInitialized)
                messageWriteBuffers
                    .remove(messageClass)
                    ?.apply { appendToFile(lastFolderName, messageClass) }
            lastFolderName = folderName
        }

        var buffer = messageWriteBuffers.getOrPut(messageClass, this::newBuffer)
        val encodedMessage = nrProtoBuf.encodeToByteArray(message)
        val encodedMessageSize = encodedMessage.size

        if (!buffer.isWritable(encodedMessageSize + Int.SIZE_BYTES)) {
            buffer.appendToFile(folderName, messageClass)
            buffer = newBuffer()
            messageWriteBuffers[messageClass] = buffer
        }

        buffer.writeInt(encodedMessageSize)
        buffer.writeBytes(encodedMessage)
    }

    fun close() {
        logger.info("Closing")
        if (this::lastFolderName.isInitialized)
            for ((messageClass, buffer) in messageWriteBuffers)
                buffer.appendToFile(lastFolderName, messageClass)
    }

    private fun ByteBuf.appendToFile(folderName: String, kClass: KClass<out GameLogMessage>) {
        val array = ByteArray(readableBytes()).apply(::readBytes)
        getFile(folderName, kClass).appendBytes(array)
    }

    private fun newBuffer(): ByteBuf = Unpooled.buffer(BUFFER_SIZE)
}
