package com.near_reality.network.proofofwork

import io.netty.buffer.ByteBuf
import io.netty.channel.ChannelHandlerContext
import io.netty.handler.codec.ByteToMessageDecoder
import io.netty.handler.codec.DecoderException

/**
 * @author Jire
 */
class ProofOfWorkResponseDecoder : ByteToMessageDecoder() {

    private enum class State {
        OPCODE,
        LENGTH,
        NONCE
    }

    private var state = State.OPCODE

    private var opcode = -1
    private var length = -1

    override fun decode(
        ctx: ChannelHandlerContext,
        buf: ByteBuf,
        out: MutableList<Any>
    ) {
        if (state == State.OPCODE) {
            opcode = buf.readUnsignedByte().toInt() // should be 19 (field3195)
            if (opcode != 19)
                throw DecoderException("Unsupported proof-of-work opcode: $opcode")

            state = State.LENGTH
        }

        if (state == State.LENGTH) {
            if (!buf.isReadable(2)) return

            length = buf.readUnsignedShort() // should always be 8
            if (length != 8)
                throw DecoderException("Mismatching proof-of-work length: $length")

            state = State.NONCE
        }

        if (state == State.NONCE) {
            if (!buf.isReadable(length)) return

            val nonce = buf.readLong()
            out += ProofOfWorkResponse(nonce)

            state = State.OPCODE
        }
    }

}