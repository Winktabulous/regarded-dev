package com.near_reality.game.content.boss.nex.item

import com.zenyte.game.content.skills.smithing.Smelting
import com.zenyte.game.item.Item
import com.zenyte.game.item.ItemId
import com.zenyte.game.model.item.ItemOnObjectAction
import com.zenyte.game.world.entity.player.Player
import com.zenyte.game.world.entity.player.container.RequestResult
import com.zenyte.game.world.entity.player.dialogue.dialogue
import com.zenyte.game.world.entity.player.dialogue.options
import com.zenyte.game.world.`object`.ObjectId
import com.zenyte.game.world.`object`.WorldObject

/**
 * Represents an [ItemOnObjectAction] that handles using bandos on the ancient forge.
 */
class BandosOnAncientForge : ItemOnObjectAction {

    override fun handleItemOnObjectAction(player: Player, item: Item, slot: Int, `object`: WorldObject) {

        val componentsAmount = componentsCount(item.id)
        val inventory = player.inventory
        if (inventory.freeSlots < componentsAmount){
            player.dialogue { plain("Not enough space in your inventory to melt down the armour.") }
            return
        }
        val componentsString = componentsString(componentsAmount)

        player.options("Melt down your ${item.name} into $componentsString?") {
            "Yes." {
                if (inventory.deleteItem(item).result == RequestResult.SUCCESS) {
                    val components = Item(ItemId.BANDOSIAN_COMPONENTS, componentsAmount)
                    inventory.addItem(components)
                    player.animation = Smelting.ANIMATION
                    player.dialogue {
                        item(components, "You use the forge to melt the armour down into $componentsString.")
                    }
                }
            }
            "No." {}
        }
    }

    override fun getItems() = arrayOf(
        ItemId.BANDOS_CHESTPLATE,
        ItemId.BANDOS_TASSETS,
    )

    override fun getObjects() = arrayOf(ObjectId.ANCIENT_FORGE_42966)

    private companion object {
        private fun componentsCount(unNotedItem: Int): Int = when (unNotedItem) {
            ItemId.BANDOS_CHESTPLATE -> 3
            ItemId.BANDOS_TASSETS -> 2
            else -> error("Unknown component")
        }
        private fun componentsString(amount: Int) = "$amount Bandosian component${if(amount > 1) "s" else ""}"
    }
}
