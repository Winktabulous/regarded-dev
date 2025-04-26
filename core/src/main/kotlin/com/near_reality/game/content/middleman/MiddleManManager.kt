package com.near_reality.game.content.middleman

import com.near_reality.game.content.middleman.MiddleManConstants.donatorPinItemIds
import com.near_reality.game.content.middleman.trade.MiddleManConfirmedTrade
import com.near_reality.game.content.middleman.trade.MiddleManHandledTrade
import com.near_reality.game.content.middleman.trade.MiddleManPendingTrade
import com.near_reality.game.content.middleman.trade.MiddleManTrade
import com.near_reality.game.content.middleman.trade.handle.MiddleManHandleTradeAction
import com.near_reality.game.content.middleman.trade.request.MiddleManTradeRequest
import com.near_reality.tools.logging.GameLogMessage
import com.near_reality.tools.logging.GameLogger
import com.zenyte.cores.CoresManager
import com.zenyte.game.content.flowerpoker.FlowerPokerManager
import com.zenyte.game.item.Item
import com.zenyte.game.util.Colour
import com.zenyte.game.world.entity.player.Player
import com.zenyte.game.world.entity.player.container.RequestResult
import com.zenyte.game.world.entity.player.dialogue.dialogue
import com.zenyte.game.world.entity.player.privilege.PlayerPrivilege
import com.zenyte.game.world.region.GlobalAreaManager
import org.slf4j.LoggerFactory
import java.util.*
import kotlin.collections.ArrayDeque

/**
 * Manages [middle-man trades][MiddleManTrade] and [middle-man controllers][MiddleManPlayerController].
 *
 * @author Stan van der Bend
 */
object MiddleManManager {

    /**
     * If `false` middle-man trading is not available to players.
     *
     * @see MiddleManCommands `::mm` can be used to disable it.
     */
    var enabled = true

    /**
     * A map of [player names][Player.getUsername] to [staff players][Player].
     */
    private val onlineMiddleMans = mutableMapOf<String, Player>()

    /**
     * A list of [pending trade requests][MiddleManTradeRequest].
     */
    private val pendingRequests = ArrayDeque<MiddleManTradeRequest>()

    /**
     * A list of [trades][MiddleManPendingTrade] that has been accepted but not yet confirmed.
     */
    internal val pendingTrades = mutableListOf<MiddleManPendingTrade>()

    /**
     * A list of [trades][MiddleManHandledTrade] that has been handled in the past.
     *
     * Serialisation is handled by the companion object of [MiddleManHandledTrade].
     */
    internal val handledTrades = mutableListOf<MiddleManHandledTrade>()

    /**
     * A deque of [trades][MiddleManConfirmedTrade] that needs to be [handled][MiddleManHandleTradeAction]
     * by a [staff member][onlineMiddleMans].
     *
     * Serialisation is handled by the companion object of [MiddleManConfirmedTrade].
     */
    internal val toBeHandledTrades = ArrayDeque<MiddleManConfirmedTrade>()

    internal val itemsToBeReturnedMap = mutableMapOf<String, MutableList<Item>>()

    /**
     * A logger instance for logging middle-man related activities.
     */
    internal val logger = LoggerFactory.getLogger(MiddleManManager::class.java)

    /**
     * TODO: replace with listener for login event.
     */
    fun onLogin(player: Player) {
        if (player.isStaff) {
            if (player.privilege != PlayerPrivilege.HIDDEN_ADMINISTRATOR
                && !player.username.equals("effigyswiper", true)
                && player.privilege != PlayerPrivilege.TRUE_DEVELOPER) {
                onlineMiddleMans[player.username] = player
            }
        }
        val itemsToBeReturned = itemsToBeReturnedMap[player.username]
        if (itemsToBeReturned != null) {
            itemsToBeReturnedMap.remove(player.username)
            for (item in itemsToBeReturned) {
                 player.bank.add(item)
            }
            player.sendMessage(Colour.RS_GREEN.wrap("${itemsToBeReturned.size} items were returned to your bank from your last middle-man trade"))
        }
    }

    /**
     * TODO: update open interfaces with new MM names list?
     */
    fun onLogout(player: Player) {
        onlineMiddleMans.remove(player.username)

    }

    /**
     * Returns a list of options displayed in the drop-down menu for selecting a middle man.
     */
    fun getMiddleManOptions(): List<MiddleManStaffOption> =
//        listOf(MiddleManStaffOption.Any) +
                onlineMiddleMans.values
                    .map { MiddleManStaffOption.Specific(it.username, it.privilege.crown()) }

    fun addTradeRequest(request: MiddleManTradeRequest) =
        pendingRequests.addLast(request)

    fun findPendingTradeRequestFrom(player: Player) =
        pendingRequests.find { it.requester == player }

    fun cancelPendingTradeRequest(request: MiddleManTradeRequest) =
        pendingRequests.remove(request)

    fun findPendingTradeWith(player: Player) =
        pendingTrades.find { it.requester == player || it.accepter == player }

    fun listAvailableToBeHandledTrades(player: Player) =
        toBeHandledTrades
            .filter {
                it.staffOption.applies(player)
            }

    fun isHandled(pendingTrade: MiddleManConfirmedTrade) =
        !toBeHandledTrades.contains(pendingTrade)

    fun onTradeWithOption(receiver: Player, sender: Player): Boolean {
        val request = pendingRequests
            .find { it.requester == sender && it.target == receiver }
            ?: return false
        acceptTradeRequest(request)
        return true
    }

