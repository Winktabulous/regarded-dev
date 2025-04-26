package com.near_reality.game.content.middleman

import com.near_reality.game.content.middleman.trade.MiddleManPendingTrade
import com.near_reality.game.content.middleman.trade.request.MiddleManTradeRequest
import com.zenyte.game.GameInterface
import com.zenyte.game.GameInterface.MIDDLE_MAN_REQUEST
import com.zenyte.game.model.ui.InterfacePosition
import com.zenyte.game.util.Colour
import com.zenyte.game.world.World
import com.zenyte.game.world.entity.player.MessageType
import com.zenyte.game.world.entity.player.Player
import com.zenyte.game.world.entity.player.dialogue.dialogue
import com.zenyte.game.world.entity.player.dialogue.options
import com.zenyte.game.world.entity.player.privilege.PlayerPrivilege

/**
 * Handles the middle man actions for the argued [player].
 *
 * @author Stan van der Bend
 */
class MiddleManPlayerController(val player: Player) {

    /**
     * The current [MiddleManState] for this [player].
     */
    var state: MiddleManState = MiddleManState.None

    fun openTradeRequestInterface(offerItemId: Int = -1, offerAmount: Int = -1, targetDisplayName: String) {

        if (middleManSystemDisabled())
            return

        if (player.isLocked || player.interfaceHandler.containsInterface(InterfacePosition.CENTRAL)) {
            player.sendMessage("You can't do that right now.")
            return
        }

        val pendingTradeRequest = MiddleManManager.findPendingTradeRequestFrom(player)
        if (pendingTradeRequest != null) {
            player.dialogue {
                plain(
                    "You already have a trade request pending with ${pendingTradeRequest.targetUsername}, " +
                            "you can only have one trade request active at a time, " +
                            "would you like to cancel the currently pending trade request?"
                )
                options("Cancel the pending trade request?") {
                    "Yes." {
                        MiddleManManager.cancelPendingTradeRequest(pendingTradeRequest)
                        openTradeRequestInterface(offerItemId, offerAmount, targetDisplayName)
                    }
                    "No." {}
                }
            }
            return
        }

        val pendingTrade = MiddleManManager.findPendingTradeWith(player)
        if (pendingTrade != null) {
            player.dialogue {
                plain(
                    "You already have a trade pending with ${pendingTrade.getOtherName(player)}, " +
                            "since both players confirmed the trade, " +
                            "this can no longer be canceled by yourself"
                )
                plain(
                    "If you wish to cancel the pending trade, " +
                            "please contact ${pendingTrade.staffOption}."
                )
            }
            return
        }

        val staffOptions = MiddleManManager.getMiddleManOptions()
        if (staffOptions.isEmpty()) {
            player.dialogue {
                plain("There are currently no staff available to middle man your trade." +
                        " Please try again at some later time.")
            }
            return
        }

        state = MiddleManState.MakingRequest

        player.apply {
            middleManStaffOptions = staffOptions
            middleManTargetName = targetDisplayName
            if (offerItemId != -1) {
                middleManOfferItemId = offerItemId
                middleManOfferAmount = offerAmount
            }
            awaitStaffOptionPick()
        }
        MIDDLE_MAN_REQUEST.open(player)
    }

    fun awaitStaffOptionPick() = player.awaitInputInt {
        player.middleManStaffOption = player.middleManStaffOptions[it]
    }

    fun onTradeRequestAccepted(pendingTrade: MiddleManPendingTrade) {
        state = MiddleManState.AwaitConfirmation(pendingTrade)
        GameInterface.MIDDLE_MAN_OFFER.open(player)
    }

    fun ifTradeAwaitConfirmation(consume: MiddleManPendingTrade.() -> Unit) =
        ifTradeAwaitConfirmationOrElse(
            consume,
            elseHandler = { onUnexpectedState<MiddleManState.AwaitConfirmation>(it) })

    private inline fun<reified EXPECTED : MiddleManState> onUnexpectedState(it: MiddleManState) {
        if (player.hasPrivilege(PlayerPrivilege.DEVELOPER))
            player.dialogue { plain("Expected state ${EXPECTED::class} but was $it") }
    }

    private fun ifTradeAwaitConfirmationOrElse(consume: MiddleManPendingTrade.() -> Unit, elseHandler: (MiddleManState) -> Unit) {
        val state = state
        if (state is MiddleManState.AwaitConfirmation)
            consume(state.acceptedTrade)
        else
            elseHandler(state)
    }

