package com.near_reality.plugins.spawns

import com.near_reality.scripts.npc.spawns.NPCSpawnsScript
import com.zenyte.game.world.entity.npc.NpcId
import com.zenyte.game.world.entity.npc.NpcId.*
import com.zenyte.game.util.Direction.*

class Region5022Spawns : NPCSpawnsScript() {
    init {
        WYRM(1252, 10159, 0, SOUTH, 12)
        WYRM(1255, 10147, 0, SOUTH, 12)
        WYRM(1259, 10154, 0, SOUTH, 12)
        WYRM(1259, 10159, 0, SOUTH, 12)
        WYRM(1266, 10152, 0, SOUTH, 12)
        WYRM(1267, 10157, 0, SOUTH, 12)
        WYRM(1273, 10158, 0, SOUTH, 12)
        GARBEK_QUO_MATEN(1276, 10162, 0, WEST, 0)
    }
}