package com.near_reality.game.content.dt2.area

import com.near_reality.game.content.dt2.npc.DT2BossDifficulty
import com.zenyte.game.world.entity.Location
import com.zenyte.game.world.entity.player.GameCommands.Command
import com.zenyte.game.world.entity.player.privilege.PlayerPrivilege

class DT2Commands {
    companion object {
        fun register() {
            Command(PlayerPrivilege.DEVELOPER, "dt2ve") { p, _ ->
                p.setLocation(Location(1150, 3444, 0))
            }
            Command(PlayerPrivilege.FORUM_MODERATOR, "dt2vi") { p, _ ->
                VardorvisInstance.createInstance(p).constructRegion()
            }
            Command(PlayerPrivilege.FORUM_MODERATOR, "whisperer") { p, _ ->
                p.setLocation(Location(2656, 6399, 0))
            }
            Command(PlayerPrivilege.FORUM_MODERATOR, "dt2wi") { p, _ ->
                WhispererInstance(DT2BossDifficulty.NORMAL, p).constructRegion()
            }
            Command(PlayerPrivilege.FORUM_MODERATOR, "dt2di") { p, _ ->
                DukeSucellusInstance.createInstance(DT2BossDifficulty.NORMAL, p).constructRegion()
            }
            Command(PlayerPrivilege.FORUM_MODERATOR, "dt2dia") { p, _ ->
                DukeSucellusInstance.createInstance(DT2BossDifficulty.AWAKENED, p).constructRegion()
            }
        }
    }
}