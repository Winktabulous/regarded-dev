package com.near_reality.plugins.area.ferox_enclave

import com.zenyte.game.item.Item
import com.zenyte.game.item.ItemId
import com.zenyte.game.world.entity.npc.NpcId
import com.zenyte.game.world.entity.npc.actions.NPCPlugin
import com.zenyte.game.world.entity.player.Player
import com.zenyte.game.world.entity.player.dialogue.dialogue
import com.zenyte.game.world.entity.player.dialogue.options

/**
 * @author Andys1814
 */
@Suppress("unused")
class MadamSikaro : NPCPlugin() {

    private companion object {

        private val VOIDWAKER_COMPONENTS = mutableListOf(
            Item(ItemId.VOIDWAKER_HILT),
            Item(ItemId.VOIDWAKER_BLADE),
            Item(ItemId.VOIDWAKER_GEM)
        )

        private const val ASSEMBLED_VOIDWAKER_ATTR = "assembled_voidwaker"

    }

    override fun handle() {
        bind("Talk-to") { player, npc ->
            player.dialogue(npc) {
                options {
                    "Who are you?" {
                        player.dialogue(npc) {
                            player("Who are you?")
                            npc("I'm Madam Sikaro.")
                            player("...")
                            npc("...")
                            player("...Is that it?")
                            npc("Why shouldn't it be? You asked who I was, and I answered.")
                            player("I was hoping to learn something about you, like what you were doing here...!")
                        }
                    }
                    "What are you doing here?" {
                        player.dialogue(npc) {
                            player("What are you doing here?")
                            npc("Making a wizard hideout.")
                            player("A wizard... hideout?")
                            npc("A wizard hideout! You know how some wizards have their own towers? Think that but underground.")
                            player("So why not just get a tower?")
                            npc("Land price. Kingdoms want you to pay an extortionate amount of money to build on 'their' land, it turns out.")
                            player("So instead you ended up in the basement of a tavern.")
                            npc("Yep. Camarst said I could have it as long as I don't hurt any of his customers too badly.")
                            npc("Not that I actually plan to hurt any of his customers.")
                        }
                    }
                    if (player.inventory.containsItems(VOIDWAKER_COMPONENTS)) {
                        player.dialogue(npc) {
                            player("I have these parts of a weapon, do you think you could assemble it for me?")
                            npc("Hmm... yes, these are the parts of a particularly powerful weapon indeed. I can reassemble it... for a fee, of course. 500,000 coins, no less.")
                            if (player.inventory.getAmountOf(ItemId.COINS_995) < 500_000) {
                                player("I don't have that much on me...")
                                npc("Well, if you come across some more, come find me again.")
                            } else {
                                options("Have Madam Sikaro assemble the Voidwaker for 500,000 coins?") {
                                    "Yes" {
                                        assembleVoidmaker(player)
                                    }
                                    "No" {
                                        player("Actually, I've changed my mind.")
                                        npc("Curious, you come to me with all of the parts and the coin, yet you change your mind at the last second.")
                                        npc("Do talk to me again if you'd like to proceed more sensibly!")
                                    }
                                }
                            }
                        }
                    } else if (player.inventory.containsAnyOf(*VOIDWAKER_COMPONENTS.toTypedArray())) {
                        "I have this part that looks like it's from a weapon..." {
                            player.dialogue(npc) {
                                player("I have this part that looks like it's from a weapon...")
                                npc("Ah yes, that does look like a part of a particularly powerful weapon.")
                                player("Can I do anything with it?")
                                npc("You? Perhaps not, though if you could bring me all three pieces, I suspect I could put it back together for you.")
                                npc("There should be a hilt, a blade and a conduit gemstone. Oh, and I'll want 500,000 coins as payment as well. Good hunting!")
                                player("Do you know where I could find the other parts?")
                                npc("I suspect the Wilderness would be a good place to start. There are monsters out there that have grown immense over time.")
                                npc("Seeking out the strongest of them is where I'd start, anyway.")
                            }
                        }
                    }
                    if (player.attributes.containsKey(ASSEMBLED_VOIDWAKER_ATTR)) {
                        "What are you doing with the money?" {
                            player.dialogue(npc) {
                                player("What are you doing with the money from combining the Voidwaker?")
                                npc("I'll spend some of it improving the hideout. The rest on general expenses.")
                                player("What kind of expenses?")
                                npc("Living isn't free, you know? A woman my age has expenses. Food, clothing, protection... It all adds up.")
                            }
                        }
                    }
                    "Goodbye." {
                        player.dialogue(npc) {
                            player("Goodbye.")
                            npc("You walked up to me just to say goodbye? Ok, sure, goodbye.")
                        }
                    }
                }
            }
        }
    }

    private fun assembleVoidmaker(player: Player) {
        if (!player.inventory.containsItems(VOIDWAKER_COMPONENTS)) {
            return
        }

        if (player.inventory.getAmountOf(ItemId.COINS_995) < 500_000) {
            return
        }

        player.inventory.deleteItems(*VOIDWAKER_COMPONENTS.toTypedArray())
        player.inventory.deleteItems(Item(ItemId.COINS_995, 500_000))
        player.inventory.addItem(Item(27690))

        player.dialogue {
            item(Item(27690), "Madam Sikaro takes the components off you and, in a flash of white light, fuses them together to form the Voidwaker.")
        }

        player.putBooleanAttribute(ASSEMBLED_VOIDWAKER_ATTR, true)
    }


    override fun getNPCs() = intArrayOf(NpcId.MADAM_SIKARO)

}