@file:Suppress("DuplicatedCode")

package com.near_reality.plugins.interfaces.death

import com.near_reality.scripts.interfaces.InterfaceScript
import com.zenyte.game.GameInterface
import com.zenyte.game.item.Item
import com.zenyte.game.world.entity.SoundEffect
import com.zenyte.game.world.entity.player.Player
import kotlin.math.min

/**
 * @author Kris | 14/06/2022
 */
class DeathsOfficeSacrificeInterface : InterfaceScript() {
    init {
        GameInterface.DEATHS_OFFICE_SACRIFICE {
            val itemComponent = "Item component"(8)
            val portalComponent = "Portal component"(7)
            "Select 1"(9) {
                player.setCurrentSelectedItemQuantity(1)
            }
            "Select 5"(10) {
                player.setCurrentSelectedItemQuantity(10)
            }
            "Select X"(11) {
                player.sendInputInt("How many would you like to sacrifice?") { value ->
                    player.setCurrentSelectedItemQuantity(value.coerceAtLeast(1))
                }
            }
            "Select All"(12) {
                player.setCurrentSelectedItemQuantity(Int.MAX_VALUE)
            }
            "Confirm".suspend(13) {
                val slot = player.getCurrentSelectedItemSlot()
                val itemInSlot = player.inventory.getItem(slot) ?: return@suspend
                val price = player.getCurrentSelectedItemSacrificePrice()
                val quantity = min(itemInSlot.amount, player.getCurrentSelectedItemQuantity())
                if (price < 10_000) {
                    return@suspend player.sendMessage("Death will not accept items worth less than 10,000 coins per <col=ef1020>individual item</col>.")
                }
                val success = player.inventory.deleteItem(Item(itemInSlot.id, quantity))
                val successfulAmount = success.succeededAmount
                player.gravestone.coinsInCoffer += successfulAmount * price.toLong()
                player.resetSelectedItem(itemComponent.componentID, portalComponent.componentID)
                player.sendSound(1595)
                player.sendSound(SoundEffect(2115, 0, 10))
                player.packetDispatcher.sendComponentAnimation(`interface`, portalComponent.componentID, 8747)
            }
            opened {
                resetSelectedItem(itemComponent.componentID, portalComponent.componentID)
                packetDispatcher.sendComponentItem(id, itemComponent.componentID, 6512, 400)
                sendInterface()
                GameInterface.DEATHS_OFFICE_SACRIFICE_INV.open(this)
            }
        }
    }

    fun Player.resetSelectedItem(componentId: Int, portalComponent: Int) {
        varManager.sendVarInstant(262, -1)
        varManager.sendVarInstant(261, gravestone.coinsInCoffer.coerceIn(0..Int.MAX_VALUE.toLong()).toInt())
        packetDispatcher.sendComponentItem(id, componentId, 6512, 400)
        packetDispatcher.sendComponentAnimation(id, portalComponent, 7301)
        varManager.sendVarInstant(263, 0)
    }

    fun Player.getCurrentSelectedItemSlot(): Int {
        return varManager.getValue(262)
    }

    fun Player.getCurrentSelectedItemQuantity(): Int {
        return varManager.getValue(263)
    }

    fun Player.setCurrentSelectedItemQuantity(value: Int) {
        val slot = getCurrentSelectedItemSlot()
        val item = inventory.getItem(slot) ?: return
        val result = if (value == Int.MAX_VALUE) Int.MAX_VALUE else value.coerceAtMost(inventory.getAmountOf(item.id))
        varManager.sendVarInstant(263, result)
    }

    fun Player.getCurrentSelectedItemSacrificePrice(): Int {
        return varManager.getValue(264)
    }
}
