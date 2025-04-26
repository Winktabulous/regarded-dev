package com.near_reality.game.content.middleman.trade.handle

import com.near_reality.game.content.middleman.MiddleManManager
import com.near_reality.game.content.middleman.middleManConfirmedTradeListView
import com.near_reality.game.content.middleman.trade.MiddleManConfirmedTrade
import com.zenyte.game.GameInterface
import com.zenyte.game.model.ui.Interface
import com.zenyte.game.model.ui.InterfacePosition
import com.zenyte.game.task.WorldTasksManager
import com.zenyte.game.world.entity.player.Player

/**
 * Represents an interface staff members can open (using `::mm`) to view pending middle-man trades.
 * Within this interface staff members can accept or decline trades awaiting staff confirmation.
 *
 * @author Stan van der Bend
 */
@Suppress("UNUSED")
class MiddleManHandleTradeInterface : Interface() {

    override fun attach() {
        put(3, "Refresh")
        put(9, "Player 1 Offer")
        put(10, "Player 2 Offer")
        put(11, "Status")
        put(21, "Logs")
    }

    override fun open(player: Player) {

        player.interfaceHandler.sendInterface(InterfacePosition.CENTRAL, `interface`.id, false)

        player.packetDispatcher.sendComponentText(1604, getComponent("Status"), "Status")

        populatePendingTradeListView(player)

        // have to delay this by a tick or else it gets auto reset by the interface handling mechanism.
        WorldTasksManager.schedule {
            player.temporaryAttributes["interfaceInput"] = MiddleManHandleTradeDialogue()
        }
    }

    override fun build() {
        bind("Refresh", this::populatePendingTradeListView)
        bind("Logs", GameInterface.MIDDLE_MAN_HISTORY::open)
    }

    override fun getInterface() = GameInterface.MIDDLE_MAN_MONITORING

    private fun populatePendingTradeListView(player: Player) {

        val sessions: List<MiddleManConfirmedTrade> = MiddleManManager.listAvailableToBeHandledTrades(player)

        if (sessions.isEmpty())
            player.packetDispatcher.sendClientScript(10547, 3, 0, "")
        else {
            val tradeIterator = sessions.iterator()

            var flag = if (sessions.size == 1) 3 else 1
            var index = 0
            while (tradeIterator.hasNext()) {
                val trade: MiddleManConfirmedTrade = tradeIterator.next()
                player.packetDispatcher.sendClientScript(10547, flag, index++, trade.toInterfaceString())
                flag = if (tradeIterator.hasNext()) 2 else 0
            }
        }
        player.middleManConfirmedTradeListView = sessions
    }
}
