package com.near_reality.plugins.interfaces.death

import com.near_reality.game.content.shop.ShopCurrencyHandler
import com.near_reality.scripts.interfaces.InterfaceScript
import com.zenyte.game.GameInterface
import com.zenyte.game.content.boons.RemnantExchange
import com.zenyte.game.content.gravestone.GravestoneExt.getItemValue
import com.zenyte.game.model.shop.ShopCurrency
import com.zenyte.game.util.AccessMask
import com.zenyte.game.world.entity.player.Player
import com.zenyte.game.world.entity.player.container.impl.ContainerType

/**
 * @author Kris | 14/06/2022
 */
class RemnantInvInterface : InterfaceScript() {
    init {
        GameInterface.REMNANT_INVENTORY {
            val itemLayer = "Item layer"(0) {
                player.selectItem(slotID)
            }
            opened {
                itemLayer.sendComponentSettings(this, 27, AccessMask.CLICK_OP1, AccessMask.CLICK_OP10)
                packetDispatcher.sendClientScript(
                    149,
                    id shl 16 or itemLayer.componentID,
                    ContainerType.INVENTORY.id,
                    4,
                    7,
                    0,
                    -1,
                    "Select",
                    "",
                    "",
                    "",
                    ""
                )
                sendInterface()
            }
        }
    }

    fun Player.selectItem(slot: Int) {
        val itemInSlot = inventory.container.get(slot)
        if (itemInSlot == null || RemnantExchange.ineligibleItem(itemInSlot)) {
            sendMessage("You cannot trade that item to the remnant exchange.")
            sendSound(2277)
            packetDispatcher.sendComponentItem(GameInterface.REMNANT_EXCHANGE.id, 8, 6512, 400)
            varManager.sendVarInstant(19451, -1)
            varManager.sendVarInstant(19450, ShopCurrencyHandler.getAmount(ShopCurrency.EXCHANGE_POINTS, this).toLong().coerceIn(0..Int.MAX_VALUE.toLong()).toInt())
            varManager.sendVarInstant(19452, 0)
            varManager.sendVarInstant(19453, 0)
            return
        }
        varManager.sendVarInstant(19451, slot)
        varManager.sendVarInstant(19450, ShopCurrencyHandler.getAmount(ShopCurrency.EXCHANGE_POINTS, this).toLong().coerceIn(0..Int.MAX_VALUE.toLong()).toInt())
        val sellPrice = RemnantExchange.getValueForItem(itemInSlot)
        packetDispatcher.sendComponentItem(GameInterface.REMNANT_EXCHANGE.id, 8, itemInSlot.id, 400)
        varManager.sendVarInstant(19452, 1)
        varManager.sendVarInstant(19453, (sellPrice * this.exchangeBonus).toInt())
    }
}


