package com.near_reality.content.group_ironman.npc.actions

import com.near_reality.scripts.npc.actions.NPCActionScript
import com.zenyte.game.world.entity.npc.NpcId
import com.zenyte.game.world.entity.player.dialogue.dialogue

class Boar31337killerNpcAction : NPCActionScript() {
    init {
        npcs(NpcId.BOAR31337KILLER)

        "Talk-to"{
            player.dialogue(npc) {
                player("Why do you keep killing boars?")
                npc("Because they drop bones which I am going to use to " +
                        "train my prayer. The meat is also a nice addition.")
                player("Wouldn't it be best to go steal cakes for food?")
                npc("And then go to Wintertodt? I do that every time I " +
                        "make an Iron person account. About time I try something new.")
                player("Are you at least killing the boars on task?")
                npc("No, maybe I should go get a slayer task now you " +
                        "mention it. Thanks for the reminder.")
            }
        }

        "Trade" {
            player.dialogue(npc) {
                npc("You can only trade with people in your group.")
            }
        }

    }
}
