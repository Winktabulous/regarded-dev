package com.near_reality.game.content.elven.npc.dialogue

import com.zenyte.game.world.entity.npc.NPC
import com.zenyte.game.world.entity.player.Player
import com.zenyte.game.world.entity.player.dialogue.Dialogue
import com.zenyte.game.world.entity.player.dialogue.dialogue
import com.zenyte.game.world.entity.player.dialogue.options

/**
 * Represents the [Dialogue] for Caerwyn.
 */
class CaerwynDialogue(player: Player, npc: NPC) : Dialogue(player, npc) {
    override fun buildDialogue() {
        val gender = player.appearance.gender
        npc("Are you interested in buying or selling spice?")
        options{
            "Yes please." {
                player.dialogue {
                    player("Yes please.").executeAction {
                        // TODO: open shop
                    }
                }
            }
            "No thanks." {
                player.dialogue {
                    player("No thanks.")
                }
            }
        }
    }
}
