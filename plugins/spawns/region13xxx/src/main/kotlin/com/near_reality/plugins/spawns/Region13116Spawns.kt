package com.near_reality.plugins.spawns

import com.near_reality.scripts.npc.spawns.NPCSpawnsScript
import com.zenyte.game.world.entity.npc.NpcId
import com.zenyte.game.world.entity.npc.NpcId.*
import com.zenyte.game.util.Direction.*

class Region13116Spawns : NPCSpawnsScript() {
    init {
        GREATER_DEMON_2028(3282, 3880, 0, SOUTH, 4)
        GREATER_DEMON_2029(3287, 3894, 0, SOUTH, 7)
        GREATER_DEMON_2027(3296, 3872, 0, SOUTH, 2)
        GREATER_DEMON_2026(3304, 3886, 0, SOUTH, 4)
        LESSER_DEMON(3311, 3845, 0, SOUTH, 4)
        LESSER_DEMON_2018(3324, 3856, 0, SOUTH, 8)
    }
}