package com.near_reality.api.resources

import com.near_reality.api.model.ApiGameMode
import io.ktor.resources.*

@Resource("/user")
class User {

    @Resource("login")
    class Login(val parent: User = User())

    @Resource("{id}")
    class Id(val parent: User = User(), val id: Long) {

        @Resource("check2fa")
        class Check2FA(val parent: Id, val code: Int) {
            constructor(id: Long, code: Int) : this(Id(id = id), code)
            val id get() = parent.id
        }

        @Resource("vote")
        class Vote(val parent: Id) {
            constructor(id: Long) : this(Id(id = id))
            val id get() = parent.id
        }

        @Resource("game_mode")
        class GameMode(val parent: Id, val mode: ApiGameMode) {
            constructor(id: Long, mode: ApiGameMode) : this(Id(id = id), mode)
            val id get() = parent.id
        }

        @Resource("hiscores")
        class Hiscores(val parent: Id) {
            constructor(id: Long) : this(Id(id = id))
            val id get() = parent.id
        }

        @Resource("bond")
        class Bond(val parent: Id, val bond: com.near_reality.api.model.Bond) {
            constructor(id: Long, bond: com.near_reality.api.model.Bond) : this(Id(id = id), bond = bond)
            val id get() = parent.id
        }

        @Resource("credits")
        class Credits(val parent: Id, val amount: Int) {
            constructor(id: Long, amount: Int) : this(Id(id = id), amount = amount)
            val id get() = parent.id
        }
    }
}
