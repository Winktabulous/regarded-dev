package com.zenyte.net.game.packet

import com.zenyte.net.NetworkConstants
import com.zenyte.net.PacketOut
import com.zenyte.net.game.ServerProt
import com.zenyte.net.io.RSBuffer
import io.netty.buffer.ByteBuf
import io.netty.buffer.ByteBufAllocator

/**
 * @author Tommeh | 28 jul. 2018 | 12:39:39
 * @see [Rune-Server profile](https://www.rune-server.ee/members/tommeh/)}
 */
class GamePacketOut @JvmOverloads constructor(
    val prot: ServerProt,
    data: ByteBuf = prot.byteBuf(),
    val encryptBuffer: Boolean = false
) : RSBuffer(data), PacketOut {

    constructor(packet: ServerProt, encryptBuffer: Boolean = false) : this(
        packet,
        packet.byteBuf(),
        encryptBuffer = encryptBuffer
    )

    @JvmOverloads
    constructor(packet: ServerProt, data: RSBuffer, encryptBuffer: Boolean = false) : this(
        packet,
        data.content(),
        encryptBuffer
    )

    constructor(
        packet: ServerProt,
        initialCapacity: Int = NetworkConstants.INITIAL_SERVER_BUFFER_SIZE,
        maximumCapacity: Int = NetworkConstants.MAX_SERVER_BUFFER_SIZE
    ) : this(packet, ByteBufAllocator.DEFAULT.buffer(initialCapacity, maximumCapacity))

    constructor(
        packet: ServerProt,
        initialCapacity: Int = NetworkConstants.INITIAL_SERVER_BUFFER_SIZE
    ) : this(packet, initialCapacity, NetworkConstants.MAX_SERVER_BUFFER_SIZE)

}