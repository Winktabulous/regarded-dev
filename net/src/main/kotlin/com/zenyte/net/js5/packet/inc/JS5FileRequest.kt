package com.zenyte.net.js5.packet.inc

import com.zenyte.net.js5.packet.JS5PacketIn

/**
 * @author Tommeh | 27 jul. 2018 | 20:31:21
 * @see [Rune-Server profile](https://www.rune-server.ee/members/tommeh/)}
 */
data class JS5FileRequest(
    val urgent: Boolean,
    val index: Int,
    val file: Int
) : JS5PacketIn