package com.near_reality.network

import com.google.common.io.BaseEncoding
import com.near_reality.crypto.ThreadLocalSecureRandom
import com.near_reality.network.NetworkPipelineConstants.DECODER
import com.near_reality.network.NetworkPipelineConstants.ENCODER
import com.near_reality.network.NetworkPipelineConstants.JS5_CONNECTION_IP_TOS
import com.near_reality.network.js5.JS5Request
import com.near_reality.network.js5.JS5RequestDecoder
import com.near_reality.network.js5.JS5Responses
import com.near_reality.network.login.codec.LoginDecoder
import com.near_reality.network.login.packet.LoginPacketIn
import com.near_reality.network.proofofwork.ProofOfWork
import com.near_reality.network.proofofwork.ProofOfWorkResponse
import com.near_reality.network.proofofwork.ProofOfWorkResponseDecoder
import com.near_reality.util.logging.LazyLoggerWithPrefix
import com.zenyte.game.packet.GamePacketEncoder
import com.zenyte.game.packet.GamePacketEncoderMode
import com.zenyte.game.packet.`in`.ClientProtEvent
import com.zenyte.game.packet.`in`.event.EventMouseIdleEvent
import com.zenyte.game.world.World
import com.zenyte.game.world.WorldThread
import com.zenyte.game.world.entity.player.LogoutType
import com.zenyte.game.world.entity.player.Player
import com.zenyte.game.world.entity.player.PlayerInformation
import com.zenyte.game.world.region.area.wilderness.WildernessArea
import com.zenyte.net.ClientResponse
import com.zenyte.net.NetworkConstants
import com.zenyte.net.NetworkConstants.MAX_INBOUND_PACKETS_PER_TICK
import com.zenyte.net.NetworkConstants.MAX_OUTBOUND_PACKETS_PER_TICK
import com.zenyte.net.NetworkConstants.MAX_PENDING_OUTBOUND_PACKETS
import com.zenyte.net.PacketIn
import com.zenyte.net.Session
import com.zenyte.net.game.ServerEvent
import com.zenyte.net.game.packet.GamePacketOut
import com.zenyte.net.handshake.packet.HandshakePacketIn
import com.zenyte.net.handshake.packet.HandshakeResponse
import com.zenyte.net.handshake.packet.inc.InitGameConnection
import com.zenyte.net.handshake.packet.inc.InitJS5Connection
import com.zenyte.utils.TimeUnit
import io.netty.buffer.ByteBuf
import io.netty.channel.*
import io.netty.handler.codec.DecoderException
import io.netty.util.concurrent.GenericFutureListener
import org.jctools.queues.MessagePassingQueue
import org.jctools.queues.SpscArrayQueue
import org.slf4j.LoggerFactory
import kotlin.reflect.KClass

/**
 * Single client connection handler built around NIO, full auto-read write with the exception
 * of a single pause performed while waiting on IO-bound tasks during login. This was created to
 * replace the overcomplicated system that was previously written that was not built to scale.
 *
 * The actual handling of incoming messages is done by the [subHandler], which the master can freely swap around,
 * once a handler is created once, it will be cached and re-used once switched back to, for state persistence.
 *
 * @author John J. Woloszyk
 * @author Stan van der Bend
 */
class MasterHandler : ChannelInboundHandlerAdapter(), Session {

    private val subHandlerCache = mutableMapOf<KClass<*>, SubHandler<*>>()
    private var subHandler: SubHandler<*>? = null

    private val handshakeSubHandler get() = getOrSwitchOrPutHandler { HandshakeSubHandler(this) }
    private val js5SubHandler get() = getOrSwitchOrPutHandler { Js5SubHandler(this) }
    private val loginSubHandler get() = getOrSwitchOrPutHandler { LoginSubHandler(this) }
    private val worldSubHandler get() = getOrSwitchOrPutHandler { WorldSubHandler(this) }

