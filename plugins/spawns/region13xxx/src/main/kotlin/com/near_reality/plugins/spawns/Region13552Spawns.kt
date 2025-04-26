package com.near_reality.plugins.spawns

import com.near_reality.scripts.npc.spawns.NPCSpawnsScript
import com.zenyte.game.world.entity.npc.NpcId
import com.zenyte.game.world.entity.npc.NpcId.*
import com.zenyte.game.util.Direction.*

class Region13552Spawns : NPCSpawnsScript() {
    init {
        REVENANT_DRAGON(3370, 15391, 0, SOUTH, 5)
        REVENANT_DARK_BEAST(3360, 15404, 0, SOUTH, 5)
        REVENANT_KNIGHT(3358, 15391, 0, SOUTH, 5)
    }
}