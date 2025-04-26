package com.near_reality.plugins.spawns

import com.near_reality.scripts.npc.spawns.NPCSpawnsScript
import com.zenyte.game.world.entity.npc.NpcId
import com.zenyte.game.world.entity.npc.NpcId.*
import com.zenyte.game.util.Direction.*

class Region5948Spawns : NPCSpawnsScript() {
    init {
        SPIDER_3019(1475, 3873, 0, SOUTH, 8)
        SPIDER_3019(1479, 3853, 0, SOUTH, 8)
        SPIDER_3019(1488, 3880, 0, SOUTH, 8)
        SPIDER_3019(1498, 3872, 0, SOUTH, 8)
        SPIDER_3019(1504, 3854, 0, SOUTH, 8)
        OPERATOR_7073(1476, 3864, 0, EAST, 0)
        OPERATOR_7073(1479, 3874, 0, EAST, 0)
        OPERATOR(1497, 3864, 0, EAST, 0)
        OPERATOR_7073(1498, 3871, 0, EAST, 0)
        BLASTED_ORE(1479, 3873, 0, SOUTH, 5)
        BLASTED_ORE(1498, 3870, 0, SOUTH, 5)
        BLASTED_ORE(1479, 3873, 0, SOUTH, 5)
        BLASTED_ORE(1498, 3870, 0, SOUTH, 5)
        BLASTED_ORE(1477, 3864, 0, SOUTH, 5)
        BLASTED_ORE(1479, 3873, 0, SOUTH, 5)
        BLASTED_ORE(1498, 3870, 0, SOUTH, 5)
    }
}