package com.near_reality.game.content.middleman.trade.request

import com.near_reality.game.content.middleman.MiddleManStaffOption
import com.zenyte.game.world.World
import com.zenyte.game.world.entity.player.Player

/**
 * Represents a middle-man trade request send by the [requester] to the [target].
 *
 * @author Stan van der Bend
 *
 * @param requesterUsername the [username][Player.getUsername] of the player making the trade request.
 * @param targetUsername    the [username][Player.getUsername] of the player receiving the trade request.
 * @param donatorPinId      the id of the donator pin item(s) being offered by the [requester].
 * @param donatorPinAmount  the number of donator pin item(s) being offered by the [requester].
 * @param staffOption       the [MiddleManStaffOption] selected by the [requester].
 */
data class MiddleManTradeRequest(
    val requesterUsername: String,
    val targetUsername: String,
    val donatorPinId: Int,
    val donatorPinAmount: Int,
    val staffOption: MiddleManStaffOption
) {

    val requester: Player?
        get() = World.getPlayerByUsername(requesterUsername)

    val target: Player?
        get() = World.getPlayerByUsername(targetUsername)

    /**
     * Gets the other [username][Player.getUsername],
     * in case of [player] being the [requesterUsername]
     * it returns the [targetUsername] and vise versa.
     */
    fun getOtherName(player: Player) =
        if(requesterUsername == player.username) targetUsername else requesterUsername
}
