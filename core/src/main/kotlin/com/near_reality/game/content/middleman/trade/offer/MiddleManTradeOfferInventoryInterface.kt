package com.near_reality.game.content.middleman.trade.offer

import com.near_reality.game.content.middleman.middleManController
import com.zenyte.game.GameInterface
import com.zenyte.game.model.ui.Interface
import com.zenyte.game.world.entity.player.Player
import java.util.*

/**
 * Represents an inventory [Interface] that opens for the middle-man trade accepter,
 * in which the accepter can offer items from their inventory.
 *
 * @author Stan van der Bend
 */
@Suppress("UNUSED")
class MiddleManTradeOfferInventoryInterface : Interface() {

    override fun attach() {
        put(0, "My Tradeable Inventory")
    }

    override fun close(player: Player, replacement: Optional<GameInterface>) = Unit

    override fun open(player: Player) = Unit

    override fun build() {
        bind("My Tradeable Inventory") { player: Player, slotId: Int, itemId: Int, option: Int ->
            val itemAtSlot = player.inventory.getItem(slotId)
            if (itemAtSlot != null && !itemAtSlot.isTradable)
            {
                player.sendMessage("You can't trade this item.")
                return@bind
            }
            val offerOption = when(option){
                1 -> MiddleManTradeOfferOption.Amount(1)
                2 -> MiddleManTradeOfferOption.Amount(5)
                3 -> MiddleManTradeOfferOption.Amount(10)
                4 -> MiddleManTradeOfferOption.Amount(player.inventory.getAmountOf(itemId))
                5 -> MiddleManTradeOfferOption.X
                else -> return@bind
            }
            player.middleManController.ifTradeAwaitConfirmation {
                if (player == accepter) {
                    when (offerOption) {
                        is MiddleManTradeOfferOption.Amount -> {
                            deposit(itemId, offerOption.amount)
                        }
                        MiddleManTradeOfferOption.X -> {
                            player.sendInputInt("Enter amount:") { amountEntered: Int ->

                                val amount = amountEntered.coerceAtMost(player.inventory.getAmountOf(itemId))
                                if (amount <= 0)
                                    return@sendInputInt

                                deposit(itemId, amount)
                            }
                        }
                    }
                }
            }
        }
    }

    override fun getInterface() = GameInterface.MIDDLE_MAN_OFFER_INVENTORY
}