    internal val logger: LazyLoggerWithPrefix = object : LazyLoggerWithPrefix(
        backingLogger = LoggerFactory.getLogger("Channel")
    ) {
        override fun prefixProvider(): String = "$subHandler -> "
        override fun showDebugLogs() = player?.isDeveloper == true || super.showDebugLogs()
    }

    internal var ctx: ChannelHandlerContext? = null
    internal var player: Player? = null

    @Override
    override fun channelRead(ctx: ChannelHandlerContext, msg: Any) {
        when (msg) {
            is JS5Request -> js5SubHandler.read(ctx, msg)
            else -> {
                /* We are always updating the currently used context upon completion of any read, except JS5 */
                this.ctx = ctx
                when (msg) {
                    is HandshakePacketIn -> handshakeSubHandler.read(ctx, msg)
                    is LoginPacketIn,
                    is ProofOfWorkResponse -> loginSubHandler.read(ctx, msg as PacketIn)
                    is ClientProtEvent -> worldSubHandler.read(ctx, msg)
                    else -> logger.warn { "channelRead() -> Unhandled message: $msg" }
                }
            }
        }
    }

    private inline fun <reified R : SubHandler<*>> getOrSwitchOrPutHandler(provider: () -> R): R {
        if (subHandler !is R) {
            val newReader = subHandlerCache.getOrPut(R::class, provider)
            val oldReader = this.subHandler
            this.subHandler = newReader
            logger.trace { "Switched sub-handler from $oldReader to $newReader" }
        }
        return subHandler as R
    }

    override fun channelReadComplete(ctx: ChannelHandlerContext) {
        subHandler?.onReadComplete(ctx)
    }

    override fun channelActive(ctx: ChannelHandlerContext) {
        ctx.channel().closeFuture().addListener(CloseListener(this))
    }

    override fun channelInactive(ctx: ChannelHandlerContext) {
        subHandler?.onChannelInactive(ctx)
    }

    override fun channelWritabilityChanged(ctx: ChannelHandlerContext) {
        subHandler?.onChannelWritabilityChanged(ctx)
    }

    fun showDebugPrints() = player?.isDeveloper == true || NetworkConstants.NETWORKING_DEBUG

    @Deprecated("Deprecated in Java")
    override fun exceptionCaught(ctx: ChannelHandlerContext, cause: Throwable) =
        logger.error(cause) { "Exception caught!" }

    override fun toString(): String = "MasterHandler-$subHandler)"

    /*
     * START - Session method implementation
     *
     * Can only be used if sub handler is instance of session,
     * otherwise any session method invocation will throw.
     */

    override fun flush() =
        getSubHandlerIfSessionOrThrow().flush()

    override fun process(): Boolean =
        getSubHandlerIfSessionOrThrow().process()

    override fun close() =
        getSubHandlerIfSessionOrThrow().close()

    override fun send(event: ServerEvent): Boolean =
        getSubHandlerIfSessionOrThrow().send(event)

    override fun getHostAddress(): String =
        getSubHandlerIfSessionOrThrow().getHostAddress()

    override fun isActive(): Boolean =
        getSubHandlerIfSessionOrThrow().isActive()

    override fun isExpired(): Boolean =
        getSubHandlerIfSessionOrThrow().isExpired()

    private fun getSubHandlerIfSessionOrThrow(): Session {
        return when (val activeSubHandler = subHandler) {
            is Session -> activeSubHandler
            is LoginSubHandler -> worldSubHandler
            else -> throw IllegalStateException("Active sub-handler $activeSubHandler is not a login sub handler or world sub handler")
        }
    }

    /*
     * End - Session method implementation
     */
}

private class CloseListener(private val parent: MasterHandler) : GenericFutureListener<ChannelFuture> {

    private val logger by parent::logger

    override fun operationComplete(future: ChannelFuture) {
        if (!future.isSuccess) {
            logger.error(future.cause()) { "CloseListener -> Failed to close channel" }
        } else
            logger.info {"CloseListener -> Channel closed successfully" }
    }
}

