package com.near_reality.game.content.elven.npc.dialogue

import com.zenyte.game.world.entity.npc.NPC
import com.zenyte.game.world.entity.player.Player
import com.zenyte.game.world.entity.player.dialogue.Dialogue
import com.zenyte.game.world.entity.player.dialogue.dialogue
import com.zenyte.game.world.entity.player.dialogue.options

/**
 * Represents the [Dialogue] for Nia.
 */
class NiaDialogue(player: Player, npc: NPC) : Dialogue(player, npc) {
    override fun buildDialogue() {
        val gender = player.appearance.gender
        npc("You look like a $gender who knows how to dress up! How can I help ya?")
        options {
            "Can I take a look at your stocks?" {
                player.dialogue {
                    player("Can I take a look at your stocks?").executeAction {
                        // TODO: open shop
                    }
                }
            }
            "Do you make any custom orders?" {
                player.dialogue {
                    player("Do you make any custom orders?")
                    npc("I do! I'm always keeping an eye out for interesting materials to work with.")
                    options {
                        "Could you make something for me?" {
                            player.dialogue {
                                player("Could you make something for me?")
                                // TODO: open custom fur clothing shop
                            }
                        }
                        "Maybe later." {
                            player.dialogue {
                                player("Maybe later.")
                            }
                        }
                    }
                }
            }
            "I think I'm okay." {
                player.dialogue {
                    player("I think I'm okay.")
                }
            }
        }
    }
}
