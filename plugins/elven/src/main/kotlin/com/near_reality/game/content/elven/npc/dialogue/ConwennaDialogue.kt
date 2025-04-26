package com.near_reality.game.content.elven.npc.dialogue

import com.zenyte.game.world.entity.npc.NPC
import com.zenyte.game.world.entity.player.Player
import com.zenyte.game.world.entity.player.dialogue.Dialogue
import com.zenyte.game.world.entity.player.dialogue.dialogue
import com.zenyte.game.world.entity.player.dialogue.options

/**
 * Represents the [Dialogue] for Cowenna, a NPC found near the singing bowl in Prifddinas.
 *
 * @author Stan van der Bend
 */
class ConwennaDialogue(player: Player, npc: NPC) : Dialogue(player, npc) {
    override fun buildDialogue() {
        player("Hello.")
        npc("Welcome, traveller.")
        options {
            "Could you tell me about crystal singing?" {
                player.dialogue {
                    player("Could you tell me about crystal singing?")
                    npc("Of course! We elves learned the art long ago, allowing" +
                            "us to form crystals in any way we would like, using" +
                            "just our voices.")
                    npc("It's allowed us to make weapons, armour and tools" +
                            "using the finest crystals.")
                    player("Wow. Is there any way I could do it?")
                    npc("You might be able to with the help of the crystal bowl" +
                            "over there. As long as you've got the right knowledge of" +
                            "what you're trying to craft.")
                    player("Thanks!")
                }
            }
            "Could you sing a song for me?" {
                player.dialogue {
                    player("Could you sing a song for me?")
                    plain("Conwenna sings you a beautiful song of the elves.")
                    player("Wow! Thank you.")
                }
            }
        }
    }
}
