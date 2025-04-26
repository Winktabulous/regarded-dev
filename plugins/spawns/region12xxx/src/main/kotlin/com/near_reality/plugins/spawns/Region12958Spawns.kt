
package com.near_reality.plugins.spawns

import com.near_reality.scripts.npc.spawns.NPCSpawnsScript
import com.zenyte.game.world.entity.npc.NpcId
import com.zenyte.game.world.entity.npc.NpcId.*
import com.zenyte.game.util.Direction.*

class Region12958Spawns : NPCSpawnsScript() {
    init {
        /*REVENANT_DARK_BEAST*/REVENANT_HELLHOUND(3207, 10164, 0, SOUTH, 3)
        /*REVENANT_DARK_BEAST*/REVENANT_HELLHOUND(3200, 10166, 0, SOUTH, 3)
        REVENANT_ORK(3218, 10133, 0, SOUTH, 2)
        REVENANT_ORK(3227, 10126, 0, SOUTH, 2)
        //REVENANT_HELLHOUND(3246, 10175, 0, SOUTH, 2)
        //REVENANT_HELLHOUND(3239, 10162, 0, SOUTH, 2)
        REVENANT_PYREFIEND(3254, 10140, 0, SOUTH, 2)
        REVENANT_PYREFIEND(3248, 10151, 0, SOUTH, 2)
        REVENANT_HOBGOBLIN(3241, 10121, 0, SOUTH, 2)
        REVENANT_ORK(3232, 10139, 0, SOUTH, 2)
        REVENANT_HELLHOUND(3216, 10164, 0, SOUTH, 2)
    }
}