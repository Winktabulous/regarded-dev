package com.near_reality.plugins.spawns

import com.near_reality.scripts.npc.spawns.NPCSpawnsScript
import com.zenyte.game.world.entity.npc.NpcId
import com.zenyte.game.world.entity.npc.NpcId.*
import com.zenyte.game.util.Direction.*

class Region9363Spawns : NPCSpawnsScript() {
    init {
        SMOKE_DEVIL(2351, 9448, 0, SOUTH, 4)
        SMOKE_DEVIL(2354, 9455, 0, SOUTH, 4)
        SMOKE_DEVIL(2358, 9445, 0, SOUTH, 4)
        THERMONUCLEAR_SMOKE_DEVIL(2360, 9452, 0, SOUTH, 3)
        SMOKE_DEVIL(2367, 9455, 0, SOUTH, 4)
    }
}