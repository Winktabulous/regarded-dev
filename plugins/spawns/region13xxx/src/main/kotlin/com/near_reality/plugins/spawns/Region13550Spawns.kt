package com.near_reality.plugins.spawns

import com.near_reality.scripts.npc.spawns.NPCSpawnsScript
import com.zenyte.game.world.entity.npc.NpcId
import com.zenyte.game.world.entity.npc.NpcId.*
import com.zenyte.game.util.Direction.*

class Region13550Spawns : NPCSpawnsScript() {
    init {
        LAVA_DRAGON(3372, 15264, 0, SOUTH, 3)
        LAVA_DRAGON(3362, 15257, 0, SOUTH, 3)
        REVENANT_ORK(3362, 15267, 0, SOUTH, 3)
        REVENANT_DEMON(3371, 15270, 0, SOUTH, 3)
        REVENANT_CYCLOPS(3358, 15276, 0, SOUTH, 3)
    }
}