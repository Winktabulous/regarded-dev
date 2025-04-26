package com.near_reality.plugins.spawns

import com.near_reality.scripts.npc.spawns.NPCSpawnsScript
import com.zenyte.game.world.entity.npc.NpcId
import com.zenyte.game.world.entity.npc.NpcId.*
import com.zenyte.game.util.Direction.*

class Region9776Spawns : NPCSpawnsScript() {
    init {
        LANTHUS(2441, 3089, 0, NORTH_EAST, 0)
        POSTIE_PETE(2445, 3097, 0, SOUTH, 627)
        9392(2455, 3084, 0, SOUTH, 0)
        4729(2460, 3108, 0, WEST, 0)
    }
}