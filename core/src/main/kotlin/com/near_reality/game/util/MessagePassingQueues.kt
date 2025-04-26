package com.near_reality.game.util

import org.jctools.queues.MessagePassingQueue

/**
 * @author Jire
 */
object MessagePassingQueues {

    /**
     * Used to drain from a `MessagePassingQueue`.
     */
    fun interface Drainer<T> {

        /**
         * @return `true` if the queue should continue being drained, `false` otherwise.
         */
        fun continueDraining(e: T): Boolean

    }

    @JvmStatic
    @JvmOverloads
    inline fun <T> drain(
        queue: MessagePassingQueue<T>,
        limit: Int = queue.capacity(),
        consumer: Drainer<T>
    ) {
        var i = 0
        var next: T
        while (i < limit) {
            next = queue.poll() ?: break
            consumer.continueDraining(next)
            i++
        }
    }

}