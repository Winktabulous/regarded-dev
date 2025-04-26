package com.near_reality.plugins.spawns

import com.near_reality.scripts.npc.spawns.NPCSpawnsScript
import com.zenyte.game.world.entity.npc.NpcId
import com.zenyte.game.world.entity.npc.NpcId.*
import com.zenyte.game.util.Direction.*

class Region7508Spawns : NPCSpawnsScript() {
    init {
        MAJOR_COLLECT(1883, 5401, 0, SOUTH, 5)
        MAJOR_HEAL(1886, 5401, 0, SOUTH, 5)
        MAJOR_ATTACK(1889, 5401, 0, SOUTH, 5)
        MAJOR_DEFEND(1892, 5401, 0, SOUTH, 5)
        EGG_LAUNCHER(1877, 5410, 0, SOUTH, 0)
    }
}