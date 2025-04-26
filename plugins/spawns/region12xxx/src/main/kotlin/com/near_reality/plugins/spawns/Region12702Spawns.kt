package com.near_reality.plugins.spawns

import com.near_reality.scripts.npc.spawns.NPCSpawnsScript
import com.zenyte.game.world.entity.npc.NpcId
import com.zenyte.game.world.entity.npc.NpcId.*
import com.zenyte.game.util.Direction.*

class Region12702Spawns : NPCSpawnsScript() {
    init {
        REVENANT_DEMON(3162, 10114, 0, SOUTH, 2)
        REVENANT_DEMON(3152, 10113, 0, SOUTH, 2)
        REVENANT_PYREFIEND(3170, 10155, 0, SOUTH, 2)
        REVENANT_PYREFIEND(3181, 10152, 0, SOUTH, 2)
        REVENANT_PYREFIEND(3167, 10165, 0, SOUTH, 2)
    }
}