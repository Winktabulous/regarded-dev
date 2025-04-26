package com.near_reality.plugins.spawns

import com.near_reality.scripts.npc.spawns.NPCSpawnsScript
import com.zenyte.game.world.entity.npc.NpcId
import com.zenyte.game.world.entity.npc.NpcId.*
import com.zenyte.game.util.Direction.*

class Region10040Spawns : NPCSpawnsScript() {
    init {
        LARRISSA(2508, 3635, 0, SOUTH, 5)
        JOSSIK(2509, 3639, 1, SOUTH, 2)
    }
}