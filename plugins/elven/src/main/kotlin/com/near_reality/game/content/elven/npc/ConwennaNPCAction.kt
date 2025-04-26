package com.near_reality.game.content.elven.npc

import com.near_reality.game.content.elven.npc.dialogue.ConwennaDialogue
import com.near_reality.scripts.npc.actions.NPCActionScript

class ConwennaNPCAction : NPCActionScript() {

    init {
        npcs(9240)

        "Talk-To" {
            player.dialogueManager.start(ConwennaDialogue(player, npc))
        }
    }

}
