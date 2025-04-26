package com.near_reality.plugins.area.osrs_home.npc

import com.near_reality.scripts.npc.actions.NPCActionScript
import com.zenyte.game.world.entity.npc.NpcId
import com.zenyte.game.world.entity.player.dialogue.dialogue

class BrotherAlthricNPCAction : NPCActionScript() {

    init {
        npcs(NpcId.BROTHER_ALTHRIC)

        "Talk-To" {
            player.dialogue(npc) {
                player("Very nice rosebushes you have here.")
                npc("Yes, it has taken me many long hours in this garden to bring them to this state of near-perfection.")
            }
        }

    }

}