package com.near_reality.plugins.spawns

import com.near_reality.scripts.npc.spawns.NPCSpawnsScript
import com.zenyte.game.world.entity.npc.NpcId
import com.zenyte.game.world.entity.npc.NpcId.*
import com.zenyte.game.util.Direction.*

class Region9544Spawns : NPCSpawnsScript() {
    init {
        MEIYERDITCH_MINER_3824(2378, 4624, 2, WEST, 0)
        VAMPYRE_JUVINATE_3691(2380, 4627, 2, SOUTH, 7)
        MEIYERDITCH_MINER_3823(2383, 4636, 2, NORTH, 0)
        VAMPYRE_JUVINATE_3691(2386, 4632, 2, SOUTH, 7)
        MEIYERDITCH_MINER_3821(2389, 4634, 2, NORTH, 0)
        VAMPYRE_JUVINATE(2391, 4619, 2, SOUTH, 7)
        VAMPYRE_JUVINATE(2392, 4623, 2, SOUTH, 7)
        MEIYERDITCH_MINER(2392, 4626, 2, EAST, 0)
        MEIYERDITCH_MINER_3820(2395, 4631, 2, EAST, 0)
        MEIYERDITCH_MINER_3822(2397, 4634, 2, EAST, 0)
    }
}