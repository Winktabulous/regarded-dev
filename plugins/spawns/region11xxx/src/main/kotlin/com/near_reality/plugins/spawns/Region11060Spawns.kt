package com.near_reality.plugins.spawns

import com.near_reality.scripts.npc.spawns.NPCSpawnsScript
import com.zenyte.game.world.entity.npc.NpcId
import com.zenyte.game.world.entity.npc.NpcId.*
import com.zenyte.game.util.Direction.*

class Region11060Spawns : NPCSpawnsScript() {
    init {
        FRINCOS(2808, 3342, 0, SOUTH, 2)
        AUGUSTE(2808, 3355, 0, SOUTH, 3)
        BLACK_BEAR(2809, 3376, 0, SOUTH, 8)
        TOOL_LEPRECHAUN(2812, 3332, 0, SOUTH, 0)
        FRANCIS(2814, 3337, 0, SOUTH, 4)
    }
}