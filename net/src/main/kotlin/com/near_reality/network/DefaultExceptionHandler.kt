package com.near_reality.network

import io.netty.channel.ChannelDuplexHandler
import io.netty.channel.ChannelHandlerContext
import io.netty.handler.timeout.ReadTimeoutException
import it.unimi.dsi.fastutil.objects.ObjectSet
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.nio.channels.ClosedChannelException

/**
 * @author Jire
 */
class DefaultExceptionHandler : ChannelDuplexHandler() {

    @Deprecated("Deprecated in Java")
    override fun exceptionCaught(ctx: ChannelHandlerContext, cause: Throwable) {
        ctx.close()

        if (ReadTimeoutException.INSTANCE == cause
            || cause is ClosedChannelException
            || ignoredExceptionMessages.contains(cause.message)
        ) return

        logger.error("", cause)
    }

    private companion object {
        val logger: Logger = LoggerFactory.getLogger(DefaultExceptionHandler::class.java)

        val ignoredExceptionMessages: ObjectSet<String> = ObjectSet.of(
            "An established connection was aborted by the software in your host machine",
            "Connection reset"
        )
    }

}