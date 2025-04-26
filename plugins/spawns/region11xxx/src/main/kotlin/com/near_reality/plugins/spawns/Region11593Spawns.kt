package com.near_reality.plugins.spawns

import com.near_reality.scripts.npc.spawns.NPCSpawnsScript
import com.zenyte.game.world.entity.npc.NpcId
import com.zenyte.game.world.entity.npc.NpcId.*
import com.zenyte.game.util.Direction.*

class Region11593Spawns : NPCSpawnsScript() {
    init {
        SOLDIER_5421(2924, 4702, 0, SOUTH, 5)
        5382(2934, 4700, 0, SOUTH, 2)
        GUARD_CAPTAIN(2935, 4678, 0, SOUTH, 2)
        BARTENDER_1320(2940, 4678, 0, SOUTH, 2)
        WIZARD_4399(2928, 4712, 2, SOUTH, 5)
        WIZARD_4398(2928, 4717, 2, SOUTH, 5)
        WIZARD_4400(2933, 4712, 2, SOUTH, 5)
        WATCHTOWER_WIZARD(2933, 4716, 2, SOUTH, 5)
    }
}