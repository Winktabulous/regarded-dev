package com.near_reality.plugins.spawns.custom

import com.zenyte.game.world.entity.Location
import com.zenyte.game.world.region.area.RegisterIslandArea

import com.near_reality.scripts.npc.spawns.NPCSpawnsScript
import com.zenyte.game.world.entity.npc.NpcId
import com.zenyte.game.world.entity.npc.NpcId.*
import com.zenyte.game.util.Direction.*

class StarterGuideSpawns : NPCSpawnsScript() {
    init {
        val loc: Location = RegisterIslandArea.ZENYTE_GUIDE_LOCATION
        
        NEARREALITY_GUIDE(loc.x, loc.y, loc.plane)
    }
}