package com.near_reality.plugins.spawns

import com.near_reality.scripts.npc.spawns.NPCSpawnsScript
import com.zenyte.game.world.entity.npc.NpcId
import com.zenyte.game.world.entity.npc.NpcId.*
import com.zenyte.game.util.Direction.*

class Region8012Spawns : NPCSpawnsScript() {
    init {
        GULL_284(2012, 4879, 0, SOUTH, 5)
        GULL_285(2016, 4895, 0, SOUTH, 5)
        GULL_285(2017, 4882, 0, SOUTH, 5)
        GULL_284(2021, 4896, 0, SOUTH, 5)
        GULL_284(2024, 4883, 0, SOUTH, 5)
    }
}