package com.zenyte.net.handshake.codec

import com.zenyte.net.handshake.packet.inc.InitGameConnection
import com.zenyte.net.handshake.packet.inc.InitJS5Connection
import io.netty.buffer.ByteBuf
import io.netty.channel.ChannelHandlerContext
import io.netty.handler.codec.ByteToMessageDecoder
import io.netty.handler.codec.DecoderException

/**
 * @author Jire
 */
class HandshakeDecoder : ByteToMessageDecoder() {

    private enum class State {
        OPCODE,
        PAYLOAD
    }

    private var state = State.OPCODE

    private var opcode = -1
    private var length = -1

    override fun decode(ctx: ChannelHandlerContext, buf: ByteBuf, out: MutableList<Any>) {
        if (state == State.OPCODE) {
            opcode = buf.readUnsignedByte().toInt()
            when (opcode) {
                14 -> out += InitGameConnection
                15 -> {
                    length = 4
                    state = State.PAYLOAD
                }

                else -> ctx.close()
            }
        }

        if (state == State.PAYLOAD) {
            if (!buf.isReadable(length)) return

            when (opcode) {
                15 -> {
                    val build = buf.readInt()
                    out += InitJS5Connection(build)

                    state = State.OPCODE
                }

                else -> ctx.close()
            }
        }
    }

    override fun isSingleDecode() = true

}