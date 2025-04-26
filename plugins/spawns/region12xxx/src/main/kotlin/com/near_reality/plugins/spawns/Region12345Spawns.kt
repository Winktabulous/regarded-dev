package com.near_reality.plugins.spawns

import com.near_reality.scripts.npc.spawns.NPCSpawnsScript
import com.zenyte.game.world.entity.npc.NpcId
import com.zenyte.game.world.entity.npc.NpcId.*
import com.zenyte.game.util.Direction.*

class Region12345Spawns : NPCSpawnsScript() {
    init {
        GHOST(3088, 3700, 0, SOUTH, 5)
        GHOST_86(3092, 3692, 0, SOUTH, 5)
        GHOST_87(3097, 3700, 0, SOUTH, 2)
        GRIZZLY_BEAR(3098, 3652, 0, SOUTH, 11)
        GHOST_90(3105, 3696, 0, SOUTH, 8)
        GRIZZLY_BEAR(3106, 3677, 0, SOUTH, 11)
        GRIZZLY_BEAR(3110, 3664, 0, SOUTH, 11)
        GHOST_92(3113, 3689, 0, SOUTH, 6)
        GRIZZLY_BEAR(3119, 3673, 0, SOUTH, 11)
        GRIZZLY_BEAR(3120, 3653, 0, SOUTH, 11)
        GRIZZLY_BEAR(3125, 3693, 0, SOUTH, 11)
    }
}