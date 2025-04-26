@file:Suppress("unused")

package com.near_reality.api.resources

import io.ktor.resources.*
import kotlinx.serialization.SerialName

@Resource("/vote")
class Vote {

    @Resource("sites")
    class Sites(val parent: Vote = Vote())

    @Resource("callback")
    class Callback(val parent: Vote = Vote()) {

        @Resource("game")
        class Game(val parent: Callback = Callback())

        @Resource("rspslist")
        class RSPSList(val parent: Callback = Callback(), val voted: Int, @SerialName("userid") val userId: Int, @SerialName("userip") val ip: String)

        @Resource("runelist/{username}")
        class RuneList(val parent: Callback = Callback(), val username: String)

        @Resource("runelocus")
        class RuneLocus(val parent: Callback = Callback(), @SerialName("callback") val userId: Int, val ip: String)
    }

    @Resource("status")
    class Status(val parent: Vote = Vote(), val accountId: Long) {

        @Resource("all")
        class All(val parent: Status) {
            constructor(accountId: Long) : this(Status(accountId = accountId))
            val accountId get() = parent.accountId
        }
    }
}