private sealed class SubHandler<T : Any>(protected val parent: MasterHandler) {

    protected val ctx by parent::ctx
    protected val logger by parent::logger

    abstract fun read(ctx: ChannelHandlerContext, msg: T)

    open fun onReadComplete(ctx: ChannelHandlerContext) = Unit

    open fun onChannelInactive(ctx: ChannelHandlerContext) = Unit

    open fun onChannelWritabilityChanged(ctx: ChannelHandlerContext) = Unit
}

private class Js5SubHandler(parent: MasterHandler) : SubHandler<JS5Request>(parent) {

    private val prefetchQueue: MessagePassingQueue<ByteBuf> = SpscArrayQueue(200)
    private val onDemandQueue: MessagePassingQueue<ByteBuf> = SpscArrayQueue(200)
    private var loggedIn = false

    override fun read(ctx: ChannelHandlerContext, msg: JS5Request) {
        when (msg) {
            is JS5Request.Group.Prefetch -> {
                val response = JS5Responses[msg.archive, msg.group]
                    ?: throw DecoderException("Invalid prefetch group request ($msg)")

                if (!prefetchQueue.offer(response))
                    throw IllegalStateException("Filled prefetch queue ($msg)")
            }

            is JS5Request.Group.OnDemand -> {
                val response = JS5Responses[msg.archive, msg.group]
                    ?: throw DecoderException("Invalid on-demand group request ($msg)")

                if (!onDemandQueue.offer(response))
                    throw IllegalStateException("Filled on-demand queue ($msg)")
            }

            JS5Request.LoggedIn -> loggedIn = true
            JS5Request.LoggedOut -> loggedIn = false
            JS5Request.Disconnected -> ctx.close()
            else -> {}
        }
    }

    override fun onReadComplete(ctx: ChannelHandlerContext) {
        var written = false

        for (i in 1..200) {
            val request = onDemandQueue.poll() ?: break
            ctx.write(request.retainedDuplicate(), ctx.voidPromise())
            written = true
        }

        if (written)
            ctx.flush()

        while (true) {
            val request = prefetchQueue.poll() ?: break
            ctx.writeAndFlush(request.retainedDuplicate(), ctx.voidPromise())
        }
        return
    }

    override fun toString(): String =
        "Js5(prefetchQueue(size=${prefetchQueue.size()}), onDemandQueue(size=${onDemandQueue.size()}), loggedIn=$loggedIn)"
}

private class HandshakeSubHandler(parent: MasterHandler) : SubHandler<HandshakePacketIn>(parent) {

    override fun read(ctx: ChannelHandlerContext, msg: HandshakePacketIn) {
        when (msg) {
            is InitGameConnection -> {
                val decoder = ctx.pipeline().get(DECODER)
                val encoder = ctx.pipeline().get(ENCODER)
                ctx.writeAndFlush(
                    HandshakeResponse.GameConnection(
                        ClientResponse.SUCCESSFUL,
                        sessionKey = ThreadLocalSecureRandom().nextLong()
                    )
                ).addListener { future ->
                    if (future.isSuccess) {
                        ctx.pipeline().remove(encoder)
                        try {
                            ctx.channel().config().setOption(
                                ChannelOption.IP_TOS,
                                NetworkPipelineConstants.GAME_CONNECTION_IP_TOS
                            )
                        } catch (e: Exception) {
                            logger.error(e) { "read() -> InitGameConnection -> Failed to set game IP_TOS to: ${NetworkPipelineConstants.GAME_CONNECTION_IP_TOS}" }
                        }
                        ctx.pipeline().replace(decoder, DECODER, LoginDecoder())
                    }
                }
            }

            is InitJS5Connection -> {
                if (NetworkConstants.REVISION == msg.build) {
                    val encoder = ctx.pipeline().get(ENCODER)
                    val decoder = ctx.pipeline().get(DECODER)
                    ctx.writeAndFlush(HandshakeResponse.JS5Connection(ClientResponse.SUCCESSFUL))
                        .addListener { future ->
                            if (future.isSuccess) {
                                ctx.pipeline().remove(encoder)
                                try {
                                    ctx.channel().config().setOption(ChannelOption.IP_TOS, JS5_CONNECTION_IP_TOS)
                                } catch (e: Exception) {
                                    logger.error(e) { "read() -> InitJS5Connection -> Failed to set JS5 IP_TOS to: $JS5_CONNECTION_IP_TOS" }
                                }
                                ctx.pipeline().replace(decoder, DECODER, JS5RequestDecoder())
                            }
                        }
                } else {
                    ctx.writeAndFlush(HandshakeResponse.JS5Connection(ClientResponse.SERVER_UPDATED))
                        .addListener(ChannelFutureListener.CLOSE)
                }
            }
        }
    }

