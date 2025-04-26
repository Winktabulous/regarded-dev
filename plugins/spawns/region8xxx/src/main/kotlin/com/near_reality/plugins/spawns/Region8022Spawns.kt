package com.near_reality.plugins.spawns

import com.near_reality.scripts.npc.spawns.NPCSpawnsScript
import com.zenyte.game.world.entity.npc.NpcId
import com.zenyte.game.world.entity.npc.NpcId.*
import com.zenyte.game.util.Direction.*

class Region8022Spawns : NPCSpawnsScript() {
    init {
        BUTTERFLY_236(1986, 5564, 0, SOUTH, 9)
        GNOME_6095(2033, 5530, 1, SOUTH, 4)
        GNOME_GUARD_6082(2012, 5535, 2, SOUTH, 5)
        GNOME_GUARD_6082(2023, 5544, 2, SOUTH, 5)
    }
}