package com.near_reality.plugins.spawns

import com.near_reality.scripts.npc.spawns.NPCSpawnsScript
import com.zenyte.game.world.entity.npc.NpcId
import com.zenyte.game.world.entity.npc.NpcId.*
import com.zenyte.game.util.Direction.*

class Region8009Spawns : NPCSpawnsScript() {
    init {
        WIZARD_3257(2010, 4710, 1, SOUTH, 9)
        WIZARD_3257(2010, 4714, 1, SOUTH, 9)
        KOFTIK(2012, 4713, 1, SOUTH, 5)
    }
}