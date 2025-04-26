package com.near_reality.plugins.spawns

import com.near_reality.scripts.npc.spawns.NPCSpawnsScript
import com.zenyte.game.world.entity.npc.NpcId
import com.zenyte.game.world.entity.npc.NpcId.*
import com.zenyte.game.util.Direction.*

class Region16196Spawns : NPCSpawnsScript() {
    init {
        TORRMENTED_DEMON(4038, 4380, 0, SOUTH, 5)
        TORRMENTED_DEMON(4040, 4385, 0, SOUTH, 5)
        TORRMENTED_DEMON(4039, 4391, 0, SOUTH, 5)
        
        TORRMENTED_DEMON(4088, 4368, 0, SOUTH, 5)
        TORRMENTED_DEMON(4083, 4374, 0, SOUTH, 5)
        TORRMENTED_DEMON(4088, 4380, 0, SOUTH, 5)
    }
}