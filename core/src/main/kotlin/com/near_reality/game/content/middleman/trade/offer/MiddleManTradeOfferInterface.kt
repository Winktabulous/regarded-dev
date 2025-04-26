package com.near_reality.game.content.middleman.trade.offer

import com.near_reality.game.content.middleman.middleManController
import com.near_reality.game.content.middleman.trade.MiddleManPendingTrade
import com.zenyte.game.GameInterface
import com.zenyte.game.item.ItemId
import com.zenyte.game.model.ui.Interface
import com.zenyte.game.model.ui.Interface.Handler
import com.zenyte.game.packet.sendItemOptionScript
import com.zenyte.game.util.AccessMask.*
import com.zenyte.game.util.ItemUtil
import com.zenyte.game.util.component
import com.zenyte.game.world.entity.player.Player
import com.zenyte.game.world.entity.player.container.impl.ContainerType
import mgi.types.config.items.ItemDefinitions
import java.util.*

/**
 * Represents the interface where both players can confirm the [MiddleManPendingTrade]
 * and where the accepting player can offer items/OSRS gp.
 *
 * @author Stan van der Bend
 */
@Suppress("UNUSED")
class MiddleManTradeOfferInterface : Interface() {

    override fun attach() {

        put(13, "My Offered Items")

        put(53, "Minus 1")
        put(54, "Plus 1")
        put(59, "Set quantity")

        put(26, "Cancel")
        put(27, "Confirm")
    }

    override fun open(player: Player) {
        player.middleManController.ifTradeAwaitConfirmation {
            player.interfaceHandler.apply {
                sendInterface(this@MiddleManTradeOfferInterface)
                sendInterface(GameInterface.MIDDLE_MAN_OFFER_INVENTORY)
            }
            player.packetDispatcher.apply {
                if (requester == player) {
                    sendComponentText(`interface`, 6, "Your offer:")
                    sendComponentText(`interface`, 11, "${accepterUsername}'s offer:")
                    sendItemOptionScript(
                        149, 1603, 13, ContainerType.TRADE,
                        scrollBarComponentId = 14
                    )
                    sendItemOptionScript(
                        149, 1613, 0, ContainerType.INVENTORY,
                    )
                    sendComponentSettings(
                        1613, 0, 0, 27,
                        CLICK_OP10
                    )
                    sendComponentSettings(
                        1603, 13, 0, 27,
                        CLICK_OP10
                    )
                } else {
                    sendComponentText(`interface`, 6, "${requesterUsername}'s offer:")
                    sendComponentText(`interface`, 11, "Your offer:")
                    sendItemOptionScript(
                        149, 1613, 0, ContainerType.INVENTORY,
                        "Add 1", "Add 5", "Add 10", "Add All", "Add X"
                    )
                    sendComponentSettings(
                        1613, 0, 0, 27,
                        CLICK_OP1, CLICK_OP2, CLICK_OP3, CLICK_OP4, CLICK_OP5, CLICK_OP10
                    )
                    sendComponentSettings(
                        1603, 13, 0, 27,
                        CLICK_OP3, CLICK_OP4, CLICK_OP5, CLICK_OP7, CLICK_OP8, CLICK_OP10
                    )
                }
                sendClientScript(10589, ItemId.COINS, accepterOSRSMillions * 1_000_000, 1603 component 50)
                sendUpdateItemContainer(accepterContainer)
                sendComponentItem(`interface`, 21, requesterDonatorPin.id, requesterDonatorPin.amount)
                sendComponentText(`interface`, 16, ItemDefinitions.nameOf(requesterDonatorPin.id))
                sendComponentText(`interface`, 17, requesterDonatorPin.amount)
                sendComponentText(`interface`, 59, accepterOSRSMillions)
            }
        }
    }

    override fun close(player: Player, replacement: Optional<GameInterface>) {
        player.middleManController.cancelRequestOrTrade()
    }

    override fun build() {
        bind("My Offered Items", Handler { player, slotId, itemId, option ->
            if (option == 10) {
                ItemUtil.sendItemExamine(player, itemId)
                return@Handler
            }
            player.middleManController.ifTradeAwaitConfirmation {
                if (player == accepter) {
                    val offerOption = when (option) {
                        3 -> MiddleManTradeOfferOption.Amount(1)
                        4 -> MiddleManTradeOfferOption.Amount(5)
                        5 -> MiddleManTradeOfferOption.Amount(10)
                        7 -> MiddleManTradeOfferOption.X
                        8 -> MiddleManTradeOfferOption.Amount(accepterContainer.getAmountOf(itemId))
                        else -> {
                            player.sendDeveloperMessage("Unhandled option $option")
                            return@ifTradeAwaitConfirmation
                        }
                    }
                    when (offerOption) {
                        is MiddleManTradeOfferOption.Amount -> {
                            withdraw(itemId, offerOption.amount)
                        }

                        MiddleManTradeOfferOption.X -> {
                            player.sendInputInt("Enter amount:") { amountEntered: Int ->

                                val amount = amountEntered.coerceAtMost(accepterContainer.getAmountOf(itemId))
                                if (amount <= 0)
                                    return@sendInputInt

                                withdraw(itemId, amount)
                            }
                        }
                    }
                }
            }
        })
        bind("Minus 1") { player ->
            player.middleManController.ifTradeAwaitConfirmation {
                if (player == accepter) {
                    --accepterOSRSMillions
                    updateOSRSGp()
                }
            }
        }
        bind("Plus 1") { player ->
            player.middleManController.ifTradeAwaitConfirmation {
                if (player == accepter) {
                    ++accepterOSRSMillions
                    updateOSRSGp()
                }
            }
        }
        bind("Confirm") { player ->
            player.middleManController.ifTradeAwaitConfirmation {
                if (hasConfirmed(player)) {
                    player.sendMessage(
                        "You have already confirmed the trade, " +
                                "waiting for the other player to confirm.")
                } else {
                    player.awaitInputInt {
                        player.middleManController.confirmRequestOrTrade()
                        player.packetDispatcher.sendClientScript(
                            10542,
                            0,
                            1603 component 29,
                            1603 component 30,
                            1603 component 31
                        )
                    }
                }
            }
        }
        bind("Set quantity") { player ->
            player.sendInputInt("Enter the number of OSRS millions to offer") {
                player.middleManController.ifTradeAwaitConfirmation {
                    if (player == accepter) {
                        accepterOSRSMillions = it
                        updateOSRSGp()
                    }
                }
            }
        }
        bind("Cancel") { player -> player.middleManController.cancelRequestOrTrade() }
    }

    private fun MiddleManPendingTrade.updateOSRSGp() {
        val requester = requireNotNull(requester) {
            "Could not update OSRS gp for $requesterUsername because user is offline"
        }
        val accepter = requireNotNull(accepter) {
            "Could not update OSRS gp for $accepterUsername because user is offline"
        }
        requester.packetDispatcher.sendComponentText(`interface`, 59, "${accepterOSRSMillions}M")
        accepter.packetDispatcher.sendComponentText(`interface`, 59, "${accepterOSRSMillions}M")
        requester.packetDispatcher.sendClientScript(
            10589,
            ItemId.COINS,
            accepterOSRSMillions * 1_000_000,
            1603 component 50
        )
        accepter.packetDispatcher.sendClientScript(
            10589,
            ItemId.COINS,
            accepterOSRSMillions * 1_000_000,
            1603 component 50
        )
    }

    override fun getInterface() = GameInterface.MIDDLE_MAN_OFFER
}
