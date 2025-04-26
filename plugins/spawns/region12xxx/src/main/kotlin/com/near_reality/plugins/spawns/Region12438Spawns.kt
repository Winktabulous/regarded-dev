package com.near_reality.plugins.spawns

import com.near_reality.scripts.npc.spawns.NPCSpawnsScript
import com.zenyte.game.world.entity.npc.NpcId
import com.zenyte.game.world.entity.npc.NpcId.*
import com.zenyte.game.util.Direction.*

class Region12438Spawns : NPCSpawnsScript() {
    init {
        ZOMBIE_38(3119, 9647, 0, SOUTH, 3)
        ZOMBIE_57(3122, 9658, 0, SOUTH, 7)
        ZOMBIE_40(3124, 9651, 0, SOUTH, 4)
        ZOMBIE_55(3124, 9662, 0, SOUTH, 4)
    }
}