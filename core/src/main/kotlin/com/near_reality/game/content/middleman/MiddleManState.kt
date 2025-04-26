package com.near_reality.game.content.middleman

import com.near_reality.game.content.middleman.trade.MiddleManPendingTrade

/**
 * Represents the state of a [MiddleManPlayerController] for a specific player.
 *
 * @author Stan van der Bend
 */
sealed class MiddleManState {

    /**
     * There is no state, anything can be done at this point.
     */
    object None : MiddleManState()

    /**
     * A player [opened the middle-man trade request creation interface][MiddleManPlayerController.openTradeRequestInterface].
     */
    object MakingRequest : MiddleManState()

    /**
     * A player has submitted a middle-man trade request and is waiting for target player to accept.
     */
    object AwaitAccept : MiddleManState()

    /**
     * The middle-man trade request has been accepted by the target player,
     * the offers window is now opened and both players must confirm.
     */
    class AwaitConfirmation(val acceptedTrade: MiddleManPendingTrade) : MiddleManState()

}
