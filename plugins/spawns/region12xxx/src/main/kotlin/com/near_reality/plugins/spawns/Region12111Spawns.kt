package com.near_reality.plugins.spawns

import com.near_reality.scripts.npc.spawns.NPCSpawnsScript
import com.zenyte.game.world.entity.npc.NpcId
import com.zenyte.game.world.entity.npc.NpcId.*
import com.zenyte.game.util.Direction.*

class Region12111Spawns : NPCSpawnsScript() {
    init {
        ROGUE_GUARD_3191(3017, 5060, 1, SOUTH, 5)
        ROGUE_GUARD(3018, 5072, 1, SOUTH, 5)
        SPIN_BLADES(3044, 5105, 1, SOUTH, 5)
        SPIN_BLADES_3196(3059, 5090, 1, SOUTH, 5)
    }
}