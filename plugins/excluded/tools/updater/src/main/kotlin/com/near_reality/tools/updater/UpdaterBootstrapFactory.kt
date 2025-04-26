package com.near_reality.tools.updater

import com.near_reality.network.BootstrapFactory
import io.netty.channel.EventLoopGroup
import io.netty.channel.ServerChannel

/**
 * @author Jire
 */
object UpdaterBootstrapFactory : BootstrapFactory {

    override fun createServerBootstrap(
        parentGroup: EventLoopGroup,
        childGroup: EventLoopGroup,
        channel: Class<out ServerChannel>
    ) = super.createServerBootstrap(parentGroup, childGroup, channel).apply {
        childHandler(UpdaterChannelInitializer)
    }

}
