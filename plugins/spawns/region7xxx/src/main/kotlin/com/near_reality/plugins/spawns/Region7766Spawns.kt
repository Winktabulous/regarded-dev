package com.near_reality.plugins.spawns

import com.near_reality.scripts.npc.spawns.NPCSpawnsScript
import com.zenyte.game.world.entity.npc.NpcId
import com.zenyte.game.world.entity.npc.NpcId.*
import com.zenyte.game.util.Direction.*

class Region7766Spawns : NPCSpawnsScript() {
    init {
        GNOME_GUARD_6081(1964, 5519, 3, SOUTH, 5)
        GNOME_TROOP_4974(1964, 5522, 3, SOUTH, 4)
        GNOME_GUARD_6081(1970, 5519, 3, SOUTH, 5)
        GNOME_TROOP(1970, 5522, 3, SOUTH, 3)
    }
}