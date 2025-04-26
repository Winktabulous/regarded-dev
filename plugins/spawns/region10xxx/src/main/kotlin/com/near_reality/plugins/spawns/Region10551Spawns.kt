package com.near_reality.plugins.spawns

import com.near_reality.scripts.npc.spawns.NPCSpawnsScript
import com.zenyte.game.world.entity.npc.NpcId
import com.zenyte.game.world.entity.npc.NpcId.*
import com.zenyte.game.util.Direction.*

class Region10551Spawns : NPCSpawnsScript() {
    init {
        TOOL_LEPRECHAUN(2663, 3521, 0, SOUTH, 0)
        RHONEN(2666, 3530, 0, SOUTH, 2)
    }
}