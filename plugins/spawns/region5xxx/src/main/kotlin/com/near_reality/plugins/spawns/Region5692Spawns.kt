package com.near_reality.plugins.spawns

import com.near_reality.scripts.npc.spawns.NPCSpawnsScript
import com.zenyte.game.world.entity.npc.NpcId
import com.zenyte.game.world.entity.npc.NpcId.*
import com.zenyte.game.util.Direction.*

class Region5692Spawns : NPCSpawnsScript() {
    init {
        MINE_SUPERVISOR(1426, 3848, 0, SOUTH, 2)
        SPIDER_3019(1428, 3861, 0, SOUTH, 8)
        SPIDER_3019(1432, 3877, 0, SOUTH, 8)
        SPIDER_3019(1433, 3853, 0, SOUTH, 8)
        MINE_SUPERVISOR_7076(1442, 3843, 0, SOUTH, 4)
        SPIDER_3019(1447, 3849, 0, SOUTH, 8)
        SPIDER_3019(1447, 3881, 0, SOUTH, 8)
        TOOTHY(1452, 3858, 0, SOUTH, 2)
    }
}