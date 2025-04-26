package com.near_reality.game.content.elven.npc

import com.near_reality.game.content.elven.npc.dialogue.LordIeuanAmloddDialogue
import com.near_reality.scripts.npc.actions.NPCActionScript
import com.zenyte.game.world.entity.npc.NpcId

class LordEuanAmloddNPCAction : NPCActionScript() {
    init {
        npcs(NpcId.LORD_IEUAN_AMLODD_9119)

        "Talk-To" {
            player.dialogueManager.start(LordIeuanAmloddDialogue(player, npc))
        }
    }
}