    override fun toString(): String = "Handshake"
}

private class LoginSubHandler(parent: MasterHandler) : SubHandler<PacketIn>(parent) {

    private var proofOfWork: ProofOfWork? = null
    private var loginPacketIn: LoginPacketIn? = null

    override fun read(ctx: ChannelHandlerContext, msg: PacketIn) {
        when (msg) {
            is LoginPacketIn -> handleLoginPacket(ctx, msg)
            is ProofOfWorkResponse -> handleProofOfWorkPacket(ctx, msg)
            else -> throw IllegalStateException("Invalid login packet: $msg")
        }
    }

    private fun handleLoginPacket(
        ctx: ChannelHandlerContext,
        msg: LoginPacketIn,
    ) {
        val decoder = ctx.pipeline().get(DECODER)
        LoginService.service.execute {
            val response = msg.getResponse()
            if (response != null) {
                response.writeAndClose(ctx)
                return@execute
            }

            val array = ByteArray(45)
            ThreadLocalSecureRandom().nextBytes(array)
            val text = BaseEncoding.base64Url().encode(array)

            proofOfWork = ProofOfWork(NetworkConstants.proofOfWorkDifficulty, text)

            val stringBuffer = proofOfWork!!.text.toByteArray()
            val stringBufferSize = stringBuffer.size

            val bufSize = 1 + 2 + 1 + 1 + 1 + stringBufferSize + 1
            val buf = ctx.alloc().buffer(bufSize, bufSize).apply {
                writeByte(ClientResponse.PROOF_OF_WORK.id)
                writeShort(4 + stringBufferSize)
                writeByte(0)
                writeByte(1)
                writeByte(proofOfWork!!.difficulty)
                writeBytes(stringBuffer)
                writeByte(0)
            }

            ctx.writeAndFlush(buf).addListener { future ->
                if (future.isSuccess) {
                    ctx.pipeline().replace(decoder, DECODER, ProofOfWorkResponseDecoder())
                    loginPacketIn = msg
                } else {
                    logger.error(future.cause()) {
                        "handleLoginPacket -> Failed to write proofOfWorkResponse, closing channel."
                    }
                }
            }
        }
    }

    private fun handleProofOfWorkPacket(ctx: ChannelHandlerContext, msg: ProofOfWorkResponse) {
        LoginService.service.execute {
            try {
                val proofOfWork = proofOfWork?: error("proofOfWork is null")
                if (proofOfWork.validate(msg.nonce)) {
                    val loginPacket = loginPacketIn?: error("loginPacketIn is null")
                    val info = PlayerInformation(
                        loginPacket.username,
                        loginPacket.password,
                        if (loginPacket.clientSettings.isResizable) 1 else 0, // XXX
                        loginPacket.uniqueId,
                        loginPacket.hardwareInfo
                    )
                    World.addLoginRequest(loginPacketIn, info, ctx)
                } else {
                    ClientResponse.INVALID_LOGIN_SERVER_REQUEST.writeAndClose(ctx)
                }
            } catch (e: Exception) {
                logger.error(e) { "handleProofOfWorkPacket -> Failed to handle proofOfWorkPacket" }
            }
        }
    }

