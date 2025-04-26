@file:Suppress("UNCHECKED_CAST")

package com.near_reality.game.content.middleman

import com.near_reality.game.content.middleman.MiddleManConstants.MM_CONTROLLER
import com.near_reality.game.content.middleman.MiddleManConstants.MM_OFFER_ITEM_AMOUNT_ATTRIBUTE_KEY
import com.near_reality.game.content.middleman.MiddleManConstants.MM_OFFER_ITEM_ID_ATTRIBUTE_KEY
import com.near_reality.game.content.middleman.MiddleManConstants.MM_STAFF_OPTIONS_ATTRIBUTE_KEY
import com.near_reality.game.content.middleman.MiddleManConstants.MM_STAFF_OPTION_ATTRIBUTE_KEY
import com.near_reality.game.content.middleman.MiddleManConstants.MM_TARGET_NAME_ATTRIBUTE_KEY
import com.near_reality.game.content.middleman.MiddleManConstants.MM_VIEW_TRADE_HISTORY_LIST_ATTRIBUTE_KEY
import com.near_reality.game.content.middleman.MiddleManConstants.MM_VIEW_TRADE_LIST_ATTRIBUTE_KEY
import com.near_reality.game.content.middleman.MiddleManConstants.donatorPinItemIds
import com.near_reality.game.content.middleman.trade.MiddleManConfirmedTrade
import com.near_reality.game.content.middleman.trade.MiddleManHandledTrade
import com.zenyte.game.world.entity.player.Player

/**
 * Gets a (new) [MiddleManPlayerController] for this [Player].
 *
 * In case no controller is associated with the player, create a new one
 * and associate it with the player.
 */
val Player.middleManController
    get(): MiddleManPlayerController {
        var controller = temporaryAttributes[MM_CONTROLLER] as? MiddleManPlayerController
        if (controller == null) {
            controller = MiddleManPlayerController(this)
            temporaryAttributes[MM_CONTROLLER] = controller
            val pendingTradeRequest = MiddleManManager.findPendingTradeRequestFrom(this)
            val pendingTrade = MiddleManManager.findPendingTradeWith(this)

            if (pendingTrade != null && pendingTradeRequest != null) {
                MiddleManManager.logger.warn(
                    "Found both a pending trade and a pending trade request for player {}, " +
                            "(pending trade with {}, pending trade request with {})",
                    username,
                    pendingTrade.getOtherName(this),
                    pendingTradeRequest.getOtherName(this)
                )
            }

            if (pendingTradeRequest?.requester == this)
                controller.state = MiddleManState.AwaitAccept

            if (pendingTrade != null)
                controller.state = MiddleManState.AwaitConfirmation(pendingTrade)

        }
        return controller
    }

/**
 * The id of the item being offered by [this player][Player] in the middle man trade request window.
 *
 * The set value is either the id of a Donator pin or else -1 (should never happen).
 */
var Player.middleManOfferItemId: Int
    get() = getNumericTemporaryAttribute(MM_OFFER_ITEM_ID_ATTRIBUTE_KEY).toInt()
    set(value) {
        temporaryAttributes[MM_OFFER_ITEM_ID_ATTRIBUTE_KEY] =
            if (donatorPinItemIds.contains(value))
                value
            else {
                MiddleManManager.logger.error("Attempted to offer invalid item id {} must be one of {}", value, donatorPinItemIds)
                -1
            }
    }

/**
 * The amount of the item being offered by [this player][Player] in the middle man trade request window.
 *
 * The set value is at least 1 and at most the number of [middleManOfferItemId] in the player's inventory.
 */
var Player.middleManOfferAmount: Int
    get() = getNumericTemporaryAttribute(MM_OFFER_ITEM_AMOUNT_ATTRIBUTE_KEY).toInt()
    set(value) {
        temporaryAttributes[MM_OFFER_ITEM_AMOUNT_ATTRIBUTE_KEY] = value
            .coerceAtLeast(1)
            .coerceAtMost(inventory.getAmountOf(middleManOfferItemId))
    }

/**
 * The [Player.getUsername] of the player that [this player][Player] is requesting to start a middle-man trade with.
 */
var Player.middleManTargetName: String?
    get() =
        temporaryAttributes[MM_TARGET_NAME_ATTRIBUTE_KEY] as? String
    set(value) {
        temporaryAttributes[MM_TARGET_NAME_ATTRIBUTE_KEY] = value
    }

/**
 * The [MiddleManStaffOption] that [this player][Player] has selected for handling
 * their middle-man trade. Which is either a [specific staff member][MiddleManStaffOption.Specific]
 * or [any staff member][MiddleManStaffOption.Any] in case the player has no preference.
 */
var Player.middleManStaffOption: MiddleManStaffOption
    get() = (temporaryAttributes[MM_STAFF_OPTION_ATTRIBUTE_KEY] as? MiddleManStaffOption)
            ?: MiddleManStaffOption.Any
    set(value) {
        temporaryAttributes[MM_STAFF_OPTION_ATTRIBUTE_KEY] = value
    }


/**
 * A list of available [options][MiddleManStaffOption], depending on the staff members online,
 * by default always returns a singleton list containing [MiddleManStaffOption.Any].
 */
var Player.middleManStaffOptions: List<MiddleManStaffOption>
    get() = (temporaryAttributes[MM_STAFF_OPTIONS_ATTRIBUTE_KEY] as? List<MiddleManStaffOption>)
            ?: emptyList()
    set(value) {
        temporaryAttributes[MM_STAFF_OPTIONS_ATTRIBUTE_KEY] = value
    }

/**
 * A list of trades[MiddleManConfirmedTrade] being viewed by the staff member (this [Player]).
 */
var Player.middleManConfirmedTradeListView: List<MiddleManConfirmedTrade>
    get() = (temporaryAttributes[MM_VIEW_TRADE_LIST_ATTRIBUTE_KEY] as? List<MiddleManConfirmedTrade>)
        ?: emptyList()
    set(value) {
        temporaryAttributes[MM_VIEW_TRADE_LIST_ATTRIBUTE_KEY] = value
    }


/**
 * A list of trades[MiddleManHandledTrade] being viewed by the staff member (this [Player]).
 */
var Player.middleManHandledTradeListView: List<List<MiddleManHandledTrade>>
    get() = (temporaryAttributes[MM_VIEW_TRADE_HISTORY_LIST_ATTRIBUTE_KEY] as? List<List<MiddleManHandledTrade>>)
        ?: emptyList()
    set(value) {
        temporaryAttributes[MM_VIEW_TRADE_HISTORY_LIST_ATTRIBUTE_KEY] = value
    }
