package com.near_reality.plugins.area.ferox_enclave

import com.zenyte.game.item.Item
import com.zenyte.game.item.ItemId
import com.zenyte.game.model.item.ItemOnNPCAction
import com.zenyte.game.world.entity.npc.NPC
import com.zenyte.game.world.entity.npc.NpcId
import com.zenyte.game.world.entity.npc.actions.NPCPlugin
import com.zenyte.game.world.entity.player.Player
import com.zenyte.game.world.entity.player.dialogue.Expression
import com.zenyte.game.world.entity.player.dialogue.dialogue
import com.zenyte.game.world.entity.player.dialogue.options

/**
 * @author Andys1814
 */
@Suppress("unused")
class AndrosMai : NPCPlugin(), ItemOnNPCAction {

    override fun handle() {
        bind("Talk-to") { player, npc ->
            if (player.inventory.containsItem(ItemId.VIGGORAS_CHAINMACE) || player.inventory.containsItem(ItemId.VIGGORAS_CHAINMACE_U)) {
                val chainmace = if (player.inventory.containsItem(ItemId.VIGGORAS_CHAINMACE_U)) ItemId.VIGGORAS_CHAINMACE_U else ItemId.VIGGORAS_CHAINMACE
                if (player.inventory.containsItem(27667)) {
                    player.dialogue(npc) {
                        player("Hey. Do you know anything about this?")
                        item(Item(chainmace), "You show the chainmace to Andros Mai.")
                        npc("By Saradomin! That's a Zamorakian made chainmace you have there!")
                        player("And? Do you know anything about it?")
                        npc("I know melee weapons in general. Well enough to know most of them can be upgraded.")
                        npc("Perhaps with something sharp to give it more impact as it swings around.")
                        player("Perhaps I could use this?")
                        item(Item(27667), "You show the Claws of callisto to Andros Mai.")
                        npc("Yes. Those look like they could be attached to the weapon.")
                        player("Could you combine them?")
                        npc("While I could, I'd really not like to.")
                        player("Surely you could consider it... For the right price?")
                        npc("...")
                        npc("Money has been tight lately...")
                        npc("And my supplies are running low...")
                        npc("Fine. But I want 500,000 coins for that. No less.")
                        options("Pay 500,000 coins to combine the claws and chainmace?") {
                            "Yes" {
                                if (player.inventory.getAmountOf(ItemId.COINS_995) >= 500_000) {
                                    assembleChainmace(player, npc)
                                } else {
                                    player.dialogue(npc) {
                                        player("I don't have the money for that.")
                                        npc("Feel free to come back when you do.")
                                    }
                                }
                            }
                            "No" {
                                player.dialogue {
                                    player("No thanks.")
                                }
                            }
                        }
                    }
                } else {
                    player.dialogue(npc) {
                        player("Hey. Do you know anything about this?")
                        item(Item(chainmace), "You show the chainmace to Andros Mai.")
                        npc("By Saradomin! That's a Zamorakian made chainmace you have there!")
                        player("And? Do you know anything about it?")
                        npc("I know melee weapons in general. Well enough to know most of them can be upgraded.")
                        npc("Perhaps with something sharp to give it more impact as it swings around.")
                        player("Interesting! I'll be on the lookout for a powerful enhancement like that.")
                    }
                }
            } else if (player.inventory.containsItem(27667)) {
                player.dialogue(npc) {
                    player("Hey. Do you know anything about this?")
                    item(Item(27667), "You show the claws to Androis Mai.")
                    npc("They look like powerful claws. You could probably attach them to a weapon to make the weapon stronger.")
                    player("Interesting! I'll be on the lookout for that.")
                }
            } else {
                player.dialogue(npc) {
                    player("Who are you?")
                    npc("Who am I?", Expression.ANNOYED)
                    npc("I am Andros Mai, brother of the Holy Order of Saradomin. You would do well to greet me with respect.")
                    player("Andros Mai? Sorry, but I can't say I've heard of you I'm afraid.")
                    npc("I wouldn't have expected someone of your... stature to know of me.")
                    npc("How about this... come back when you have a reason to speak to me.")
                }
            }
        }
    }

    private fun assembleChainmace(player: Player, andros: NPC) {
        if (!player.inventory.containsItem(ItemId.VIGGORAS_CHAINMACE) && !player.inventory.containsItem(ItemId.VIGGORAS_CHAINMACE_U)) {
            return
        }

        if (!player.inventory.containsItem(27667)) {
            return
        }

        if (player.inventory.getAmountOf(ItemId.COINS_995) < 500_000) {
            return
        }

        player.inventory.deleteItem(Item(ItemId.COINS_995, 500_000))
        player.inventory.deleteItem(Item(27667))

        val viggoras = Item(if (player.inventory.containsItem(ItemId.VIGGORAS_CHAINMACE_U)) ItemId.VIGGORAS_CHAINMACE_U else ItemId.VIGGORAS_CHAINMACE)
        player.inventory.deleteItem(viggoras)

        val ursine = Item(if (viggoras.id == ItemId.VIGGORAS_CHAINMACE_U) 27657 else 27660)
        ursine.charges = viggoras.charges
        player.inventory.addItem(ursine)

        player.dialogue(andros) {
            item(ursine, "Andros Mai combines the items and gives you the ${ursine.name}.")
            npc("There we go! All done.")
        }
    }


    override fun getNPCs() = intArrayOf(NpcId.ANDROS_MAI)

    override fun handleItemOnNPCAction(player: Player, item: Item, slot: Int, npc: NPC) {
        player.dialogue(npc) {
            player("Hey. Do you know anything about this?")
            if (item.id == 27670) {
                item(item, "You show the fangs to Androis Mai.")
                npc("No. I'm afraid not. They just look like fangs.")
                player("Fangs indeed. Rather useless it seems.")
                npc("Perhaps not. I've seen that Derse Venator guy over there handle similar items. Perhaps he knows more.")
            } else if (item.id == 27673) {
                item(item, "You show the skull to Androis Mai.")
                npc("That's him! The vile Vet'Ion. I should like to smash this skull.")
                player("Wait! It seems to still contain some magic. Surely we can use it?")
                npc("The only thing his skull would be useful for is Zamorakian heresy. I won't stand for it!")
                player("Is there really nothing you can do to help me with this?")
                npc("Do you see Phabelle over there? Despite her questionable morals, she's my friend.")
                npc("She's good with magic and may know how to take advantage of the powers in that skull.")
            } else {
                item(item, "You show the ${item.name} to Androis Mai.")
                npc("No, I'm afraid not.")
                player("Oh. Do you know someone who might know more?")
                npc("Downstairs there's a strange looking person. I don't know what that item is, but if anyone would, I reckon she would.")
                npc("I think she said her name was Madam Sikaro.")
            }
            player("Thanks!")
        }
    }

    override fun getItems() = arrayOf(27670, 27673, 27681, 27684, 27687)

    override fun getObjects() = arrayOf(NpcId.ANDROS_MAI)

}