    override fun toString(): String =
        "Login(proofOfWork=${proofOfWork != null}, loginPacketIn=${loginPacketIn != null})"
}

private const val OUTBOUND_BUFFER_SIZE_WARNING_THRESHOLD = 30_000

/**
 * Represents a [SubHandler] that handles all incoming and outgoing packets for the game,
 * after the player has successfully logged in.
 *
 * @author Stan van der Bend
 */
private class WorldSubHandler(parent: MasterHandler) : SubHandler<ClientProtEvent>(parent), Session {

    private var player by parent::player
    private val playerName = player?.username?:"null"

    @Volatile private var lastPacketProcessedTick = WorldThread.WORLD_CYCLE
    @Volatile private var lastPacketReceived: LastReceivedPacket? = null
    @Volatile private var skipNextMouseIdleEventPacket = false
    @Volatile private var pendingFlush = false
    @Volatile private var incomingEventsQueueFull = false
    @Volatile private var incomingEventsQueueFullRejectionCount = 0

    private val incomingEvents: MessagePassingQueue<ClientProtEvent> =
        SpscArrayQueue(MAX_INBOUND_PACKETS_PER_TICK)

    private val outgoingPackets: MessagePassingQueue<GamePacketOut> =
        SpscArrayQueue(MAX_PENDING_OUTBOUND_PACKETS)

    private val outgoingPacketsHighPriority: MessagePassingQueue<GamePacketOut> =
        SpscArrayQueue(MAX_PENDING_OUTBOUND_PACKETS)

    private val incomingPacketHandler = MessagePassingQueue.Consumer { event: ClientProtEvent ->
        try {
            val player = player ?: error("Player is null")
            event.use {
                event.handleEvent(player)
                event.logChecked(player)
            }
        } catch (e: Throwable) {
            logger.error(e) { "`incomingPacketHandler` -> Failed to consume $event" }
        }
    }

    private val outgoingPacketWriter = MessagePassingQueue.Consumer { msg: GamePacketOut ->
        try {
            val ctx = ctx ?: error("ChannelHandlerContext is null")
            ctx.write(msg, ctx.voidPromise())
        } catch (e: Throwable) {
            logger.error(e) { "`outgoingPacketWriter` -> Failed to encode $msg" }
        }
    }

    private val outgoingPacketDropper = MessagePassingQueue.Consumer { msg: GamePacketOut ->
        try {
            if (msg.refCnt() > 0)
                msg.release()
        } catch (e: Throwable) {
            logger.error(e) { "`outgoingPacketDropper` -> Failed to drop $msg" }
        }
    }

    override fun read(ctx: ChannelHandlerContext, msg: ClientProtEvent) {

        val player = player
        if (player == null) {
            logger.warn { "read() -> Player is null, did not add event: $msg" }
            return
        }

        if (disregardPacket(msg))
            return

        lastPacketReceived = LastReceivedPacket(WorldThread.WORLD_CYCLE, msg)

        if (incomingEvents.offer(msg)) {
            incomingEventsQueueFull = false
            incomingEventsQueueFullRejectionCount = 0
        } else {
            if (++incomingEventsQueueFullRejectionCount % 10 == 0)
                logger.warn { "read() -> Failed to enqueue $msg because `incomingEvents` queue is full" }
            incomingEventsQueueFull = true
        }
    }

    private fun disregardPacket(msg: ClientProtEvent): Boolean {
        if (msg is EventMouseIdleEvent) {
            if (skipNextMouseIdleEventPacket)
                return true
            else
                skipNextMouseIdleEventPacket = true
        }
        return false
    }

    override fun onChannelInactive(ctx: ChannelHandlerContext) {
        val player = player
        if (player == null) {
            logger.warn {"onChannelInactive() -> Player is null, did not mark player in logout state." }
            return
        }
        try {
            if (parent.showDebugPrints())
                logger.info { "channelInactive() -> Marked player in logout state." }
            player.logoutType = LogoutType.CONNECTION_LOST
            this.player = null
        } catch (e: Exception) {
            logger.error(e) { "onChannelInactive() -> Failed to mark player in logout state." }
        }
    }

