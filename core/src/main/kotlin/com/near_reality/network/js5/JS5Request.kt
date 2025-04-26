package com.near_reality.network.js5

/**
 * @author Jire
 */
sealed interface JS5Request {

    sealed interface Group : JS5Request {
        val archive: Int
        val group: Int

        data class Prefetch(
            override val archive: Int,
            override val group: Int
        ) : Group

        data class OnDemand(
            override val archive: Int,
            override val group: Int
        ) : Group
    }

    object LoggedIn : JS5Request
    object LoggedOut : JS5Request

    data class Rekey(val key: Int) : JS5Request

    object Connected : JS5Request
    object Disconnected : JS5Request

}
