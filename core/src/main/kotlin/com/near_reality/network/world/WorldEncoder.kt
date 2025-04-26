package com.near_reality.network.world

import com.near_reality.crypto.StreamCipher
import com.zenyte.net.game.ServerProt
import com.zenyte.net.game.packet.GamePacketOut
import io.netty.buffer.ByteBuf
import io.netty.channel.ChannelHandlerContext
import io.netty.handler.codec.MessageToByteEncoder

/**
 * @author Jire
 */
class WorldEncoder(
    private val cipher: StreamCipher
) : MessageToByteEncoder<GamePacketOut>() {

    override fun encode(ctx: ChannelHandlerContext, msg: GamePacketOut, out: ByteBuf) {
        val prot = msg.prot
        val content = msg.content()
        if (prot == ServerProt.SEND_PING) {
            val now = System.nanoTime()
            content.setLong(0, now)
        }
        prot.encodePacket(cipher, content, msg.encryptBuffer, out)
    }

}