    override fun onChannelWritabilityChanged(ctx: ChannelHandlerContext) {
        val player = player
        if (player == null) {
            logger.warn {"onChannelWritabilityChanged() -> Player is null, did not change channel auto read status." }
            return
        }
        val channel = ctx.channel()
        if (channel == null) {
            logger.warn {"onChannelWritabilityChanged() -> Channel is null, did not change channel auto read status." }
            return
        }
        val autoReadAndIsWritable = channel.isWritable
        channel.config().isAutoRead = autoReadAndIsWritable
        logger.debug { "onChannelWritabilityChanged() -> `channel.isWritable` = `channel.config().isAutoRead` = $autoReadAndIsWritable" }
    }

    override fun send(event: ServerEvent): Boolean {
        val ctx = ctx
        if (ctx == null) {
            logger.trace { "send() -> ChannelHandlerContext is null, ignoring $event." }
            return false
        }
        if (!isActive()) {
            logger.trace { "send() -> Channel is inactive, ignoring $event." }
            return false
        }
        event as GamePacketEncoder
        return try {
            val packet = event.encode()
            when (event.encoderMode()) {
                GamePacketEncoderMode.QUEUE -> {
                    if (outgoingPackets.offer(packet))
                        true
                    else {
                        logger.warn { "send() -> `outgoingPackets` is full, ignoring $event." }
                        false
                    }
                }
                GamePacketEncoderMode.WRITE -> {
                    if (ctx.channel().isWritable) {
                        ctx.write(packet, ctx.voidPromise())
                        pendingFlush = true
                        true
                    } else {
                        if (outgoingPacketsHighPriority.offer(packet))
                            true
                        else {
                            logger.warn { "send() -> `outgoingPacketsHighPriority` is full, ignoring $event." }
                            false
                        }
                    }
                }
                GamePacketEncoderMode.WRITE_FLUSH -> {
                    ctx.writeAndFlush(packet, ctx.voidPromise())
                    true
                }
            }
        } catch (e: Throwable) {
            logger.error(e) { "send() -> Failed to write $event." }
            false
        }
    }

    override fun process(): Boolean {
        val result = tryProcess()
        if (incomingEventsQueueFull && !result)
            logger.warn { "process() -> incomingEvents is marked as full, but did not process any packets?" }
        skipNextMouseIdleEventPacket = false
        return result
    }

    private fun tryProcess(): Boolean {
        val ctx = ctx
        if (ctx == null) {
            logger.trace { "tryProcess() -> ChannelHandlerContext is null, session did not process." }
            return false
        }

        val player = player
        if (player == null) {
            logger.trace { "tryProcess() -> Player is null, session did not process." }
            return false
        }

        if (!player.logoutType.processSession()) {
            logger.trace { "tryProcess() -> Player is marked for logout, session did not process." }
            return false
        }

        if (!isActive()) {
            logger.trace { "tryProcess() -> Session is not active, did not process." }
            return false
        }

        if (isExpired()) {
            logger.trace { "tryProcess() -> Session expired, did not process." }
            return false
        }

        val eventDrained = incomingEvents.drain(incomingPacketHandler, MAX_INBOUND_PACKETS_PER_TICK)
        if (eventDrained > 0) {
            lastPacketProcessedTick = WorldThread.WORLD_CYCLE
            return true
        }
        return false
    }

