package com.near_reality.plugins.spawns

import com.near_reality.scripts.npc.spawns.NPCSpawnsScript
import com.zenyte.game.world.entity.npc.NpcId
import com.zenyte.game.world.entity.npc.NpcId.*
import com.zenyte.game.util.Direction.*

class Region10545Spawns : NPCSpawnsScript() {
    init {
        SHOP_KEEPER_2888(2641, 3171, 0, SOUTH, 2)
        SHOP_KEEPER_2888(2655, 3152, 0, SOUTH, 2)
        MURPHY(2668, 3162, 0, SOUTH, 2)
        MURPHY_10707(2671, 3172, 1, SOUTH, 5)
        1330(2673, 3144, 0, SOUTH, 4)
        1332(2675, 3144, 0, SOUTH, 2)
        TINDEL_MARCHANT(2678, 3153, 0, SOUTH, 2)
    }
}