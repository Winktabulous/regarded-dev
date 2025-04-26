package com.near_reality.plugins.spawns

import com.near_reality.scripts.npc.spawns.NPCSpawnsScript
import com.zenyte.game.world.entity.npc.NpcId
import com.zenyte.game.world.entity.npc.NpcId.*
import com.zenyte.game.util.Direction.*

class Region12703Spawns : NPCSpawnsScript() {
    init {
        REVENANT_CYCLOPS(3167, 10195, 0, SOUTH, 2)
        REVENANT_CYCLOPS(3173, 10185, 0, SOUTH, 2)
        REVENANT_DEMON(3184, 10189, 0, SOUTH, 2)
        REVENANT_DEMON(3184, 10199, 0, SOUTH, 2)
        EMBLEM_TRADER_7943(3196, 10224, 0, SOUTH, 4)
    }
}