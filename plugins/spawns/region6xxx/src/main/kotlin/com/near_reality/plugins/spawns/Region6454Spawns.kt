package com.near_reality.plugins.spawns

import com.near_reality.scripts.npc.spawns.NPCSpawnsScript
import com.zenyte.game.world.entity.npc.NpcId
import com.zenyte.game.world.entity.npc.NpcId.*
import com.zenyte.game.util.Direction.*

class Region6454Spawns : NPCSpawnsScript() {
    init {
        MURFET(1606, 3508, 0, SOUTH, 2)
        FORESTER_7238(1614, 3491, 0, SOUTH, 4)
        NESTY(1615, 3514, 0, SOUTH, 0)
        SAWMILL_OPERATOR(1623, 3500, 0, EAST, 0)
        FORESTER_7238(1628, 3514, 0, SOUTH, 4)
        DUCK(1634, 3502, 0, SOUTH, 18)
        DUCK_1839(1636, 3500, 0, SOUTH, 20)
        DUCKLINGS(1636, 3501, 0, SOUTH, 2)
        GUILDMASTER_LARS(1650, 3499, 0, SOUTH, 2)
        PERRY(1654, 3501, 0, SOUTH, 2)
        BERRY_7235(1657, 3506, 0, SOUTH, 0)
    }
}