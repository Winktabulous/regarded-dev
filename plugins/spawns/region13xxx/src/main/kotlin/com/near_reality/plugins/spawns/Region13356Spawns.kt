package com.near_reality.plugins.spawns

import com.near_reality.scripts.npc.spawns.NPCSpawnsScript
import com.zenyte.game.world.entity.npc.NpcId
import com.zenyte.game.world.entity.npc.NpcId.*
import com.zenyte.game.util.Direction.*

class Region13356Spawns : NPCSpawnsScript() {
    init {
        VULTURE(3333, 2864, 0, SOUTH, 4)
        VULTURE(3335, 2860, 0, SOUTH, 4)
        VULTURE(3338, 2864, 0, SOUTH, 4)
        SIMON_TEMPLETON(3346, 2826, 0, NORTH, 0)
        PYRAMID_BLOCK_5788(3372, 2847, 1, SOUTH, 0)
        PYRAMID_BLOCK(3366, 2845, 3, SOUTH, 0)
    }
}