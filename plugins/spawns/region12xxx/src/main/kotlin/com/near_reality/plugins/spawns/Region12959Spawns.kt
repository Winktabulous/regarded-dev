package com.near_reality.plugins.spawns

import com.near_reality.scripts.npc.spawns.NPCSpawnsScript
import com.zenyte.game.world.entity.npc.NpcId
import com.zenyte.game.world.entity.npc.NpcId.*
import com.zenyte.game.util.Direction.*

class Region12959Spawns : NPCSpawnsScript() {
    init {
        /*REVENANT_KNIGHT*/REVENANT_ORK(3209, 10216, 0, SOUTH, 6)
        /*REVENANT_KNIGHT*/REVENANT_HELLHOUND(3227, 10220, 0, SOUTH, 6)
        REVENANT_IMP(3214, 10195, 0, SOUTH, 3)
        REVENANT_IMP(3218, 10188, 0, SOUTH, 3)
        REVENANT_DRAGON(3237, 10201, 0, SOUTH, 2)
        REVENANT_DRAGON(3230, 10200, 0, SOUTH, 2)
        REVENANT_IMP(3219, 10198, 0, SOUTH, 3)
    }
}