package com.near_reality.plugins.spawns

import com.near_reality.scripts.npc.spawns.NPCSpawnsScript
import com.zenyte.game.world.entity.npc.NpcId
import com.zenyte.game.world.entity.npc.NpcId.*
import com.zenyte.game.util.Direction.*

class Region10642Spawns : NPCSpawnsScript() {
    init {
        BUGS(2640, 9391, 0, SOUTH, 3)
        FYCIE(2650, 9393, 0, SOUTH, 5)
    }
}