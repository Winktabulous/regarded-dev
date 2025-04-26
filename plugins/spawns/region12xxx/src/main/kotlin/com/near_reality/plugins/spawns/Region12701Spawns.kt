package com.near_reality.plugins.spawns

import com.near_reality.scripts.npc.spawns.NPCSpawnsScript
import com.zenyte.game.world.entity.npc.NpcId
import com.zenyte.game.world.entity.npc.NpcId.*
import com.zenyte.game.util.Direction.*

class Region12701Spawns : NPCSpawnsScript() {
    init {
        REVENANT_IMP(3199, 10071, 0, SOUTH, 3)
        REVENANT_IMP(3196, 10069, 0, SOUTH, 3)
    }
}