
package com.near_reality.plugins.interfaces.death

import com.near_reality.game.content.shop.ShopCurrencyHandler
import com.near_reality.game.item.CustomItemId
import com.near_reality.game.world.entity.player.dailyRemainingTomes
import com.near_reality.game.world.entity.player.sacrificedScytheOfVitur
import com.near_reality.game.world.entity.player.sacrificedTumekensShadow
import com.near_reality.game.world.entity.player.sacrificedTwistedBow
import com.near_reality.scripts.interfaces.InterfaceScript
import com.near_reality.scripts.interfaces.NamedInterfaceHandler
import com.near_reality.tools.logging.GameLogMessage
import com.near_reality.tools.logging.GameLogger
import com.zenyte.game.GameInterface
import com.zenyte.game.item.Item
import com.zenyte.game.item.ItemId
import com.zenyte.game.model.shop.ShopCurrency
import com.zenyte.game.world.entity.SoundEffect
import com.zenyte.game.world.entity.player.Player
import com.zenyte.game.world.entity.player.dialogue.Dialogue.DialogueOption
import com.zenyte.game.world.entity.player.dialogue.options
import com.zenyte.game.world.entity.player.dialogue.start
import kotlin.math.min

/**
 * @author Kris | 14/06/2022
 */
class RemnantExchInterface : InterfaceScript() {
    init {
        GameInterface.REMNANT_EXCHANGE {
            val forceConfirmItems = mutableListOf(CustomItemId.DONATOR_PIN_10, CustomItemId.DONATOR_PIN_25, CustomItemId.DONATOR_PIN_50, CustomItemId.DONATOR_PIN_100)
            val itemComponent = "Item component"(8)
            val portalComponent = "Portal component"(7)
            "Select 1"(9) {
                player.setCurrentSelectedItemQuantity(1)
            }
            "Select 5"(10) {
                player.setCurrentSelectedItemQuantity(5)
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
                var quantity = min(player.inventory.getAmountOf(itemInSlot.id), player.getCurrentSelectedItemQuantity())
                if(itemInSlot.id == 30215) {//tomes
                    quantity = min(quantity, player.dailyRemainingTomes)
                    if(quantity == 0) {
                        player.sendMessage("You have reached the maximum daily sacrifice cap of 10 exp tomes")
                        return@suspend
                    }
                }
                if(quantity > 1 || forceConfirmItems.contains(itemInSlot.id)) {
                    askMultiSale(player, itemComponent, portalComponent, itemInSlot, price, quantity)
                    return@suspend
                }
                val success = player.inventory.deleteItem(Item(itemInSlot.id, quantity))
                val successfulAmount = success.succeededAmount
                if(itemInSlot.id == 30215)
                    player.dailyRemainingTomes = player.dailyRemainingTomes - successfulAmount
                ShopCurrencyHandler.add(ShopCurrency.EXCHANGE_POINTS, player, (successfulAmount * price.toLong()).toInt())
                GameLogger.log {
                    GameLogMessage.RemnantExchange(username = player.username, item = Item(itemInSlot.id, successfulAmount), value = price)
                }
                player.resetSelectedItem(itemComponent.componentID, portalComponent.componentID)
                player.sendSound(1595)
                player.sendSound(SoundEffect(2115, 0, 10))
                player.packetDispatcher.sendComponentAnimation(`interface`, portalComponent.componentID, 8747)
                when(itemInSlot.id) {
                    ItemId.TWISTED_BOW, ItemId.TWISTED_BOW + 1 ->  {
                        if(!player.sacrificedTwistedBow) {
                            player.sacrificedTwistedBow = true
                            player.sendMessage("You have met the requirements to purchase Twisted Trade-Off.")
                        }
                        return@suspend
                    }
                    ItemId.SCYTHE_OF_VITUR, ItemId.SCYTHE_OF_VITUR + 1 -> {
                        if(!player.sacrificedScytheOfVitur) {
                            player.sacrificedScytheOfVitur = true
                            player.sendMessage("You have met the requirements to purchase Vitur's Offering.")
                        }
                        return@suspend
                    }
                    ItemId.TUMEKENS_SHADOW, ItemId.TUMEKENS_SHADOW + 1 -> {
                        if(!player.sacrificedTumekensShadow) {
                            player.sacrificedTumekensShadow = true
                            player.sendMessage("You have met the requirements to purchase Tumeken's Tribute")
                        }
                        return@suspend
                    }
                }
            }
            opened {
                resetSelectedItem(itemComponent.componentID, portalComponent.componentID)
                packetDispatcher.sendComponentItem(id, itemComponent.componentID, 6512, 400)
                sendInterface()
                GameInterface.REMNANT_INVENTORY.open(this)
            }
        }
    }

