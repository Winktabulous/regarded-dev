package com.near_reality.plugins.spawns.custom

import com.near_reality.scripts.npc.spawns.NPCSpawnsScript
import com.zenyte.game.util.Direction.WEST
import com.zenyte.game.world.entity.npc.NpcId.COMBAT_DUMMY_16019

class ClanWarsFfaSpawns : NPCSpawnsScript() {
    init {
        COMBAT_DUMMY_16019(3331, 4752, 0, WEST)
    }
}