package com.near_reality.plugins.spawns

import com.near_reality.scripts.npc.spawns.NPCSpawnsScript
import com.zenyte.game.world.entity.npc.NpcId
import com.zenyte.game.world.entity.npc.NpcId.*
import com.zenyte.game.util.Direction.*

class Region12861Spawns : NPCSpawnsScript() {
    init {
        WOLF(3207, 3919, 0, SOUTH, 6)
        WOLF(3218, 3924, 0, SOUTH, 6)
        SCORPION_3024(3225, 3943, 0, SOUTH, 12)
        WOLF(3229, 3917, 0, SOUTH, 6)
        SCORPION_3024(3231, 3940, 0, SOUTH, 12)
        SCORPION_3024(3235, 3946, 0, SOUTH, 12)
        SCORPION_3024(3239, 3942, 0, SOUTH, 12)
        WOLF(3241, 3921, 0, SOUTH, 6)
        SCORPION_3024(3246, 3945, 0, SOUTH, 12)
        SCORPION_3024(3249, 3949, 0, SOUTH, 12)
        SCORPION_3024(3256, 3954, 0, SOUTH, 12)
        CHAOS_ELEMENTAL(3261, 3927, 0, SOUTH, 10)
    }
}