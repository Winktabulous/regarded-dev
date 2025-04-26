package com.zenyte.net.game.packet

import com.zenyte.net.PacketIn
import com.zenyte.net.game.ClientProt
import com.zenyte.net.io.RSBuffer
import io.netty.buffer.ByteBuf

/**
 * @author Tommeh | 28 jul. 2018 | 12:39:39
 * @see [Rune-Server profile](https://www.rune-server.ee/members/tommeh/)}
 */
class GamePacketIn(
    val clientProt: ClientProt,
    data: ByteBuf
) : RSBuffer(data), PacketIn