package com.near_reality.game.content.middleman.trade.history

import com.zenyte.game.world.entity.player.Player
import com.zenyte.plugins.dialogue.CountDialogue

/**
 * Represents a [CountDialogue] that opens up when a staff member clicks a searched for middle-man trade.
 *
 * @author Stan van der Bend
 */
class MiddleManTradeHistoryDialogue : CountDialogue {

    override fun run(index: Int) = Unit

    override fun execute(player: Player, amount: Int) {

        val tradeIndex = amount.div(65536)
        val buttonIndex = amount.minus(tradeIndex * 65536)

        player.sendDeveloperMessage("$amount -> ($tradeIndex, $buttonIndex)")
    }
}
