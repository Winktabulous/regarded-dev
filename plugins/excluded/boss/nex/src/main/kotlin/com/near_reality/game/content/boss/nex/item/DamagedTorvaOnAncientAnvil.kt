package com.near_reality.game.content.boss.nex.item

import com.zenyte.game.content.skills.smithing.Smithing
import com.zenyte.game.item.Item
import com.zenyte.game.item.ItemId
import com.zenyte.game.model.item.ItemOnObjectAction
import com.zenyte.game.world.entity.player.Action
import com.zenyte.game.world.entity.player.Player
import com.zenyte.game.world.entity.player.container.RequestResult
import com.zenyte.game.world.entity.player.dialogue.dialogue
import com.zenyte.game.world.entity.player.dialogue.options
import com.zenyte.game.world.`object`.ObjectId
import com.zenyte.game.world.`object`.WorldObject

/**
 * Represents an [ItemOnObjectAction] used for repairing damaged Torva by using it on an ancient anvil.
 */
@Suppress("unused")
class DamagedTorvaOnAncientAnvil : ItemOnObjectAction {

    override fun handleItemOnObjectAction(player: Player, item: Item, slot: Int, anvil: WorldObject) {
        val (fixed, cost) = item.fix()
        if (!player.inventory.containsItem(ItemId.HAMMER)) {
            player.dialogue { plain("You'll need a hammer to repair your ${fixed.name}.") }
            return
        }
        val costString = itemCostString(cost)
        val inventory = player.inventory
        player.options("Repair your ${fixed.name} with $costString?") {
            "Yes" {
                player.actionManager.action = object : Action() {
                    var tick = 0
                    override fun start() = true
                    override fun process() = true
                    override fun processWithDelay(): Int {
                        if (cost > inventory.getAmountOf(ItemId.BANDOSIAN_COMPONENTS)) {
                            player.sendMessage("You need $costString to fix this item")
                            return -1
                        }
                        when (tick++) {
                            1 -> {
                                player.faceObject(anvil)
                                player.animation = Smithing.ANIMATION
                            }

                            4 -> {
                                if (inventory.deleteItems(Item(ItemId.BANDOSIAN_COMPONENTS, cost), item).result == RequestResult.SUCCESS){
                                    inventory.addItem(fixed)
                                    player.dialogue {
                                        item(fixed,
                                            "You use $costString to repair your ${fixed.name}. " +
                                                    "You feel mentally and physically exhausted.")
                                    }
                                }
                            }
                        }
                        return if (tick > 4) -1 else 0
                    }

                }
            }
            "No" {}
        }
    }

    override fun getItems() = arrayOf(
        ItemId.TORVA_PLATELEGS_DAMAGED,
        ItemId.TORVA_PLATEBODY_DAMAGED,
        ItemId.TORVA_FULLHELM_DAMAGED
    )

    override fun getObjects() =
        arrayOf(ObjectId.ANVIL_28563)

    private fun Item.fix() = when (id) {
        ItemId.TORVA_PLATELEGS_DAMAGED -> Item(ItemId.TORVA_PLATELEGS) to 2
        ItemId.TORVA_PLATEBODY_DAMAGED -> Item(ItemId.TORVA_PLATEBODY) to 2
        ItemId.TORVA_FULLHELM_DAMAGED -> Item(ItemId.TORVA_FULLHELM) to 1
        else -> error("Unknown torva item $this")
    }

    private fun itemCostString( cost: Int) = "$cost Bandosian component${if (cost > 1) "s" else ""}"
}
