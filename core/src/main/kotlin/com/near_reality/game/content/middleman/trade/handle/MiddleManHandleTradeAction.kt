package com.near_reality.game.content.middleman.trade.handle

/**
 * Represents an action a staff member can take with respect to a confirmed middle-man trade.
 *
 * @author Stan van der Bend
 */
sealed class MiddleManHandleTradeAction {

    /**
     * The trade is accepted and the respective items are automatically returned to their new owners.
     */
    object Accept : MiddleManHandleTradeAction() {
        override fun toString() = "accept"
    }

    /**
     * The trade is declined and the respective items are automatically returned to their original owners.
     */
    object Decline : MiddleManHandleTradeAction(){
        override fun toString() = "decline"
    }
}
