package com.near_reality.game.content.middleman.trade.history

import com.near_reality.game.content.middleman.MiddleManManager
import com.near_reality.game.content.middleman.middleManHandledTradeListView
import com.near_reality.game.content.middleman.trade.MiddleManHandledTrade
import com.zenyte.game.GameInterface
import com.zenyte.game.model.ui.Interface
import com.zenyte.game.model.ui.InterfacePosition
import com.zenyte.game.task.WorldTasksManager
import com.zenyte.game.util.AccessMask
import com.zenyte.game.world.entity.player.Player
import java.time.LocalDate
import java.time.format.TextStyle
import java.util.*

/**
 * Represents an interface staff members can open (using `::mm`) to view pending middle-man trades.
 * Within this interface staff members can accept or decline trades awaiting staff confirmation.
 *
 * @author Stan van der Bend
 */
@Suppress("UNUSED")
class MiddleManTradeHistoryInterface : Interface() {

    override fun attach() {
        put(6, "MM Home")
        put(4, "List")
    }

    override fun open(player: Player) {

        player.interfaceHandler.sendInterface(InterfacePosition.CENTRAL, `interface`.id, false)

        val currentDate = LocalDate.now()
        val tradeHistoryMap = mutableMapOf<String, List<MiddleManHandledTrade>>()
        for ((dateString, handledTradeList) in MiddleManManager.handledTrades
            .filter {
                it.handledDate.monthValue == currentDate.monthValue &&
                        it.handledDate.year == currentDate.year
            }
            .groupBy {
                val month = it.handledDate.month.getDisplayName(TextStyle.FULL, Locale.US)
                val year = it.handledDate.year
                "${it.handledDate.dayOfMonth} - $month - $year"
            }
            .toSortedMap(Comparator.reverseOrder())
        ) {
            handledTradeList.chunked(MAX_ENTRIES).let { handledTradeListChunks ->
                val multiple = handledTradeListChunks.size > 1
                handledTradeListChunks.reversed().forEachIndexed { index, handledTrades ->
                    val textKey = if (multiple)
                        "$dateString (${index * MAX_ENTRIES}-${(index + 1) * MAX_ENTRIES})"
                    else
                        dateString
                    tradeHistoryMap[textKey] = handledTrades.asReversed()
                }
            }
        }
        val entriesShown = tradeHistoryMap.keys.take(MAX_ENTRIES)

        val data = entriesShown.joinToString("|")
        player.packetDispatcher.run {
            sendClientScript(10562, entriesShown.size, data)
            sendComponentSettings(
                1605, getComponent("List"), 0, entriesShown.size * 3,
                AccessMask.CLICK_OP1
            )
        }
        player.middleManHandledTradeListView = entriesShown.map { tradeHistoryMap[it]!! }
        // have to delay this by a tick or else it gets auto reset by the interface handling mechanism.
        WorldTasksManager.schedule {
            player.temporaryAttributes["interfaceInput"] = MiddleManTradeHistoryDialogue()
        }
    }

    override fun build() {
        bind("MM Home", GameInterface.MIDDLE_MAN_MONITORING::open)
        bind("List") { player, slotId, _, _ ->
            player.sendDeveloperMessage("$slotId")
            var tradeIndex = slotId - player.middleManHandledTradeListView.size
            if (tradeIndex > 0)
                tradeIndex /= 2
            val tradeList = player.middleManHandledTradeListView[tradeIndex]
            GameInterface.MIDDLE_MAN_MONITORING.open(player)

            WorldTasksManager.schedule {
                player.packetDispatcher.sendComponentText(1604, 11, "Day - H:M:S")
            }

            val tradeIterator = tradeList.iterator()
            var flag = if (tradeList.size == 1) 3 else 1
            var index = 0
            while (tradeIterator.hasNext()) {
                val trade = tradeIterator.next()
                player.packetDispatcher.sendClientScript(10547, flag, index++, buildString {
                    append(trade.toInterfaceString())
                    append('|')
                    append(trade.handledDate.let {
                        "${it.dayOfMonth} - ${it.hour}:${it.minute}:${it.second}"
                    })
                })
                flag = if (tradeIterator.hasNext()) 2 else 0
            }
        }
    }

    override fun getInterface() = GameInterface.MIDDLE_MAN_HISTORY

    private companion object {
        const val MAX_ENTRIES = 100
    }
}
