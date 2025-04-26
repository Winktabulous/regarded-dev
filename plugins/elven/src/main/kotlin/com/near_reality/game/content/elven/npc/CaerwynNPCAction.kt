package com.near_reality.game.content.elven.npc

import com.near_reality.game.content.elven.npc.dialogue.ElganDialogue
import com.near_reality.scripts.npc.actions.NPCActionScript
import com.zenyte.game.world.entity.npc.NpcId

class CaerwynNPCAction : NPCActionScript() {
    
    init {
        npcs(NpcId.CAERWYN)

        "Talk-To" {
            player.dialogueManager.start(ElganDialogue(player, npc))
        }

        "Trade" {
            // TODO: open shop
        }

        // TODO: Overhead dialogue
    }

}
