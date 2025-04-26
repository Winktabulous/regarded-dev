package com.near_reality.plugins.spawns

import com.near_reality.scripts.npc.spawns.NPCSpawnsScript
import com.zenyte.game.world.entity.npc.NpcId
import com.zenyte.game.world.entity.npc.NpcId.*
import com.zenyte.game.util.Direction.*

class Region9773Spawns : NPCSpawnsScript() {
    init {
        FISHING_SPOT_7946(2453, 2891, 0, SOUTH, 5)
        FISHING_SPOT_7946(2456, 2893, 0, SOUTH, 5)
        FISHING_SPOT_7946(2458, 2890, 0, SOUTH, 5)
        WOLF(2482, 2923, 0, SOUTH, 6)
        BIG_WOLF_115(2487, 2924, 0, SOUTH, 5)
        WOLF(2491, 2922, 0, SOUTH, 6)
        WOLF(2491, 2927, 0, SOUTH, 6)
        WOLF(2491, 2931, 0, SOUTH, 6)
    }
}