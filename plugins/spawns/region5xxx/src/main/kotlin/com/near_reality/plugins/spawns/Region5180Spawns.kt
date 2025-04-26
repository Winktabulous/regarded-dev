package com.near_reality.plugins.spawns

import com.near_reality.scripts.npc.spawns.NPCSpawnsScript
import com.zenyte.game.world.entity.npc.NpcId
import com.zenyte.game.world.entity.npc.NpcId.*
import com.zenyte.game.util.Direction.*

class Region5180Spawns : NPCSpawnsScript() {
    init {
        GIANT_BAT(1288, 3846, 0, SOUTH, 11)
        GIANT_BAT(1290, 3840, 0, SOUTH, 11)
        GIANT_BAT(1325, 3843, 0, SOUTH, 11)
        GIANT_BAT(1336, 3842, 0, SOUTH, 11)
    }
}