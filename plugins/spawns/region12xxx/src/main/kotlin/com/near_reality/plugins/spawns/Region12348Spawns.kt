package com.near_reality.plugins.spawns

import com.near_reality.scripts.npc.spawns.NPCSpawnsScript
import com.zenyte.game.world.entity.npc.NpcId
import com.zenyte.game.world.entity.npc.NpcId.*
import com.zenyte.game.util.Direction.*

class Region12348Spawns : NPCSpawnsScript() {
    init {
        KING_SCORPION(3075, 3848, 0, SOUTH, 8)
        KING_SCORPION(3090, 3841, 0, SOUTH, 8)
        LESSER_DEMON_2008(3091, 3861, 0, SOUTH, 6)
        HILL_GIANT_2103(3094, 3849, 0, SOUTH, 3)
        LESSER_DEMON_2006(3094, 3869, 0, SOUTH, 6)
        HILL_GIANT_2100(3104, 3875, 0, SOUTH, 3)
        HILL_GIANT_2101(3110, 3854, 0, SOUTH, 3)
        HILL_GIANT_2102(3117, 3854, 0, SOUTH, 3)
    }
}