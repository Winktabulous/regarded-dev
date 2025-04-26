package com.zenyte.net.handshake.packet

import com.zenyte.net.ClientResponse

/**
 * @author Jire
 */
sealed interface HandshakeResponse {

    val response: ClientResponse

    data class GameConnection(
        override val response: ClientResponse,
        val sessionKey: Long
    ) : HandshakeResponse

    data class JS5Connection(
        override val response: ClientResponse
    ) : HandshakeResponse

}