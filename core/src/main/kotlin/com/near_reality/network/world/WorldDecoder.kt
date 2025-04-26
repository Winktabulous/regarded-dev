package com.near_reality.network.world

import com.near_reality.crypto.StreamCipher
import com.zenyte.game.GameLoader
import com.zenyte.game.packet.ClientProtDecoder
import com.zenyte.game.packet.`in`.ClientProtEvent
import com.zenyte.game.packet.`in`.event.NoTimeOutEvent
import com.zenyte.net.NetworkConstants
import com.zenyte.net.game.ClientProt
import com.zenyte.net.game.packet.GamePacketIn
import io.netty.buffer.ByteBuf
import io.netty.channel.ChannelHandlerContext
import io.netty.handler.codec.ByteToMessageDecoder
import io.netty.handler.codec.DecoderException
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant

/**
 * @author Jire
 */
class WorldDecoder(
    private val cipher: StreamCipher
) : ByteToMessageDecoder() {

    private enum class State {
        OPCODE,
        LENGTH,
        PAYLOAD
    }

    private var state = State.OPCODE
    private lateinit var prot: ClientProt
    private var length = 0
    private lateinit var decodeInit: Instant

    override fun decode(ctx: ChannelHandlerContext, input: ByteBuf, out: MutableList<Any>) {
        if (state == State.OPCODE) {
            val opcode = (input.readUnsignedByte().toInt() - cipher.nextInt()) and 0xFF

            if (opcode >= prots.size) {
                throw DecoderException("Unsupported opcode: $opcode")
            }
            prot = prots[opcode]
            state = State.LENGTH
            decodeInit = Clock.System.now()
        }

        if (state == State.LENGTH) {
            val decodeLength = when (val size = prot.size) {
                -1 -> {
                    if (!input.isReadable) return
                    input.readUnsignedByte().toInt()
                }

                -2 -> {
                    if (!input.isReadable(2)) return
                    input.readUnsignedShort()
                }

                else -> size
            }
            if (decodeLength > NetworkConstants.MAX_CLIENT_BUFFER_SIZE) {
                throw DecoderException("Max client buffer size exceeded. Prot: $prot, size: $decodeLength")
            }

            length = decodeLength

            state = State.PAYLOAD
        }

        if (state == State.PAYLOAD) {
            if (length != 0 && !input.isReadable(length)) return

            val payload = input.readSlice(length)

            val opcode = prot.opcode
            val decoder = decoders[opcode]
                ?: throw DecoderException("Unhandled prot: $prot")

            val event: ClientProtEvent = decoder.decode(opcode, GamePacketIn(prot, payload))

            if (event is NoTimeOutEvent) {
                event.timeOfDecode = decodeInit
                event.timeOfPass = Clock.System.now()
            }
            /*if (payload.isReadable) {
                throw DecoderException(
                    "Decoder didn't read entire payload: ${decoder.javaClass} " +
                            "(${payload.readableBytes()} bytes remaining)"
                )
            }*/

            out += event

            state = State.OPCODE
        }
    }

    private companion object {
        private val prots = ClientProt.values
        private val decoders: Array<ClientProtDecoder<*>?> = GameLoader.getDecoders()
    }

}
