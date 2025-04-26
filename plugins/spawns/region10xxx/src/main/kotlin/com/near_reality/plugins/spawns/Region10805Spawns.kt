package com.near_reality.plugins.spawns

import com.near_reality.scripts.npc.spawns.NPCSpawnsScript
import com.zenyte.game.world.entity.npc.NpcId
import com.zenyte.game.world.entity.npc.NpcId.*
import com.zenyte.game.util.Direction.*

class Region10805Spawns : NPCSpawnsScript() {
    init {
        UNICORN(2700, 3423, 0, SOUTH, 15)
        UNICORN(2700, 3442, 0, SOUTH, 15)
        IGNATIUS_VULCAN(2719, 3431, 0, SOUTH, 0)
        SHERLOCK(2733, 3415, 0, SOUTH, 3)
        FLAX_KEEPER(2744, 3444, 0, SOUTH, 3)
        THORMAC(2702, 3405, 3, SOUTH, 2)
    }
}