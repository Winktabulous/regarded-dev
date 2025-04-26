package com.near_reality.game.content.middleman.trade

import com.near_reality.game.content.middleman.MiddleManManager
import com.near_reality.game.content.middleman.MiddleManStaffOption
import com.zenyte.game.item.Item
import com.zenyte.game.world.World
import com.zenyte.game.world.entity.player.Player
import com.zenyte.game.world.entity.player.container.Container
import com.zenyte.game.world.entity.player.container.ContainerPolicy
import com.zenyte.game.world.entity.player.container.impl.ContainerType
import com.zenyte.game.world.entity.player.container.impl.Inventory
import java.util.*

/**
 * Represents a [MiddleManTrade] in the second window,
 * waiting for the [accepter] to offer items and or OSRS gp.
 *
 * @author Stan van der Bend
 */
class MiddleManPendingTrade(
    override val requesterUsername: String,
    override val accepterUsername: String,
    override val requesterDonatorPin: Item,
    override val staffOption: MiddleManStaffOption,
) : MiddleManTrade() {

    var accepterConfirmed: Boolean = false
    var requesterConfirmed: Boolean = false


    /**
     * The number of OSRS gp (in millions) the [accepter] offers.
     */
    override var accepterOSRSMillions = 0
        set(value) {
            field = value.coerceAtLeast(0)
        }

    /**
     * A [Container] of items that the [accepter] offers.
     */
    override val accepterContainer: Container =
        Container(ContainerPolicy.NORMAL, ContainerType.TRADE, Optional.empty())

    /**
     * Deposits [amount] times an item with [itemId]
     * from the [accepter]'s [inventory][Player.inventory] into the [accepterContainer].
     */
    fun deposit(itemId: Int, amount: Int) {
        val accepter = requireNotNull(accepter) {
            "Could not deposit item ($itemId, $amount) because $accepterUsername is offline"
        }
        val inventory: Inventory = accepter.inventory
        accepterContainer.deposit(accepter, inventory.container, inventory.container.getSlotOf(itemId), amount)
        inventory.refresh()
        accepter.packetDispatcher.sendUpdateItemContainer(accepterContainer)
        val requester = requireNotNull(requester) {
            "Could not update container for $requesterUsername because player is offline"
        }
        requester.packetDispatcher.sendUpdateItemContainer(accepterContainer)
    }

    /**
     * Withdraw [amount] times an item with [itemId]
     * from the [accepterContainer] into the [accepter]'s [inventory][Player.inventory].
     */
    fun withdraw(itemId: Int, amount: Int) {
        val accepter = requireNotNull(accepter) {
            "Could not withdraw item ($itemId, $amount) because $accepterUsername is offline"
        }
        val inventory = accepter.inventory
        val slot: Int = accepterContainer.getSlotOf(itemId)
        inventory.container.deposit(accepter, accepterContainer, slot, amount)
        inventory.refresh()
        accepter.packetDispatcher.sendUpdateItemContainer(accepterContainer)
        val requester = requireNotNull(requester) {
            "Could not update container for $requesterUsername because player is offline"
        }
        requester.packetDispatcher.sendUpdateItemContainer(accepterContainer)
    }

    /**
     * Gets the other [Player], in case of [player] being the [requester] it returns the [accepter] and vise versa.
     */
    fun getOther(player: Player): Player? =
        World.getPlayerByUsername(getOtherName(player))

    /**
     * Returns the items in [accepterContainer] to the [accepter]
     * and the [requesterDonatorPin] to the [requester].
     */
    fun returnItems() {
        val accepter = this.accepter
        if (accepter != null)
            giveOfferedItemsTo(accepter)
        else {
            MiddleManManager.itemsToBeReturnedMap
                .getOrPut(accepterUsername) { mutableListOf() }
                .addAll(accepterContainer.items.values)
            MiddleManManager.logger.error(
                "Could not return items {} to {} because user is offline",
                accepterContainer,
                accepterUsername
            )
        }
        val requester = this.requester
        if (requester != null)
            giveDonatorPinTo(requester)
        else {
            MiddleManManager.itemsToBeReturnedMap
                .getOrPut(requesterUsername) { mutableListOf() }
                .add(requesterDonatorPin)
            MiddleManManager.logger.error(
                "Could not return donator pin {} to {} because user is offline",
                requesterDonatorPin,
                requesterUsername
            )
        }
    }

    fun hasConfirmed(player: Player) =
        when (player.username) {
            requesterUsername -> requesterConfirmed
            accepterUsername -> accepterConfirmed
            else -> {
                MiddleManManager.logger.error(
                    "Invalid check player {} must be either {} or {}",
                    player,
                    requesterUsername,
                    accepterUsername
                )
                false
            }
        }

    fun setConfirmedFor(player: Player) {
        when (player.username) {
            requesterUsername -> requesterConfirmed = true
            accepterUsername -> accepterConfirmed = true
            else -> MiddleManManager.logger.error(
                "Could not confirm player {} must be either {} or {}",
                player,
                requesterUsername,
                accepterUsername
            )
        }
    }
    /**
     * Creates a [MiddleManConfirmedTrade] from this trade.
     *
     * This means both players accepted the trade and it is now awaiting staff approval.
     */
    fun toConfirmedTrade() =
        MiddleManConfirmedTrade(
            requesterUsername = requesterUsername,
            accepterUsername = accepterUsername,
            requesterDonatorPin = requesterDonatorPin,
            accepterContainer = accepterContainer,
            accepterOSRSMillions = accepterOSRSMillions,
            staffOption = staffOption
        )

}

