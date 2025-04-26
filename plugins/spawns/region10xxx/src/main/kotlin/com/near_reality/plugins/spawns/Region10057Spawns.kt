package com.near_reality.plugins.spawns

import com.near_reality.scripts.npc.spawns.NPCSpawnsScript
import com.zenyte.game.world.entity.npc.NpcId
import com.zenyte.game.world.entity.npc.NpcId.*
import com.zenyte.game.util.Direction.*

class Region10057Spawns : NPCSpawnsScript() {
    init {
        CHAMBER_GUARDIAN(2508, 4696, 0, SOUTH, 1)
        GUNDAI(2534, 4714, 0, SOUTH, 2)
        LUNDAIL(2534, 4719, 0, SOUTH, 0)
        KOLODION(2541, 4715, 0, SOUTH, 2)
    }
}