package com.zenyte.net.handshake.packet.inc

import com.zenyte.net.handshake.packet.HandshakePacketIn

/**
 * @author Jire
 */
data class InitJS5Connection(
    val build: Int
) : HandshakePacketIn