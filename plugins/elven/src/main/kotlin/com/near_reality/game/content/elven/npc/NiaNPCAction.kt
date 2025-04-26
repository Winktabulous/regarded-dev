package com.near_reality.game.content.elven.npc

import com.near_reality.game.content.elven.npc.dialogue.NiaDialogue
import com.near_reality.scripts.npc.actions.NPCActionScript
import com.zenyte.game.world.entity.npc.NpcId

class NiaNPCAction : NPCActionScript() {
    init {
        npcs(NpcId.NIA)

        "Talk-To" {
            player.dialogueManager.start(NiaDialogue(player, npc))
        }

        "Trade" {
            // TODO: open shop
        }
    }
}
