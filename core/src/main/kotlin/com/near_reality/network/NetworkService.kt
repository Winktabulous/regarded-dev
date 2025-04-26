package com.near_reality.network

import com.google.common.util.concurrent.ThreadFactoryBuilder
import io.netty.bootstrap.ServerBootstrap
import io.netty.channel.*
import io.netty.channel.epoll.Epoll
import io.netty.channel.epoll.EpollEventLoopGroup
import io.netty.channel.epoll.EpollServerSocketChannel
import io.netty.channel.kqueue.KQueue
import io.netty.channel.kqueue.KQueueEventLoopGroup
import io.netty.channel.kqueue.KQueueServerSocketChannel
import io.netty.channel.nio.NioEventLoopGroup
import io.netty.channel.socket.nio.NioServerSocketChannel
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.util.concurrent.ThreadFactory


/**
 * @author Jire (clown)
 * @author John J. Woloszyk
 */
class NetworkService {

    private val bossGroup = createEventLoopGroup(0, ThreadFactoryBuilder().setNameFormat("netty-boss-%d").build());
    private val workerGroup = createEventLoopGroup(0, ThreadFactoryBuilder().setNameFormat("netty-worker-%d").build());

    private val bootstrap = ServerBootstrap()

    fun initialize() {
        bootstrap.apply {
            group(bossGroup, workerGroup)
            channel(getServerSocketChannelClass())
            childHandler(PipelineFactory())
            childOption(ChannelOption.TCP_NODELAY, true)
            childOption(ChannelOption.WRITE_BUFFER_WATER_MARK, WriteBufferWaterMark(5000, 40_000))
            childOption(ChannelOption.CONNECT_TIMEOUT_MILLIS, 30_000)
        }
    }

    fun bind(port: Int): ChannelFuture {
        logger.debug("Binding to port {} with group type \"{}\"", port, bossGroup::class.java)
        return bootstrap.bind(port)
    }

    fun createEventLoopGroup(threads: Int = 0, factory: ThreadFactory): EventLoopGroup =
        when {
//            IOUring.isAvailable() -> IOUringEventLoopGroup(threads, factory)
            Epoll.isAvailable() -> EpollEventLoopGroup(threads, factory)
            KQueue.isAvailable() -> KQueueEventLoopGroup(threads, factory)
            else -> NioEventLoopGroup(threads, factory)
        }

    fun getServerSocketChannelClass(): Class<out ServerChannel> =
        when {
//            IOUring.isAvailable() -> IOUringServerSocketChannel::class.java
            Epoll.isAvailable() -> EpollServerSocketChannel::class.java
            KQueue.isAvailable() -> KQueueServerSocketChannel::class.java
            else -> NioServerSocketChannel::class.java
        }
    fun listen(port: Int): ChannelFuture =
        bind(port)
            .addListener {
                logger.info("Listening on port {}", port)
            }

    companion object {
        const val USE_NEW_PROTOCOL = false

        private val logger: Logger = LoggerFactory.getLogger(NetworkService::class.java)
    }

}
