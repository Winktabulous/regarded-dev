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
class PhabelleBile : NPCPlugin(), ItemOnNPCAction {

    override fun handle() {
        bind("Talk-to") { player, npc ->
            if (player.inventory.containsItem(ItemId.THAMMARONS_SCEPTRE_U) || player.inventory.containsItem(ItemId.THAMMARONS_SCEPTRE)) {
                val chainmace = if (player.inventory.containsItem(ItemId.THAMMARONS_SCEPTRE_U)) ItemId.THAMMARONS_SCEPTRE_U else ItemId.THAMMARONS_SCEPTRE
                if (player.inventory.containsItem(27673)) {
                    player.dialogue(npc) {
                        player("Hey. Do you know anything about this?")
                        item(Item(chainmace), "You show the sceptre to Phabelle Bile.")
                        npc("Well, would you look at that. Most fascinating.")
                        player("Could you tell me what's so fascinating?")
                        npc("It's a rare Zamorakian artifact. I haven't seen any quite like this one before.")
                        npc("If you look closely, you can see that it has a socket where you can place an item to increase its power.")
                        player("Oh, like this skull?")
                        item(Item(27673), "You show the skull to Phabelle Bile.")
                        npc("Yes, like that one! That looks powerful indeed. I think that would do quite nicely.")
                        npc("A skilled crafter can combine the two. If you wish, I could do it for you... For a price, that is.")
                        options("Pay 500,000 coins to combine the skull and sceptre?") {
                            "Yes" {
                                if (player.inventory.getAmountOf(ItemId.COINS_995) >= 500_000) {
                                    assembleSceptre(player, npc)
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
                        item(Item(chainmace), "You show the sceptre to Phabelle Bile.")
                        npc("Well, would you look at that. Most fascinating.")
                        player("Could you tell me what's so fascinating?")
                        npc("It's a rare Zamorakian artifact. I haven't seen any quite like this one before.")
                        npc("If you look closely, you can see that it has a socket where you can place an item to increase its power.")
                        player("Interesting! I'll be on the lookout for that.")
                    }
                }
            } else if (player.inventory.containsItem(27673)) {
                player.dialogue(npc) {
                    player("Hey. Do you know anything about this?")
                    item(Item(27673), "You show the skull to Phabelle Bile.")
                    npc("So it's real. Magnificent.")
                    player("You've seen this before?")
                    npc("No, but I have heard tales of the mighty Vet'Ion.")
                    npc("It is said his skull can power a certain Zamorakian weapon.")
                    player("Interesting! I'll be on the lookout for that.")
                }
            } else {
                player.dialogue(npc) {
                    player("Who are you?")
                    npc("I'm out here to conduct research, I've been told a giant spider lurks in the heart of the Wilderness.")
                    npc("Her venom is priceless and it would aid me greatly in my research.")
                    player("Interesting... best of luck with that!")
                    npc("Thanks, I'll be here if you need me.")
                }
            }
        }
    }

    private fun assembleSceptre(player: Player, phabelle: NPC) {
        if (!player.inventory.containsItem(ItemId.THAMMARONS_SCEPTRE_U) && !player.inventory.containsItem(ItemId.THAMMARONS_SCEPTRE)) {
            return
        }

        if (!player.inventory.containsItem(27673)) {
            return
        }

        if (player.inventory.getAmountOf(ItemId.COINS_995) < 500_000) {
            return
        }

        player.inventory.deleteItem(Item(ItemId.COINS_995, 500_000))
        player.inventory.deleteItem(Item(27673))

        val thammarons = Item(if (player.inventory.containsItem(ItemId.THAMMARONS_SCEPTRE_U)) ItemId.THAMMARONS_SCEPTRE_U else ItemId.THAMMARONS_SCEPTRE)
        player.inventory.deleteItem(thammarons)

        val accursed = Item(if (thammarons.id == ItemId.THAMMARONS_SCEPTRE_U) 27662 else 27665)
        accursed.charges = thammarons.charges
        player.inventory.addItem(accursed)

        player.dialogue(phabelle) {
            item(accursed, "Phabelle Bile combines the items and gives you the ${accursed.name}.")
            npc("There we go! All done.")
        }
    }

    override fun getNPCs() = intArrayOf(NpcId.PHABELLE_BILE)

    override fun handleItemOnNPCAction(player: Player, item: Item, slot: Int, npc: NPC) {
        player.dialogue(npc) {
            player("Hey. Do you know anything about this?")
            if (item.id == 27667) {
                item(Item(27667), "You show the claws to Phabelle Bile.")
                npc("No, I'm afraid not.")
                player("Oh. Do you know someone who might know more?")
                npc("Andros Mai is a very... Devoted man. If you can see past that, he may be able to help you.")
            } else if (item.id == 27670) {
                item(Item(27670), "You show the fangs to Phabelle Bile.")
                npc("Yes! Those belong to Venenatis!")
                npc("Ah, but they contain no more venom. That is disappointing.")
                player("So you're saying they're useless?")
                npc("To me? Yes. But maybe not to you.")
                npc("I've seen that Derse Venator guy over there handle similar items. Perhaps he knows more.")
            } else {
                item(item, "You show the ${item.name} to Phabelle Bile.")
                npc("No, I'm afraid not.")
                player("Oh. Do you know someone who might know more?")
                npc("Downstairs there's a strange looking person. I don't know what that item is, but if anyone would, I reckon she would.")
                npc("I think she said her name was Madam Sikaro.")
            }
            player("Thanks!")
        }
    }

    override fun getItems() = arrayOf(27667, 27670, 27681, 27684, 27687)

    override fun getObjects() = arrayOf(NpcId.PHABELLE_BILE)

}