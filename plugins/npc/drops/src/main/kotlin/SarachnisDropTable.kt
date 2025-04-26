package com.zenyte.game.content

import com.zenyte.game.item.Item
import com.zenyte.game.util.Utils

import com.near_reality.scripts.npc.drops.NPCDropTableScript
import com.zenyte.game.world.entity.npc.NpcId
import com.zenyte.game.world.entity.npc.NpcId.*
import com.zenyte.game.item.ItemId
import com.zenyte.game.item.ItemId.*
import com.near_reality.scripts.npc.drops.table.DropTableType.*
import com.zenyte.game.world.entity.npc.drop.matrix.*
import com.zenyte.game.world.entity.npc.drop.matrix.Drop.*
import com.zenyte.game.world.entity.npc.drop.matrix.DropProcessor.*

class SarachnisDropTable : NPCDropTableScript() {
    init {
        npcs(SARACHNIS)
        
        onDeath {
            val cudgel = Utils.randomNoPlus(125)
            val jarOfEyes = Utils.randomNoPlus(600)
        
            if(cudgel == 0) {
                npc.dropItem(killer, Item(SARACHNIS_CUDGEL))
            }
        
            if(jarOfEyes == 0) {
                npc.dropItem(killer, Item(JAR_OF_EYES))
            }
        
        }
        appendDrop(DisplayedDrop(SARACHNIS_CUDGEL, 1, 1, 125.0))
        appendDrop(DisplayedDrop(JAR_OF_EYES, 1, 1, 600.0))
        
        buildTable(200, overrideTable = false){
        }
    }
}