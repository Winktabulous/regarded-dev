package com.near_reality.game.content.dt2.plugins.rings

import com.zenyte.game.item.Item
import com.zenyte.game.item.ItemId.*
import com.zenyte.game.model.item.ItemOnObjectAction
import com.zenyte.game.world.entity.player.Player
import com.zenyte.game.world.entity.player.Skills
import com.zenyte.game.world.entity.player.dialogue.dialogue
import com.zenyte.game.world.entity.player.dialogue.options
import com.zenyte.game.world.`object`.WorldObject

class IconOnFurnaceAction : ItemOnObjectAction {

    override fun handleItemOnObjectAction(player: Player, item: Item, slot: Int, `object`: WorldObject?) {
        val ingots = Item(CHROMIUM_INGOT, 3)
        val icon = when(item.id) {
            MAGUS_ICON -> Item(MAGUS_ICON)
            BELLATOR_ICON -> Item(BELLATOR_ICON)
            ULTOR_ICON -> Item(ULTOR_ICON)
            VENATOR_ICON -> Item(VENATOR_ICON)
            else -> null
        }
        val resultingRing = when(item.id) {
            MAGUS_ICON -> Item(MAGUS_RING_28313)
            BELLATOR_ICON -> Item(BELLATOR_RING_28316)
            ULTOR_ICON -> Item(ULTOR_RING_28307)
            VENATOR_ICON -> Item(VENATOR_RING_28310)
            else -> null
        }
        val ringMould = Item(RING_MOULD)
        if (icon == null || resultingRing == null)
            return;

        if (!player.inventory.containsItem(ringMould)) {
            player.dialogue { plain("You need a ring mould to forge this.") }
        }
        else if (player.inventory.containsItems(icon, ingots)) {
            if (player.skills.getLevel(Skills.MAGIC) < 90 || player.skills.getLevel(Skills.CRAFTING) < 80)
                player.dialogue { plain("You lack the required skills (90 magic, 80 crafting)") }
            else {
                player.dialogue {
                    options("Do you want to create the ${icon.name}?") {
                        "Yes" {
                            player.inventory.deleteItemsIfContains(arrayOf(icon, ingots)) {
                                player.inventory.addOrDrop(resultingRing)
                                player.skills.addXp(Skills.CRAFTING, 2500.00)
                                plain("You create the ${resultingRing.name}.")
                            }
                        }
                        "No" { finish() }
                    }
                }
            }
        }
        else player.dialogue { plain("Please come back with three chromium ingots to forge this") }

    }

    override fun getItems() = arrayOf(MAGUS_ICON, BELLATOR_ICON, ULTOR_ICON, VENATOR_ICON)

    override fun getObjects() = arrayOf(16469)
}