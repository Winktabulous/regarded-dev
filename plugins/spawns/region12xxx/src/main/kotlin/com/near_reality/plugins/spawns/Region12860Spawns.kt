package com.near_reality.plugins.spawns

import com.near_reality.scripts.npc.spawns.NPCSpawnsScript
import com.zenyte.game.world.entity.npc.NpcId
import com.zenyte.game.world.entity.npc.NpcId.*
import com.zenyte.game.util.Direction.*

class Region12860Spawns : NPCSpawnsScript() {
    init {
        LAVA_DRAGON(3212, 3846, 0, SOUTH, 4)
        WILLIAM(3218, 3879, 0, SOUTH, 2)
    }
}