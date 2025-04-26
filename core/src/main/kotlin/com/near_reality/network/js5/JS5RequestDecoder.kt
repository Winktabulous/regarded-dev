package com.near_reality.network.js5

import io.netty.buffer.ByteBuf
import io.netty.channel.ChannelHandlerContext
import io.netty.handler.codec.ByteToMessageDecoder
import io.netty.handler.codec.DecoderException

/**
 * @author Jire
 */
class JS5RequestDecoder : ByteToMessageDecoder() {

    override fun decode(ctx: ChannelHandlerContext, input: ByteBuf, out: MutableList<Any>) {
        if (!input.isReadable(TOTAL_LENGTH)) return

        when (val opcode = input.readUnsignedByte().toInt()) {
            0 -> {
                val archive = input.readUnsignedByte().toInt()
                val group = input.readUnsignedShort()
                out += JS5Request.Group.Prefetch(archive, group)
            }

            1 -> {
                val archive = input.readUnsignedByte().toInt()
                val group = input.readUnsignedShort()
                out += JS5Request.Group.OnDemand(archive, group)
            }

            2 -> JS5Request.LoggedIn.skipped(input, out)
            3 -> JS5Request.LoggedOut.skipped(input, out)

            4 -> {
                val key = input.readUnsignedByte().toInt()
                input.skipBytes(2)
                out += JS5Request.Rekey(key)
            }

            5 -> JS5Request.Connected.skipped(input, out)
            6 -> JS5Request.Disconnected.skipped(input, out)

            else -> throw DecoderException("Unsupported JS5 opcode: $opcode")
        }
    }

    private companion object {
        private const val HEADER_LENGTH = 1
        private const val BODY_LENGTH = 3
        private const val TOTAL_LENGTH = HEADER_LENGTH + BODY_LENGTH

        private fun JS5Request.skipped(
            input: ByteBuf,
            out: MutableList<Any>
        ) {
            input.skipBytes(BODY_LENGTH)
            out += this
        }
    }

}
