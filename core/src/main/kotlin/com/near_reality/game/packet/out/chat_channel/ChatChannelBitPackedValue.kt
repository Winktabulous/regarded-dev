package com.near_reality.game.packet.out.chat_channel

import com.zenyte.net.game.packet.GamePacketOut

class ChatChannelBitPackedValue(
    private val value: Int,
    private val byte1: Int,
    private val byte2: Int,
) {
    fun write(packetOut: GamePacketOut) = packetOut.run {
        writeInt(value)
        writeByte(byte1)
        writeByte(byte2)
    }
}
