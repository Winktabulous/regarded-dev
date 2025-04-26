package com.near_reality.tools.updater

import com.zenyte.game.task.WorldTasksManager
import com.zenyte.game.world.World
import io.netty.channel.ChannelHandlerContext
import io.netty.channel.SimpleChannelInboundHandler
import org.slf4j.LoggerFactory

/**
 * @author Jire
 */
class UpdaterChannelHandler : SimpleChannelInboundHandler<String>() {

    override fun channelRead0(ctx: ChannelHandlerContext, msg: String) {
        logger.info("Received command {}", msg)

        if (msg.startsWith("update")) {
            val time = msg.substringAfter("update").trim().toIntOrNull()
            if (time == null) {
                logger.error("invalid time parameter, must be an integer")
            } else {
                WorldTasksManager.schedule {
                    World.startUpdateTimer(time)
                }
            }
        }

        ctx.close()
    }

    private companion object {
        val logger = LoggerFactory.getLogger(UpdaterChannelHandler::class.java)
    }

}
