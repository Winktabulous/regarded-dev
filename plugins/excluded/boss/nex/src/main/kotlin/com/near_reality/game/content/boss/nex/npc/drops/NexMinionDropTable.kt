package com.near_reality.game.content.boss.nex.npc.drops

import com.near_reality.scripts.npc.drops.NPCDropTableScript
import com.near_reality.scripts.npc.drops.table.DropTableType.Main
import com.zenyte.game.world.entity.npc.NpcId.*
import com.zenyte.game.item.ItemId.*

class NexMinionDropTable : NPCDropTableScript() {
    init {
        npcs(FUMUS, GLACIES, UMBRA, CRUOR)

        buildTable(200) {
            Main {
                // Ancient ceremonial robes
                ANCIENT_CEREMONIAL_MASK quantity 1 rarity 1
                ANCIENT_CEREMONIAL_TOP quantity 1 rarity 1
                ANCIENT_CEREMONIAL_LEGS quantity 1 rarity 1
                ANCIENT_CEREMONIAL_GLOVES quantity 1 rarity 1
                ANCIENT_CEREMONIAL_BOOTS quantity 1 rarity 1
            }
        }

    }
}
