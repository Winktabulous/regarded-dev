package com.near_reality.plugins.spawns

import com.near_reality.scripts.npc.spawns.NPCSpawnsScript
import com.zenyte.game.world.entity.npc.NpcId
import com.zenyte.game.world.entity.npc.NpcId.*
import com.zenyte.game.util.Direction.*

class Region13614Spawns : NPCSpawnsScript() {
    init {
        JACKAL(3400, 2997, 0, SOUTH, 11)
        JACKAL(3402, 2999, 0, SOUTH, 11)
        DESERT_SNAKE(3403, 2963, 0, SOUTH, 3)
        JACKAL(3403, 2997, 0, SOUTH, 11)
        DESERT_SNAKE(3429, 2976, 0, SOUTH, 3)
        JACKAL(3445, 2993, 0, SOUTH, 11)
        JACKAL(3446, 2992, 0, SOUTH, 11)
        JACKAL(3448, 2991, 0, SOUTH, 11)
        JACKAL(3448, 2994, 0, SOUTH, 11)
    }
}