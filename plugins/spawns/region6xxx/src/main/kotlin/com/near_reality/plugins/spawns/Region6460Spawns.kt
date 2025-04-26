package com.near_reality.plugins.spawns

import com.near_reality.scripts.npc.spawns.NPCSpawnsScript
import com.zenyte.game.world.entity.npc.NpcId
import com.zenyte.game.world.entity.npc.NpcId.*
import com.zenyte.game.util.Direction.*

class Region6460Spawns : NPCSpawnsScript() {
    init {
        BAT(1603, 3844, 0, SOUTH, 23)
        GIANT_BAT_6824(1612, 3849, 0, SOUTH, 8)
        BAT(1615, 3841, 0, SOUTH, 23)
        BAT(1624, 3853, 0, SOUTH, 23)
        LOOKOUT(1624, 3877, 0, SOUTH, 2)
        GIANT_BAT_6824(1633, 3842, 0, SOUTH, 8)
        GIANT_BAT_6824(1633, 3879, 0, SOUTH, 8)
        TROSSA(1635, 3868, 0, SOUTH, 2)
        BAT(1655, 3880, 0, SOUTH, 23)
        GIANT_BAT_6824(1662, 3870, 0, SOUTH, 8)
    }
}