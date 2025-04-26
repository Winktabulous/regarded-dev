package com.near_reality.plugins.area.osrs_home.npc

import com.near_reality.scripts.npc.actions.NPCActionScript
import com.zenyte.game.world.entity.npc.NpcId.*

class MonkNPCAction : NPCActionScript() {

    init {
        npcs(MONK, MONK_1159, MONK_1171, MONK_2579, MONK_4068)

        "Talk-To" {
            player.dialogueManager.start(MonkDialogue(player, npc))
        }
    }

}