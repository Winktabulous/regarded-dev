package com.near_reality.plugins.area.osrs_home.npc

import com.zenyte.game.world.entity.npc.NPC
import com.zenyte.game.world.entity.player.Player
import com.zenyte.game.world.entity.player.dialogue.Dialogue
import com.zenyte.game.world.entity.player.dialogue.options

/**
 * Basic dialogue for monks at home area.
 *
 * @author Stan van der Bend
 */
class MonkDialogue(player: Player, npc: NPC) : Dialogue(player, npc) {
    override fun buildDialogue() {
        npc("Greetings traveller.")
        options {
            "Can you heal me? I'm injured." {
                player("Can you heal me? I'm injured.")
                npc("Ok.").executeAction {
                    player.heal(2 + player.hitpoints.times(0.2).toInt())
                }
            }
             "Isn't this place built a bit out of the way?" {
                player("Isn't this place built a bit out of the way?")
                npc("We like it that way actually! We get disturbed less. " +
                        "We still get rather a large amount of travellers looking for sanctuary and healing here as it is!")
            }
            "How do I get further into the monastery?" {
                player("How do I get further into the monastery?")
                npc("You'll need to talk to Abbot Langley about that. " +
                        "He's usually to be found walking the halls of the monastery.")
            }
        }
    }
}
