package com.near_reality.game.content.middleman.trade.offer

/**
 * Represents an option in an item container menu for adding/removing items to offer in a middle-man trade.
 *
 * @author Stan van der Bend
 */
sealed class MiddleManTradeOfferOption {

    /**
     * Add/remove [amount] items.
     */
    class Amount(val amount: Int) : MiddleManTradeOfferOption()

    /**
     * Prompt for amount of items to add/remove.
     */
    object X : MiddleManTradeOfferOption()
}
