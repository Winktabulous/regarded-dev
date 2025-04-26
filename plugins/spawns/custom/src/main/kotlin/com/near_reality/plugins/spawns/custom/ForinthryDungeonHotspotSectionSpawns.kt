package com.near_reality.plugins.spawns.custom

import com.near_reality.scripts.npc.spawns.NPCSpawnsScript
import com.zenyte.game.world.entity.npc.NpcId
import com.zenyte.game.world.entity.npc.NpcId.*
import com.zenyte.game.util.Direction.*

class ForinthryDungeonHotspotSectionSpawns : NPCSpawnsScript() {
    init {
        REVENANT_ORK(3237, 10167, walkRadius = 2)
        REVENANT_KNIGHT(3244, 10171, walkRadius = 6)
        REVENANT_DRAGON(3242, 10179, walkRadius = 2)
        REVENANT_HELLHOUND(3251, 10168, walkRadius = 2)
        REVENANT_IMP(3254, 10185, walkRadius = 3)
        REVENANT_DEMON(3251, 10181, walkRadius = 2)
    }
}