    fun Player.resetSelectedItem(componentId: Int, portalComponent: Int) {
        varManager.sendVarInstant(19451, -1)
        varManager.sendVarInstant(19450, ShopCurrencyHandler.getAmount(ShopCurrency.EXCHANGE_POINTS, this).toLong().coerceIn(0..Int.MAX_VALUE.toLong()).toInt())
        packetDispatcher.sendComponentItem(id, componentId, 6512, 400)
        packetDispatcher.sendComponentAnimation(id, portalComponent, 7301)
        varManager.sendVarInstant(19452, 0)
    }

    fun Player.getCurrentSelectedItemSlot(): Int {
        return varManager.getValue(19451)
    }

    fun Player.getCurrentSelectedItemQuantity(): Int {
        return varManager.getValue(19452)
    }

    fun Player.setCurrentSelectedItemQuantity(value: Int) {
        val slot = getCurrentSelectedItemSlot()
        val item = inventory.getItem(slot) ?: return
        val result = if (value == Int.MAX_VALUE) Int.MAX_VALUE else value.coerceAtMost(inventory.getAmountOf(item.id))
        varManager.sendVarInstant(19452, result)
    }

    fun Player.getCurrentSelectedItemSacrificePrice(): Int {
        return varManager.getValue(19453)
    }

    fun askMultiSale(
        player: Player,
        itemComponent: NamedInterfaceHandler,
        portalComponent: NamedInterfaceHandler,
        itemInSlot: Item,
        price: Int,
        quantity: Int
    ) {
        player.dialogueManager.start {
            options("Are you sure you want to sacrifice $quantity x ${itemInSlot.name}?") {
                options += DialogueOption("Yes.") {
                    val success = player.inventory.deleteItem(Item(itemInSlot.id, quantity))
                    val successfulAmount = success.succeededAmount
                    if(itemInSlot.id == 30215)
                        player.dailyRemainingTomes = player.dailyRemainingTomes - successfulAmount
                    ShopCurrencyHandler.add(ShopCurrency.EXCHANGE_POINTS, player, (successfulAmount * price.toLong()).toInt())
                    player.resetSelectedItem(itemComponent.componentID, portalComponent.componentID)
                    player.sendSound(1595)
                    player.sendSound(SoundEffect(2115, 0, 10))
                    player.packetDispatcher.sendComponentAnimation(`interface`, portalComponent.componentID, 8747)
                    when(itemInSlot.id) {
                        ItemId.TWISTED_BOW, ItemId.TWISTED_BOW + 1 ->  {
                            if(!player.sacrificedTwistedBow) {
                                player.sacrificedTwistedBow = true
                                player.sendMessage("You have met the requirements to purchase Twisted Trade-Off.")
                            }
                        }
                        ItemId.SCYTHE_OF_VITUR, ItemId.SCYTHE_OF_VITUR + 1 -> {
                            if(!player.sacrificedScytheOfVitur) {
                                player.sacrificedScytheOfVitur = true
                                player.sendMessage("You have met the requirements to purchase Vitur's Offering.")
                            }
                        }
                        ItemId.TUMEKENS_SHADOW, ItemId.TUMEKENS_SHADOW + 1 -> {
                            if(!player.sacrificedTumekensShadow) {
                                player.sacrificedTumekensShadow = true
                                player.sendMessage("You have met the requirements to purchase Tumeken's Tribute")
                            }
                        }
                    }
                    GameInterface.REMNANT_EXCHANGE.open(player)
                }
                options += DialogueOption("No.")
            }
        }
    }

}
