package com.near_reality.plugins.spawns

import com.near_reality.scripts.npc.spawns.NPCSpawnsScript
import com.zenyte.game.world.entity.npc.NpcId
import com.zenyte.game.world.entity.npc.NpcId.*
import com.zenyte.game.util.Direction.*

class Region13364Spawns : NPCSpawnsScript() {
    init {
        ARCHAEOLOGICAL_EXPERT(3355, 3333, 0, SOUTH, 4)
        DIGSITE_WORKMAN_3630(3356, 3385, 0, SOUTH, 5)
        EXAMINER_3636(3360, 3343, 0, SOUTH, 5)
        EXAMINER_3637(3364, 3339, 0, SOUTH, 4)
        EXAMINER(3364, 3342, 0, SOUTH, 3)
        RESEARCHER(3365, 3333, 0, SOUTH, 3)
        DIGSITE_WORKMAN(3370, 3387, 0, SOUTH, 5)
        NISHA(3376, 3373, 0, SOUTH, 0)
        PANNING_GUIDE(3376, 3377, 0, SOUTH, 3)
        NICK(3381, 3379, 0, SOUTH, 0)
    }
}