    fun confirmRequestOrTrade() {

        if (middleManSystemDisabled())
            return

        when (val oldState = this.state) {
            MiddleManState.MakingRequest -> {

                val targetName = player.middleManTargetName
                val target = targetName?.let { World.getPlayerByDisplayname(it) }
                if (target == null) {
                    player.sendMessage("No player by the name of $targetName is online, did you spell their name correct?")
                    return
                }

                val targetState = target.middleManController.state
                if (targetState != MiddleManState.None) {
                    player.sendMessage("Can not send request to $targetName because they are already in a middle-man trade.")
                    return
                }

                val offerItemId = player.middleManOfferItemId
                val offerAmount = player.middleManOfferAmount

                state = MiddleManState.AwaitAccept

                MiddleManManager.addTradeRequest(
                    MiddleManTradeRequest(
                        requesterUsername = player.username,
                        targetUsername = target.username,
                        donatorPinId = offerItemId,
                        donatorPinAmount = offerAmount,
                        staffOption = player.middleManStaffOption
                    )
                )

                val requesterName = player.playerInformation.displayname
                target.sendMessage(
                    "$requesterName wishes to middle man trade with you.",
                    MessageType.TRADE_REQUEST,
                    requesterName
                )

                player.sendMessage("Send a middle-man trade request to ${target.username}.")
                player.interfaceHandler.closeInterface(MIDDLE_MAN_REQUEST)
            }
            is MiddleManState.AwaitConfirmation -> {

                val trade = oldState.acceptedTrade

                val other = trade.getOther(player)
                if (other == null) {
                    player.sendMessage("Could not confirm trade because the other user is offline")
                    cancelRequestOrTrade()
                    return
                }

                val (canAccept, reasonNot) = MiddleManManager.canAcceptTrade(player, other)
                if (!canAccept) {
                    trade.messagePlayers("Both players must be $reasonNot in order to confirm the trade")
                    return
                }

                val otherState = other.middleManController.state
                if (otherState is MiddleManState.AwaitConfirmation) {
                    trade.setConfirmedFor(player)
                    if (trade.requesterConfirmed && trade.accepterConfirmed) {
                        state = MiddleManState.None
                        other.middleManController.state = MiddleManState.None
                        trade.messagePlayers("Accepted request for middle-man, staff members will be notified.")
                        player.interfaceHandler.closeInterfaces()
                        other.interfaceHandler.closeInterfaces()
                        MiddleManManager.pendingTrades.remove(trade)
                        MiddleManManager.addToBeHandledTrade(trade.toConfirmedTrade())
                    } else {
                        player.sendMessage("Waiting for confirmation of other player.")
                        other.sendMessage("The other player has confirmed the trade.")
                    }
                } else
                    player.sendDeveloperMessage("Invalid state for other $otherState")
            }
            else -> player.dialogue { plain("Invalid state $state") }
        }
    }

    private fun middleManSystemDisabled(): Boolean {
        if (!MiddleManManager.enabled) {
            player.dialogue { plain("The middle-man system is temporarily disabled, please try again later.") }
            return true
        }
        return false
    }

    fun cancelRequestOrTrade() {
        when (val oldState = state) {
            MiddleManState.MakingRequest -> {

                state = MiddleManState.None

                player.sendMessage("You canceled the trade request.")
                player.interfaceHandler.closeInterfaces()
            }
            is MiddleManState.AwaitConfirmation -> {

                val trade = oldState.acceptedTrade

                if (MiddleManManager.pendingTrades.remove(trade)) {
                    state = MiddleManState.None

                    player.sendMessage("You canceled the trade.")
                    player.interfaceHandler.closeInterfaces()

                    trade.getOther(player)?.run {
                        middleManController.state = MiddleManState.None
                        sendMessage("The other player has canceled the trade.")
                        interfaceHandler.closeInterfaces()
                    }

                    trade.returnItems()
                } else
                    MiddleManManager.logger.error("Failed to cancel pending trade {} was it already removed?", trade)
            }
            else -> player.sendDeveloperMessage("Ignored cancel in state ${Colour.RS_RED.wrap(state.javaClass.simpleName)}")
        }
    }
}
