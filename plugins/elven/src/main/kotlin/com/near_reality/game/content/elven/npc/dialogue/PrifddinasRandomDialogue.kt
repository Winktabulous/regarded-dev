package com.near_reality.game.content.elven.npc.dialogue

import com.zenyte.game.util.Utils
import com.zenyte.game.world.entity.npc.NPC
import com.zenyte.game.world.entity.player.Player
import com.zenyte.game.world.entity.player.dialogue.Dialogue
import com.zenyte.game.world.entity.player.dialogue.dialogue
import com.zenyte.game.world.entity.player.dialogue.options

/**
 * Represents the [Dialogue] for Mithrellas.
 */
class PrifddinasRandomDialogue(player: Player, npc: NPC) : Dialogue(player, npc) {

    override fun buildDialogue() {
        player("Hi.")
        when (Utils.random(4)) {
            1 -> {
                npc("You know, there are so many different ways to greet people. Hello, 'ello, hello there...")
                player("That still only counts as one!")
            }
            2 -> {
                npc("Hello. How are you today?")
                player("I'm great, thank you!")
            }
            3 -> {
                npc("Hello! What a day it is in the crystal city, huh?")
                player("Indeed.")
            }
            4 -> {
                npc("What can I do for ya?")
                options {
                    "What is there to do around here?" {
                        player.dialogue(npc) {
                            player("What is there to do around here?")
                            when (Utils.random(2)) {
                                1 -> npc("All kinds of things! The towers are a wonderful sight to see.")
                                2 -> npc("On a day like today, I'd recommend visiting the market.")
                            }
                        }
                    }
                    "Nothing." {
                        player.dialogue {
                            player("Nothing.")
                        }
                    }
                }
            }
        }
    }
}
