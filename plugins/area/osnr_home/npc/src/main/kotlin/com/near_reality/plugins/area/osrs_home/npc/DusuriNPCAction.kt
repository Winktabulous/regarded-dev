package com.near_reality.plugins.area.osrs_home.npc

import com.near_reality.scripts.npc.actions.NPCActionScript
import com.zenyte.game.world.entity.npc.NpcId

class DusuriNPCAction : NPCActionScript() {

    init {
        npcs(NpcId.DUSURI)

        "Talk-to" {
            player.openShop("Dusuri's Star Store")
        }
    }

}