package com.near_reality.plugins.spawns

import com.near_reality.scripts.npc.spawns.NPCSpawnsScript
import com.zenyte.game.world.entity.npc.NpcId
import com.zenyte.game.world.entity.npc.NpcId.*
import com.zenyte.game.util.Direction.*

class Region9772Spawns : NPCSpawnsScript() {
    init {
        MYSTERIOUS_ADVENTURER(2442, 2859, 0, WEST, 0)
        FREJA(2454, 2850, 0, NORTH, 0)
        DOG_8041(2457, 2844, 0, SOUTH, 2)
        ALEC_KINCADE(2458, 2868, 0, SOUTH, 2)
        ELLEN(2466, 2850, 0, SOUTH, 2)
        HARIS(2480, 2875, 0, EAST, 0)
        LORD_MARSHAL_BROGAN(2490, 2850, 0, SOUTH, 0)
        ALTARKIZ(2492, 2848, 0, WEST, 0)
        LUTWIDGE(2448, 2857, 1, SOUTH, 0)
        PRIMULA(2454, 2853, 1, SOUTH, 2)
        JACK_8037(2448, 2847, 2, SOUTH, 2)
        ERDAN(2463, 2847, 2, SOUTH, 2)
        DIANA(2466, 2846, 2, SOUTH, 2)
    }
}