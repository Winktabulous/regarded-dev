package com.zenyte.net.login

import it.unimi.dsi.fastutil.ints.Int2ObjectMap
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap

/**
 * @author Jire
 */
enum class AuthType(val id: Int) {

    TRUSTED_COMPUTER(0),
    TRUSTED_AUTHENTICATION(1),
    NORMAL(2),
    UNTRUSTED_AUTHENTICATION(3);

    companion object {

        @JvmField
        val values = values()

        private val idToType: Int2ObjectMap<AuthType> = Int2ObjectOpenHashMap(values.size)

        init {
            for (value in values) {
                idToType.put(value.id, value)
            }
        }

        @JvmStatic
        operator fun get(id: Int): AuthType? = idToType.get(id)

    }

}