    private fun acceptTradeRequest(request: MiddleManTradeRequest) {

        pendingRequests.remove(request)

        val accepter = request.target
        val requester = request.requester

        if (accepter == null || requester == null) {
            val onlinePlayer = Optional.ofNullable(accepter)
                .orElse(Optional.ofNullable(requester).orElse(null))
            onlinePlayer?.sendMessage("Could not accept trade request because "+request.getOtherName(onlinePlayer)+" is offline.")
            return
        }
        if (!donatorPinItemIds.contains(request.donatorPinId) || request.donatorPinAmount <= 0) {
            accepter.sendMessage("This trade request is no longer valid.")
            requester.sendMessage("You tried to trade an illegal item, only donator pins allowed.")
            return
        }

        val (canAccept, reasonNot) = canAcceptTrade(requester, accepter)
        if (!canAccept) {
            val message = "Both players must be $reasonNot in order to handle the trade"
            accepter.sendMessage(message)
            requester.sendMessage(message)
            return
        }

        val donatorPin = Item(request.donatorPinId, request.donatorPinAmount)

        if (!requester.inventory.containsItem(donatorPin)) {
            accepter.sendMessage("This trade request is no longer valid.")
            requester.sendMessage("You no longer have the amount of donator pins specified in the trade request in your inventory.")
            return
        }

        val deleteResult = requester.inventory.deleteItem(donatorPin)
        if (deleteResult.result == RequestResult.SUCCESS){
            val pendingTrade = MiddleManPendingTrade(requester.username, accepter.username, donatorPin, request.staffOption)
            logger.info("Added {} to pending middle man trades", pendingTrade)
            pendingTrades.add(pendingTrade)
            accepter.middleManController.onTradeRequestAccepted(pendingTrade)
            requester.middleManController.onTradeRequestAccepted(pendingTrade)
        }
    }


    /**
     * Adds the [trade] to the [toBeHandledTrades], a pending session is one that
     * has yet to be accepted/declined by a staff member. Once the [trade] is added,
     * it cannot be canceled by the players anymore.
     */
    fun addToBeHandledTrade(trade: MiddleManConfirmedTrade) {
        toBeHandledTrades.addLast(trade)
        onlineMiddleMans.values
            .filter(trade.staffOption::applies)
            .forEach { it.sendMessage(Colour.RS_PURPLE.wrap(
                "A new MM request was added to the queue, view it through ::mm")) }
        CoresManager.slowExecutor.execute(MiddleManConfirmedTrade::write)
    }

    fun handleTrade(middleMan: Player, trade: MiddleManConfirmedTrade, action: MiddleManHandleTradeAction) {
        if (toBeHandledTrades.contains(trade)) {

            val requester = trade.requester
            val accepter = trade.accepter
            if (requester == null || accepter == null) {
                middleMan.dialogue {
                    plain("Either one or both of the traders are offline, " +
                            "trades can only be handled when both traders are online.")
                }
                return
            }

            if (toBeHandledTrades.remove(trade)) {
                logger.info("Handled {} by {}, removed trade {}", action, middleMan.username, trade)

                val (offeredItemsReceiver, donatorPinReceiver)  = when (action) {
                    is MiddleManHandleTradeAction.Accept -> requester to accepter
                    is MiddleManHandleTradeAction.Decline -> accepter to requester
                }

                trade.giveOfferedItemsTo(offeredItemsReceiver)
                trade.giveDonatorPinTo(donatorPinReceiver)

                handledTrades.add(trade.toHandledTrade())

                requester.sendMessage("Your middle man trade with ${accepter.playerInformation.displayname} was ${action}ed by ${middleMan.playerInformation.displayname}.")
                accepter.sendMessage("Your middle man trade with ${requester.playerInformation.displayname} was ${action}ed by ${middleMan.playerInformation.displayname}.")

                GameLogger.log {
                    GameLogMessage.MiddleManTrade(
                        requester = trade.requesterUsername,
                        accepter = trade.accepterUsername,
                        middleman = middleMan.name,
                        requesterDonatorPin = trade.requesterDonatorPin,
                        accepterItems = trade.accepterContainer.items,
                        accepterOSRSMillions = trade.accepterOSRSMillions,
                    )
                }
                CoresManager.slowExecutor.execute(MiddleManConfirmedTrade::write)
            } else {
                logger.error("Failed to handle {} by {}, could not remove trade {} from pending trades {}", action, middleMan.username, trade, toBeHandledTrades)
                middleMan.dialogue {
                    plain("Failed to remove the trade from `pendingTrades`, " +
                            "the handling is canceled, please contact a developer.")
                }
            }
        } else {
            middleMan.dialogue {
                plain("Trade is no longer in `toBeHandledTrades` collection, " +
                        "this could be because someone else already handled it, " +
                        "if you think this is a mistake, please contact a developer.")
            }
        }
    }


    internal fun canAcceptTrade(player: Player?, player1: Player?): Pair<Boolean, String> {

        val players = listOfNotNull(player, player1)

        if (players.size != 2)
            return false to "online"

        if (players.any { it.isUnderCombat })
            return false to "out of combat"

        if (players.any { GlobalAreaManager.getArea(it.position)?.isWildernessArea(it.position) == true })
            return false to "outside of the wilderness"

        if (players.any { FlowerPokerManager.get(it).let { it.accepted || it.started || it.inInterface || it.isStaking } })
            return false to "out of a flower poker session"

        if (players.any { it.duel?.inDuel() == true })
            return false to "out of a duel session"

        return true to ""
    }
}
