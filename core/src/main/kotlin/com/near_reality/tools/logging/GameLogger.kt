

package com.near_reality.tools.logging

import com.near_reality.api.GameDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.asCoroutineDispatcher
import kotlinx.coroutines.channels.SendChannel
import kotlinx.coroutines.launch
import org.slf4j.LoggerFactory
import org.slf4j.event.Level
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.Executors

/**
 * Utility for logging [game log messages][GameLogMessage].
 *
 * @author Stan van der Bend
 */
object GameLogger {

    /**
     * [CoroutineScope] for sending log messages to [actors], using a single thread.
     */
    private val sendScope = CoroutineScope(
        Executors
            .newFixedThreadPool(1)
            .asCoroutineDispatcher()
    )

    /**
     * A list of actors that can receive [GameLogMessage].
     */
    private val actors = ConcurrentHashMap<String, SendChannel<GameLogMessage>>()

    /**
     * A logger instance.
     */
    private val logger = LoggerFactory.getLogger(GameLogger::class.java)


    /**
     * Submits the argued [provideMessage] which is only invoked is there is at least one [actor][actors]
     * that listens to messages of its type and [level].
     */
    @JvmStatic
    fun <M : GameLogMessage> log(level: Level = Level.INFO, provideMessage: () -> M) {
        try {
            val message = provideMessage()
            sendScope.launch {
                try {
                    GameDatabase.append(message)
                } catch (e: Exception) {
                    logger.warn("Failed to submit log", e)
                }
            }
        } catch (e: Exception) {
            logger.warn("Failed to submit log", e)
        }
    }
}
