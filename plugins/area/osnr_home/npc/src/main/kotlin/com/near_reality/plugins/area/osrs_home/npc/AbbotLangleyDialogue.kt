package com.near_reality.plugins.area.osrs_home.npc

import com.zenyte.game.item.Item
import com.zenyte.game.item.ItemId
import com.zenyte.game.item.ItemId.*
import com.zenyte.game.world.entity.npc.NPC
import com.zenyte.game.world.entity.npc.NpcId
import com.zenyte.game.world.entity.player.Player
import com.zenyte.game.world.entity.player.SkillConstants
import com.zenyte.game.world.entity.player.dialogue.Dialogue
import com.zenyte.game.world.entity.player.dialogue.dialogue
import com.zenyte.game.world.entity.player.dialogue.options

/**
 * Represents the dialogue for Abbot Langley at home area.
 *
 * @author Stan van der Bend
 */
class AbbotLangleyDialogue(player: Player?, npc: NPC?) : Dialogue(player, npc) {
    override fun buildDialogue() {
        this.npcId = NpcId.ABBOT_LANGLEY
        player("Very nice rosebushes you have here.")
        npc("Yes, it has taken me many long hours in this garden to bring them to this state of near-perfection.")
        options {
            "Can you heal me? I'm injured." {
                player.dialogue(npc) {
                    player("Can you heal me? I'm injured.")
                    npc("Ok.")
                    plain("Abbot Langley places his hands on your head. You feel a little better.").executeAction {
                        player.heal(4 + player.hitpoints.times(0.12).toInt())
                    }
                }
            }
            "Isn't this place built a bit out of the way?" {
                player.dialogue(npc) {
                    player("Isn't this place built a bit out of the way?")
                    npc(
                        "We like it that way actually! We get disturbed less. " +
                                "We still get rather a large amount of travellers looking for sanctuary " +
                                "and healing here as it is!"
                    )
                }
            }
            "Can you help with spirit shields?" {
                player.dialogue(npc) {
                    player("Can you help with spirit shields?")
                    npc("I might be able to... What did you have in mind?")
                    player("Can you remove the sigils from my spirit shields?")
                    npc("I think I should be able to manage that. However in doing so, I'm afraid I'll have to destroy the blessed shield itself. You'll only get the sigil back.")
                    npc("I can go ahead and break down all of the shields in your inventory now if you'd like?")
                    plain("Are you sure?")
                    options {
                        "yes." {
                            if (player.inventory.containsAnyOf(ARCANE_SPIRIT_SHIELD, ELYSIAN_SPIRIT_SHIELD, SPECTRAL_SPIRIT_SHIELD)) {
                                player.dialogue(npc) {
                                    item(ARCANE_SIGIL,
                                        "Abbot Langley carefully breaks each shield, leaving the sigils perfectly intact."
                                    ).executeAction { extractSigilFromShield() }
                                }
                            }
                            else
                                player.dialogue(npc) { npc("You don't have any shields in your inventory.") }

                        }
                        "No." {
                            player.dialogue(npc) { player("Not right now.") }
                        }
                    }
                }
            }
            if (player.skills.getLevel(SkillConstants.PRAYER) < 31) {
                "How do I get further into the monastery?" {
                    player.dialogue {
                        player("How do I get further into the monastery?")
                        npc("I'm sorry but only members of our order are allowed in the second level of the monastery.")
                        player.options {
                            "Well can I join your order?" {
                                player.dialogue {
                                    player("Well can I join your order?")
                                    npc("No. I am sorry, but I feel you are not devout enough.")
                                }
                            }
                            "Oh, sorry." {
                                player.dialogue {
                                    player("Oh, sorry.")
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    private fun extractSigilFromShield() {
        val spiritShields = listOf(ARCANE_SPIRIT_SHIELD, ELYSIAN_SPIRIT_SHIELD, SPECTRAL_SPIRIT_SHIELD)
        val toBreakdown = player.inventory.container.itemsAsList
            .filter { it.id in spiritShields }
            .toMutableList()

        toBreakdown.forEach { item ->
            if (!player.inventory.deleteItem(item).isFailure) {
                val sigil = when (item.id) {
                    ARCANE_SPIRIT_SHIELD -> ARCANE_SIGIL
                    ELYSIAN_SPIRIT_SHIELD -> ELYSIAN_SIGIL
                    else -> SPECTRAL_SIGIL
                }
                player.inventory.addItem(Item(sigil, 1))
            }
        }

    }
}
