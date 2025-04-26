package com.near_reality.plugins.spawns

import com.near_reality.scripts.npc.spawns.NPCSpawnsScript
import com.zenyte.game.world.entity.npc.NpcId
import com.zenyte.game.world.entity.npc.NpcId.*
import com.zenyte.game.util.Direction.*

class Region10322Spawns : NPCSpawnsScript() {
    init {
        PRIVATE_PALDON(2579, 5290, 0, SOUTH, 3)
        PRIVATE_PIERREB(2583, 5280, 0, SOUTH, 4)
        PRIVATE_PALDO(2589, 5264, 0, SOUTH, 5)
        SERGEANT_SAMBUR(2602, 5280, 0, SOUTH, 5)
        PRIVATE_PENDRON(2604, 5290, 0, SOUTH, 3)
    }
}