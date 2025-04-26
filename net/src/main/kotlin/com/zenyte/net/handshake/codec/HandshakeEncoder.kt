package com.zenyte.net.handshake.codec

import com.zenyte.net.handshake.packet.HandshakeResponse
import io.netty.buffer.ByteBuf
import io.netty.channel.ChannelHandlerContext
import io.netty.handler.codec.MessageToByteEncoder

/**
 * @author Jire
 */
class HandshakeEncoder : MessageToByteEncoder<HandshakeResponse>() {

    override fun encode(ctx: ChannelHandlerContext, packet: HandshakeResponse, out: ByteBuf) {
        when (packet) {
            is HandshakeResponse.GameConnection -> {
                out.writeByte(packet.response.id)
                out.writeLong(packet.sessionKey)
            }

            is HandshakeResponse.JS5Connection -> {
                out.writeByte(packet.response.id)
            }
        }
    }

}