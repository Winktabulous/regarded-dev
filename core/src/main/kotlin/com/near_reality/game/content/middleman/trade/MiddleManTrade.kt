package com.near_reality.game.content.middleman.trade

import com.near_reality.game.content.middleman.MiddleManStaffOption
import com.zenyte.game.item.Item
import com.zenyte.game.world.World
import com.zenyte.game.world.entity.player.Player
import com.zenyte.game.world.entity.player.container.Container

/**
 * Represents an abstraction for an ongoing middle-man trade.
 *
 * @see MiddleManConfirmedTrade   a trade confirmed by two players and awaiting for staff to be handled.
 * @see MiddleManPendingTrade
 *
 * @author Stan van der Bend
 */
abstract class MiddleManTrade {

    val requester: Player?
        get() = World.getPlayerByUsername(requesterUsername)

    val accepter: Player?
        get() = World.getPlayerByUsername(accepterUsername)

    /**
     * The [username][Player.getUsername] of the player making the trade request.
     */
    abstract val requesterUsername: String

    /**
     * The [username][Player.getUsername] of the player accepting the trade request.
     */
    abstract val accepterUsername: String

    /**
     * The [Item] (must be a Donator pin) that the requester offers.
     */
    abstract val requesterDonatorPin: Item

    /**
     * A [Container] of items that the accepter offers.
     */
    abstract val accepterContainer: Container

    /**
     * The number of OSRS gp (in millions) the accepter offers.
     */
    abstract val accepterOSRSMillions: Int

    /**
     * The [MiddleManStaffOption] selected by the requester.
     */
    abstract val staffOption: MiddleManStaffOption

    /**
     * [Sends a message][Player.sendMessage] containing [contents] to both the [accepter] and [requester].
     */
    fun messagePlayers(contents: String) {
        accepter?.sendMessage(contents)
        requester?.sendMessage(contents)
    }

    /**
     * Gets the other [username][Player.getUsername],
     * in case of [player] being the [requesterUsername]
     * it returns the [accepterUsername] and vise versa.
     */
    fun getOtherName(player: Player) =
        if(requesterUsername == player.username) accepterUsername else requesterUsername

    /**
     * Adds all the item from the [accepterContainer] to the inventory of the [player]
     * or in case their inventory is full to their bank.
     */
    fun giveOfferedItemsTo(player: Player) {
        accepterContainer.items.values.forEach {
            addToBank(player, it)
        }
    }

    /**
     * Adds the [requesterDonatorPin] to the inventory of the [player]
     * or in case their inventory is full to their bank.
     */
    fun giveDonatorPinTo(player: Player) {
        addToBank(player, requesterDonatorPin)
    }

    private fun addToBank(player: Player, item: Item) {
        player.bank.add(item)
    }

    fun toInterfaceString(): String {
        val accepterItems = accepterContainer.items.values.mapNotNull { "${it.id}|${it.amount}" }
        return buildString {
            append("OSRS GP: ${accepterOSRSMillions}M")
            append('|')
            append(requester?.titleName ?: requesterUsername)
            append('|')
            append(accepter?.titleName ?: accepterUsername)
            append('|')
            if (accepterItems.isEmpty()) {
                append(0)
            } else {
                append(accepterItems.size)
                append('|')
                append(accepterItems.joinToString("|"))
            }
            append('|')
            append(1)
            append('|')
            append(requesterDonatorPin.let { "${it.id}|${it.amount}" })
        }
    }
}
