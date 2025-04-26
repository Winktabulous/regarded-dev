package com.near_reality.game.content.boss.nex.`object`.actions

import com.near_reality.game.content.boss.nex.NexModule
import com.near_reality.game.content.boss.nex.nexBestTime
import com.near_reality.game.content.boss.nex.nexDeathCount
import com.near_reality.game.content.boss.nex.nexKillCount
import com.near_reality.scripts.`object`.actions.ObjectActionScript
import com.zenyte.game.GameInterface.NEX_STATS
import com.zenyte.game.world.entity.player.BossTimer
import com.zenyte.game.world.`object`.ObjectId

class AncientScoreboardObjectAction : ObjectActionScript() {
    init {
        ObjectId.SCOREBOARD_42936 {
            when(option) {
                "Read" -> {
                    player.interfaceHandler.sendInterface(NEX_STATS)
                    player.packetDispatcher.run {
                        sendComponentText(NEX_STATS, 9, player.nexKillCount)
                        sendComponentText(NEX_STATS, 11, player.nexDeathCount)
                        sendComponentText(NEX_STATS, 13, player.nexBestTime)
                        sendComponentText(NEX_STATS, 15, NexModule.statistics.globalKillCount)
                        sendComponentText(NEX_STATS, 17, NexModule.statistics.globalDeathCount)
                        sendComponentText(NEX_STATS, 19, BossTimer.formatBestTime(NexModule.statistics.globalBestKillTimeSeconds))
                    }
                }
            }
        }
    }
}
