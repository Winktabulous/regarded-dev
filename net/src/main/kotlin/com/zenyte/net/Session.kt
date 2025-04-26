package com.zenyte.net

import com.zenyte.net.game.ServerEvent

/**
 * @author Jire
 */
interface Session {

    fun getHostAddress(): String

    fun process(): Boolean

    fun send(event: ServerEvent): Boolean

    fun flush()

    fun close()

    fun isActive(): Boolean

    fun isExpired(): Boolean

}