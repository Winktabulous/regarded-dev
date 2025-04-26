package com.near_reality.plugins.spawns

import com.near_reality.scripts.npc.spawns.NPCSpawnsScript
import com.zenyte.game.world.entity.npc.NpcId
import com.zenyte.game.world.entity.npc.NpcId.*
import com.zenyte.game.util.Direction.*

class Region13106Spawns : NPCSpawnsScript() {
    init {
        BORDER_GUARD(3267, 3226, 0, WEST, 0)
        BORDER_GUARD(3267, 3229, 0, WEST, 0)
        BORDER_GUARD_4288(3268, 3226, 0, EAST, 0)
        BORDER_GUARD_4288(3268, 3229, 0, EAST, 0)
        6089(3284, 3212, 0, NORTH, 0)
        5953(3285, 3201, 0, SOUTH, 4)
        GEM_TRADER(3288, 3212, 0, SOUTH, 2)
        OLLIE_THE_CAMEL(3290, 3209, 0, SOUTH, 5)
        MAN_3261(3293, 3205, 0, SOUTH, 15)
        CAM_THE_CAMEL(3296, 3227, 0, SOUTH, 3)
        MAN_3108(3298, 3206, 0, SOUTH, 11)
        SILK_TRADER(3299, 3204, 0, SOUTH, 2)
        6554(3303, 3204, 0, SOUTH, 2)
        ALI_MORRISANE(3304, 3211, 0, SOUTH_EAST, 0)
        ELLY_THE_CAMEL(3311, 3208, 0, SOUTH, 2)
        MUBARIZ(3314, 3240, 0, SOUTH, 2)
        AYESHA(3316, 3205, 0, SOUTH, 2)
        TOOL_LEPRECHAUN(3319, 3204, 0, SOUTH, 0)
        RAT_2854(3319, 3250, 0, SOUTH, 14)
    }
}