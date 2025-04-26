package com.near_reality.plugins.spawns

import com.near_reality.scripts.npc.spawns.NPCSpawnsScript
import com.zenyte.game.world.entity.npc.NpcId
import com.zenyte.game.world.entity.npc.NpcId.*
import com.zenyte.game.util.Direction.*

class Region12090Spawns : NPCSpawnsScript() {
    init {
        CHAOS_DWARF(3019, 3760, 0, SOUTH, 14)
        CHAOS_DWARF(3019, 3766, 0, SOUTH, 14)
        CHAOS_DWARF(3021, 3755, 0, SOUTH, 14)
        CHAOS_DWARF(3025, 3763, 0, SOUTH, 14)
    }
}