    override fun flush() {

        val ctx = ctx
        if (ctx == null) {
            logger.warn { "flush() -> ChannelHandlerContext is null, session did not flush." }
            return
        }

        val player = player
        if (player == null) {
            logger.warn { "flush() -> Player is null, session did not flush." }
            return
        }

        val channel = ctx.channel()
        if (channel == null) {
            logger.warn { "flush() -> Channel is null, session did not flush." }
            return
        }

        if (!channel.isRegistered) {
            logger.warn { "flush() -> Channel is not registered, session did not flush, closing channel." }
            ctx.close()
            return
        }

        if (!channel.isWritable) {
            logger.warn { "flush() -> Channel is not writable, session did not flush." }
            return
        }

        var limit = MAX_OUTBOUND_PACKETS_PER_TICK
        val outgoingHighPriorityPacketsWritten = outgoingPacketsHighPriority.drain(outgoingPacketWriter, limit)
        limit -= outgoingHighPriorityPacketsWritten
        val outgoingPacketsWritten = outgoingPackets.drain(outgoingPacketWriter, limit) + outgoingHighPriorityPacketsWritten
        if (outgoingPacketsWritten > 0 || pendingFlush) {
            with(channel.unsafe().outboundBuffer()) {
                if (totalPendingWriteBytes() >= OUTBOUND_BUFFER_SIZE_WARNING_THRESHOLD)
                    logger.warn { "flush() -> Large outbound buffer detected, flushed $outgoingPacketsWritten packets (totalPendingWriteBytes = ${totalPendingWriteBytes()}, nioBufferCount = ${nioBufferCount()}, nioBufferSize = ${nioBufferSize()})" }
            }
            ctx.flush()
            pendingFlush = false
        }
    }

    override fun close() {
        val outgoingPacketsDropped = outgoingPackets.drain(outgoingPacketDropper)
        if (outgoingPacketsDropped > 0)
            logger.warn { "close() -> dropped $outgoingPacketsDropped packets because session closed." }
    }

    override fun getHostAddress(): String =
        kotlin.runCatching { ctx?.channel()?.ipv4Address()?.hostAddress }.getOrNull()?:"0.0.0.0"

    override fun isActive() =
        ctx?.channel()?.isActive ?: false && ctx?.channel()?.isOpen ?: false

    private val sessionExpirationTimeout
        get() = kotlin.runCatching {
            if (player?.areaManager?.area is WildernessArea || player?.getBooleanTemporaryAttribute("gambling") == true)
                SESSION_EXPIRATION_TIMEOUT_WILDERNESS
            else
                SESSION_EXPIRATION_TIMEOUT_SAFE
        }.getOrElse { SESSION_EXPIRATION_TIMEOUT_SAFE }

    override fun isExpired() = WorldThread.WORLD_CYCLE - lastPacketProcessedTick >= sessionExpirationTimeout

    override fun toString(): String {
        return buildString {
            append("World(")
            append("player=$playerName, ")
            append("address=${getHostAddress()}")
            if (!incomingEvents.isEmpty)
                append(", incoming(size=${incomingEvents.size()})")
            if (!outgoingPackets.isEmpty)
                append(", outgoing(size=${outgoingPackets.size()})")
            if (!isActive()) {
                val channel = ctx?.channel()
                append(", channel.isActive=${channel?.isActive}")
                append(", channel.isOpen=${channel?.isOpen}")
            }
            if (isExpired()) {
                append(", expire_timeout=$sessionExpirationTimeout")
                append(", expires_in=${sessionExpirationTimeout - (WorldThread.WORLD_CYCLE - lastPacketProcessedTick)}")
            }
            if (incomingEventsQueueFull) {
                append(", incomingEventsQueueFull=true")
                append(", incomingEventsQueueFullRejectionCount=$incomingEventsQueueFullRejectionCount")
            }
            if (lastPacketReceived != null)
                append(", lastPacketReceived=$lastPacketReceived")
            val logoutType = player?.logoutType?:LogoutType.NONE
            if (logoutType != LogoutType.NONE)
                append(", logoutType=$logoutType")
            append(")")
        }
    }

    private companion object {

        private var SESSION_EXPIRATION_TIMEOUT_WILDERNESS = TimeUnit.SECONDS.toTicks(60)
        private var SESSION_EXPIRATION_TIMEOUT_SAFE = TimeUnit.SECONDS.toTicks(20)
    }

    private data class LastReceivedPacket(val tick: Long, val packet: ClientProtEvent)
}
