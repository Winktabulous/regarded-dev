package com.near_reality.content.group_ironman.npc.spawns

import com.near_reality.scripts.npc.spawns.NPCSpawnsScript
import com.zenyte.game.util.Direction.*
import com.zenyte.game.world.entity.npc.NpcId.*

class TheNodeSpawns : NPCSpawnsScript() {
    init {
        D3AD1I_F15HER(3114, 3045, 0, SOUTH, 0)
        SEAGULL(3107, 3045, 0, SOUTH, 5)
        SEAGULL(3108, 3039, 0, SOUTH, 5)
        GULL_11297(3101, 3050, 0, SOUTH, 20)
        BUTTERFLY_236(3104, 3030, 0, SOUTH, 10)
        8735(3099, 3025, 0, EAST, 7)
        GROUP_STORAGE_TUTOR(3096, 3027, 0, EAST, 0)
        GROUP_IRON_TUTOR(3107, 3028, 0, WEST, 2)
        ENRAGED_BOAR(3101, 3011, 0, WEST, 3)
        BOAR(3107, 3014, 0, NORTH, 7)
        BOAR(3102, 3011, 0, NORTH, 7)
        BOAR(3098, 3012, 0, SOUTH, 7)
    }
}
