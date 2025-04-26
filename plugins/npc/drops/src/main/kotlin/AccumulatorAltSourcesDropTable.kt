package com.zenyte.game.content

import com.near_reality.scripts.npc.drops.NPCDropTableScript
import com.near_reality.scripts.npc.drops.table.DropTableType.Main
import com.zenyte.game.item.ItemId
import mgi.types.config.npcs.NPCDefinitions

class AccumulatorAltSourcesDropTable : NPCDropTableScript() {
    init {
        val npcNames = setOf("thug")

        //println(npcNames)

        npcs(*NPCDefinitions.getDefinitions()
            .filterNotNull()
            .mapNotNull { npcDef -> npcDef.takeIf { npcNames.contains(it.name.lowercase()) }?.id }
            .toSet()
            .toIntArray()
        )

        onDeath {
            rollStaticTableAndDrop(killer, Main)
        }

        buildTable {
            Main(100) { // 2% chance
                ItemId.AVAS_ACCUMULATOR quantity 1 rarity 2
            }
        }
    }
}
