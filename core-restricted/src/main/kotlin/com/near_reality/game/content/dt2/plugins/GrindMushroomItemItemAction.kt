package com.near_reality.game.content.dt2.plugins

import com.zenyte.game.item.Item
import com.zenyte.game.item.ItemId
import com.zenyte.game.model.item.ItemOnItemAction
import com.zenyte.game.model.item.ItemOnItemAction.ItemPair
import com.zenyte.game.world.entity.masks.Animation
import com.zenyte.game.world.entity.player.Player
import com.zenyte.game.world.entity.player.Skills
import kotlin.math.floor

@Suppress("unused")
class GrindMushroomItemItemAction : ItemOnItemAction {
    override fun handleItemOnItemAction(player: Player, from: Item, to: Item, fromSlot: Int, toSlot: Int) {
        val mushroom = if(from.isMushroom()) from else to
        player.grindMushroom(mushroom.id)
    }

    override fun getItems(): IntArray? {
        return null
    }

    override fun getMatchingPairs(): Array<ItemOnItemAction.ItemPair> {
        return arrayOf(ItemPair(ItemId.PESTLE_AND_MORTAR, 28341), ItemPair(ItemId.PESTLE_AND_MORTAR, 28345))
    }

    private fun Item.isMushroom() : Boolean {
        return this.id == 28341 || this.id == 28345
    }

    private fun Player.grindMushroom(id: Int) {
        if (inventory.deleteItem(id, 1).succeededAmount == 0) {
            return
        }

        animation = Animation(5249)

        val lvl = skills.getLevel(Skills.HERBLORE)
        val yield = floor((lvl - 20) / 10.0).toInt().coerceIn(1, 6)
        when (id) {
            28341 -> {
                sendMessage("You crush the musca mushroom into some powder.")
                inventory.addItem(28342, yield)
            }

            28345 -> {
                sendMessage("You crush the arder mushroom into some powder.")
                inventory.addItem(28346, yield)
            }
        }
    }
}

