@file:Suppress("unused")

package com.near_reality.tools.updater

import com.google.common.eventbus.Subscribe
import com.near_reality.network.BootstrapFactory
import com.zenyte.plugins.events.ServerLaunchEvent
import org.slf4j.LoggerFactory

/**
 * A detached server that can receive an "update <time>" message locally through a socket.
 *
 * This will activate the update timer and schedule server shutdown.
 *
 * @author Jire
 */
object Updater {

    private const val DEFAULT_PORT = 9090

    private val logger = LoggerFactory.getLogger(Updater::class.java)

    @Subscribe
    @JvmStatic
    fun onServerLaunch(@Suppress("UNUSED_PARAMETER") event: ServerLaunchEvent) = start()

    /**
     * Start the service on the argued [port].
     */
    @JvmOverloads
    @JvmStatic
    fun start(port: Int = DEFAULT_PORT) {
        val bootstrapFactory: BootstrapFactory = UpdaterBootstrapFactory

        val parentGroup = bootstrapFactory.createEventLoopGroup(1)
        val childGroup = bootstrapFactory.createEventLoopGroup(1)
        bootstrapFactory
            .createServerBootstrap(parentGroup, childGroup)
            .bind(port)
            .syncUninterruptibly()

        logger.info("Started updater service on port {}", port)
    }

}
