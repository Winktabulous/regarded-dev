package com.near_reality.plugins.item

import com.zenyte.game.item.Item
import com.zenyte.game.item.ItemId
import com.zenyte.game.model.item.ItemOnObjectAction
import com.zenyte.game.world.entity.npc.NpcId
import com.zenyte.game.world.entity.player.Player
import com.zenyte.game.world.entity.player.dialogue.dialogue
import com.zenyte.game.world.entity.player.dialogue.options
import com.zenyte.game.world.`object`.WorldObject

/**
 * Handles the creation of a Staff of Balance by using a Guthixian icon and Staff of the Dead on Juna.
 *
 * @author Stan van der Bend.
 */
@Suppress("UNUSED")
class StaffOfBalanceCreation : ItemOnObjectAction {

    override fun getItems() = arrayOf(ItemId.GUTHIXIAN_ICON, ItemId.STAFF_OF_THE_DEAD)

    override fun getObjects() = arrayOf("Juna")

    override fun handleItemOnObjectAction(player: Player, item: Item, slot: Int, `object`: WorldObject?) {
        player.dialogue(NpcId.JUNA) {
            when {
                // Using a staff of the dead on Juna without having a Guthixian icon
                !player.hasInInventory(ItemId.GUTHIXIAN_ICON) -> {
                    npc("A Staff of the Dead! I would happily honour the dead, " +
                            "and all the stories they must have known, " +
                            "by combining your staff with an icon of my master, Guthix.")
                    npc("Perhaps you can find such an icon and bring it to me.")
                }
                // Using a Guthixian icon on Juna without having a staff of the dead
                !player.hasInInventory(ItemId.STAFF_OF_THE_DEAD) && !player.hasEquipped(ItemId.STAFF_OF_THE_DEAD) -> {
                    player("Can you do something with this icon?")
                    plain("Juna examines your Guthixian icon.")
                    npc("So many aeons have passed since these were made. " +
                            "So many mortals have been born, lived their lives, then died as Balance requires. " +
                            "What stories they must have known!")
                    npc("I would happily honour the dead by combining such an icon with a Staff of the Dead, " +
                            "if you bring one to me.")
                    when {
                        // Using a Guthixian icon on Juna while wielding a staff of light
                        player.hasEquipped(ItemId.STAFF_OF_LIGHT) -> {
                            player("My weapon is sort of like a Staff of the Dead, don't you think?")
                            npc("I cannot combine it with that.")
                        }
                        // Using a Guthixian icon on Juna while wielding a staff of balance
                        player.hasEquipped(ItemId.STAFF_OF_BALANCE) -> {
                            player("My weapon is sort of like a Staff of the Dead, don't you think?")
                            npc("It was, but I appear to have combined it with an icon already.")
                        }
                        else ->
                            player("A Staff of the Dead, you say? Okay, thanks.")
                    }
                }
                // Using a Guthixian icon or a staff of the dead on Juna while having both
                else -> {
                    npc("I would happily honour the dead, and all the stories they must have known, " +
                            "by combining your Staff of the Dead with your icon of my master, Guthix. " +
                            "But this cannot be undone; they will be bound forever.")
                    options {
                        "Combine them. I understand it can't be undone." {
                            player.dialogue {
                                player("Combine them. I understand it can't be undone.").executeAction {
                                    if (player.hasInInventory(ItemId.GUTHIXIAN_ICON)) {
                                        val inventory = when {
                                            player.hasInInventory(ItemId.STAFF_OF_THE_DEAD) -> player.inventory
                                            player.hasEquipped(ItemId.STAFF_OF_THE_DEAD) -> player.equipment
                                            else -> return@executeAction
                                        }
                                        if (!player.inventory.deleteItem(ItemId.GUTHIXIAN_ICON, 1).isFailure) {
                                            val slotId = inventory.container.getSlotOf(ItemId.STAFF_OF_THE_DEAD)
                                            assert(slotId >= 0)
                                            inventory.replaceItem(ItemId.STAFF_OF_BALANCE, 1, slotId)
                                        }
                                    }
                                }
                                plain("Juna combines your icon and staff to make a staff of balance.")
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
        }
    }

    private fun Player.hasInInventory(itemId: Int) =
        inventory.containsItem(itemId)

    private fun Player.hasEquipped(itemId: Int) =
        equipment.containsItem(itemId)
}
