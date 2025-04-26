package com.near_reality.plugins.area.osrs_home.npc

import com.near_reality.scripts.npc.actions.NPCActionScript
import com.zenyte.game.content.treasuretrails.TreasureTrail
import com.zenyte.game.world.entity.npc.NpcId

class AbbotLangleyNPCAction : NPCActionScript() {

    init {
        npcs(NpcId.ABBOT_LANGLEY)

        "Talk-To" {
            if (!TreasureTrail.talk(player, npc)) {
                player.dialogueManager.start(AbbotLangleyDialogue(player, npc))
            }
        }
    }

}