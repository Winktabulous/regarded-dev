package com.near_reality.plugins.spawns

import com.near_reality.scripts.npc.spawns.NPCSpawnsScript
import com.zenyte.game.world.entity.npc.NpcId
import com.zenyte.game.world.entity.npc.NpcId.*
import com.zenyte.game.util.Direction.*

class Region12957Spawns : NPCSpawnsScript() {
    init {
        REVENANT_ORK(3209, 10097, 0, SOUTH, 2)
        REVENANT_ORK(3220, 10095, 0, SOUTH, 2)
        REVENANT_GOBLIN(3225, 10070, 0, SOUTH, 2)
        REVENANT_GOBLIN(3224, 10074, 0, SOUTH, 2)
        REVENANT_HOBGOBLIN(3244, 10101, 0, SOUTH, 2)
        REVENANT_HOBGOBLIN(3240, 10092, 0, SOUTH, 2)
        REVENANT_IMP(3200, 10070, 0, SOUTH, 2)
        REVENANT_GOBLIN(3220, 10064, 0, SOUTH, 2)
        REVENANT_HOBGOBLIN(3243, 10083, 0, SOUTH, 2)
    }
}