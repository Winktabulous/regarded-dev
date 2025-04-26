package com.near_reality.plugins.spawns

import com.near_reality.scripts.npc.spawns.NPCSpawnsScript
import com.zenyte.game.world.entity.npc.NpcId
import com.zenyte.game.world.entity.npc.NpcId.*
import com.zenyte.game.util.Direction.*

class Region5023Spawns : NPCSpawnsScript() {
    init {
        WYRM(1260, 10192, 0, SOUTH, 12)
        WYRM(1262, 10184, 0, SOUTH, 12)
        WYRM(1264, 10197, 0, SOUTH, 12)
        WYRM(1268, 10179, 0, SOUTH, 12)
        WYRM(1268, 10191, 0, SOUTH, 12)
        WYRM(1270, 10186, 0, SOUTH, 12)
        WYRM(1275, 10181, 0, SOUTH, 12)
        WYRM(1275, 10189, 0, SOUTH, 12)
        GREATER_DEMON_2028(1272, 10204, 1, SOUTH, 4)
        GREATER_DEMON_2027(1273, 10199, 1, SOUTH, 2)
        GREATER_DEMON(1277, 10195, 1, SOUTH, 2)
        GREATER_DEMON_2027(1278, 10207, 1, SOUTH, 2)
        GREATER_DEMON_2029(1279, 10200, 1, SOUTH, 7)
        GREATER_DEMON_2027(1279, 10213, 1, SOUTH, 2)
        FIRE_GIANT_2076(1269, 10211, 2, SOUTH, 0)
        FIRE_GIANT_2077(1272, 10194, 2, SOUTH, 0)
        FIRE_GIANT(1273, 10212, 2, SOUTH, 4)
        FIRE_GIANT_2076(1274, 10197, 2, SOUTH, 0)
        FIRE_GIANT_2077(1275, 10209, 2, SOUTH, 0)
        FIRE_GIANT_2079(1277, 10203, 2, SOUTH, 2)
        FIRE_GIANT(1277, 10215, 2, SOUTH, 4)
    }
}