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
class DerseVenator : NPCPlugin(), ItemOnNPCAction {

    override fun handle() {
        bind("Talk-to") { player, npc ->
            if (player.inventory.containsItem(ItemId.CRAWS_BOW_U) || player.inventory.containsItem(ItemId.CRAWS_BOW)) {
                val chainmace = if (player.inventory.containsItem(ItemId.CRAWS_BOW_U)) ItemId.CRAWS_BOW_U else ItemId.CRAWS_BOW
                if (player.inventory.containsItem(27670)) {
                    player.dialogue(npc) {
                        player("Hey. Do you know anything about this?")
                        item(Item(chainmace), "You show the bow to Derse Venator.")
                        npc("Ah, a fellow marksman. That's a unique bow you have there.")
                        player("Do you know anything about it?")
                        npc("I've done my fair bit of fletching in my day. I'd say this bow can be improved upon.")
                        npc("See those etches on it? You could put something sharp in there to improve the bow's strength.")
                        player("Perhaps I could use this?")
                        item(Item(27670), "You show the Fangs of venenatis to Derse Venator.")
                        npc("Yes. Those look like they could be attached to the weapon.")
                        player("Could you combine them?")
                        npc("Sure I can. For the right price.")
                        player("Sigh. How much?")
                        npc("500,000 coins would do nicely.")
                        options("Pay 500,000 coins to combine the fangs and bow?") {
                            "Yes" {
                                if (player.inventory.getAmountOf(ItemId.COINS_995) >= 500_000) {
                                    assembleBow(player, npc)
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
                        item(Item(chainmace), "You show the bow to Derse Venator.")
                        npc("Ah, a fellow marksman. That's a unique bow you have there.")
                        player("Do you know anything about it?")
                        npc("I've done my fair bit of fletching in my day. I'd say this bow can be improved upon.")
                        npc("See those etches on it? You could put something sharp in there to improve the bow's strength.")
                        player("Interesting! I'll be on the lookout for a powerful enhancement like that.")
                    }
                }
            } else if (player.inventory.containsItem(27670)) {
                player.dialogue(npc) {
                    player("Hey. Do you know anything about this?")
                    item(Item(27670), "You show the Fangs of venenatis to Derse Venator.")
                    npc("Those look like strong fangs. You could probably attach them to a bow to make it stronger.")
                    player("Interesting! I'll be on the lookout for a suitable bow.")
                }
            } else {
                player.dialogue(npc) {
                    player("What brings you to this place? I can't imagine anyone coming here by choice.")
                    npc("You're right, I did not come here by choice. I came here for vengeance.")
                    player("Vengeance? Against whom? Did someone take your gardening equipment?")
                    npc("My mission is not against another man and I will not have it mocked.")
                    player("Okay, sorry, I was just trying to lighten the mood. Any other reason you're here?")
                    npc("Sometimes I'll help adventurers create rare items, depending on what they bring me. Come back with something interesting and I'd be happy to take a look.")
                    player("Will do, thanks!")
                }
            }
        }
    }

    private fun assembleBow(player: Player, derse: NPC) {
        if (!player.inventory.containsItem(ItemId.CRAWS_BOW_U) && !player.inventory.containsItem(ItemId.CRAWS_BOW)) {
            return
        }

        if (!player.inventory.containsItem(27667)) {
            return
        }

        if (player.inventory.getAmountOf(ItemId.COINS_995) < 500_000) {
            return
        }

        player.inventory.deleteItem(Item(ItemId.COINS_995, 500_000))
        player.inventory.deleteItem(Item(27670))

        val craws = Item(if (player.inventory.containsItem(ItemId.CRAWS_BOW_U)) ItemId.CRAWS_BOW_U else ItemId.CRAWS_BOW)
        player.inventory.deleteItem(craws)

        val webweaver = Item(if (craws.id == ItemId.CRAWS_BOW_U) 27657 else 27660)
        webweaver.charges = craws.charges
        player.inventory.addItem(webweaver)

        player.dialogue(derse) {
            item(webweaver, "Derse Venator combines the items and gives you the ${webweaver.name}.")
            npc("There we go! All done.")
        }
    }


    override fun getNPCs() = intArrayOf(NpcId.DERSE_VENATOR)

    override fun handleItemOnNPCAction(player: Player, item: Item, slot: Int, npc: NPC) {
        player.dialogue(npc) {
            player("Hey. Do you know anything about this?")
            if (item.id == 27667) {
                item(item, "You show the claws to Derse Venator.")
                npc("Are those... Bear claws?")
                player("Yeah. I got them from a really big bear.")
                npc("By the gods... This bear must be even bigger than I imagined.")
                player("So do you know if the claws would be useful at all?")
                npc("I... have a lot to think about. Perhaps Andros Mai over there can help you while I think")
            } else if (item.id == 27673) {
                item(item, "You show the skull to Androis Mai.")
                npc("That looks like any skull to me. Who's is it?")
                player("It's mine. I picked it up.")
                npc("I meant... Sigh. Never mind. What do you want with the skull?")
                player("I'm trying to find out if it's useful.")
                npc("Don't know much about skulls, myself. That mage over there might know.")
                npc("She's into some weird stuff, that one. Think she said her name was Phabelle Bile.")
            } else {
                item(item, "You show the ${item.name} to Derse Venator.")
                npc("No, I'm afraid not.")
                player("Oh. Do you know someone who might know more?")
                npc("Just earlier I saw this lady run down into the basement of this pub.")
                npc("I don't know who she is, but she looks like a strange one.")
                npc("Strange attracts strange, so I reckon she might know about your strange weapon piece.")
            }
            player("Thanks!")
        }
    }

    override fun getItems() = arrayOf(27667, 27673, 27681, 27684, 27687)

    override fun getObjects() = arrayOf(NpcId.DERSE_VENATOR)

}