package com.near_reality.plugins.spawns

import com.near_reality.scripts.npc.spawns.NPCSpawnsScript
import com.zenyte.game.world.entity.npc.NpcId
import com.zenyte.game.world.entity.npc.NpcId.*
import com.zenyte.game.util.Direction.*

class Region10038Spawns : NPCSpawnsScript() {
    init {
        FISHING_SPOT_1542(2500, 3506, 0, SOUTH, 0)
        FISHING_SPOT_1542(2500, 3512, 0, SOUTH, 0)
        OTTO_GODBLESSED(2501, 3487, 0, SOUTH, 2)
        FISHING_SPOT_1542(2504, 3497, 0, SOUTH, 0)
        FISHING_SPOT_1542(2506, 3493, 0, SOUTH, 0)
        HUDON(2511, 3484, 0, SOUTH, 0)
        FISHING_SPOT_1542(2520, 3518, 0, SOUTH, 0)
        ALMERA(2522, 3498, 0, SOUTH, 2)
        GUARD_5188(2554, 3472, 0, SOUTH, 0)
        GUARD_5189(2554, 3476, 0, SOUTH, 0)
    }
}