package com.near_reality.plugins.spawns

import com.near_reality.scripts.npc.spawns.NPCSpawnsScript
import com.zenyte.game.world.entity.npc.NpcId
import com.zenyte.game.world.entity.npc.NpcId.*
import com.zenyte.game.util.Direction.*

class Region9527Spawns : NPCSpawnsScript() {
    init {
        DARK_KEBBIT(2374, 3575, 0, SOUTH, 2)
        DASHING_KEBBIT(2375, 3578, 0, SOUTH, 3)
        DARK_KEBBIT(2380, 3581, 0, SOUTH, 2)
        TORTOISE_6076(2410, 3528, 0, SOUTH, 4)
        TORTOISE(2416, 3527, 0, SOUTH, 4)
        TRAINER_NACKLEPEN(2421, 3525, 0, SOUTH, 5)
        TORTOISE(2424, 3529, 0, SOUTH, 4)
    }
}