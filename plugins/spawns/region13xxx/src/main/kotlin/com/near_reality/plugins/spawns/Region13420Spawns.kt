package com.near_reality.plugins.spawns

import com.near_reality.scripts.npc.spawns.NPCSpawnsScript
import com.zenyte.game.world.entity.npc.NpcId
import com.zenyte.game.world.entity.npc.NpcId.*
import com.zenyte.game.util.Direction.*

class Region13420Spawns : NPCSpawnsScript() {
    init {
        BANKER_2117(3362, 6938, 2, EAST, 0)
        BANKER_2117(3362, 6939, 2, EAST, 0)
        BANKER_2117(3359, 6939, 2, WEST, 0)
        BANKER_2117(3360, 6937, 2, SOUTH, 0)
        BANKER_2117(3361, 6937, 2, SOUTH, 0)
        BANKER_2117(3360, 6940, 2, NORTH, 0)
        BANKER_2117(3361, 6940, 2, NORTH, 0)
        WAYDAR(3365, 6944, 2, NORTH, 3)
        
    }
}