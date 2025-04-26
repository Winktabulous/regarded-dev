package com.near_reality.game.world.entity.player.dialogue

import com.zenyte.game.model.ui.InterfacePosition
import com.zenyte.game.model.ui.PaneType
import com.zenyte.game.world.entity.player.Player
import com.zenyte.game.world.entity.player.dialogue.Message

/**
 * Represents a [dialogue message][Message] that creates a loading bar in the chat box.
 *
 * @author Stan van der Bend
 */
class LoadingMessage(
    private val loadingText: String
) : Message {

    override fun display(player: Player) {
        player.interfaceHandler.run {
            sendInterface(InterfacePosition.DIALOGUE, 293)
            sendInterface(663, 2, PaneType.LOADING_CHATBOX, true)
        }
        player.packetDispatcher.run {
            sendClientScript(2379)
            sendClientScript(4217, 19202050, 19202049, 3787, 10, 1000, 3000, 512, 1024, 0, 3, loadingText)
        }
    }
}
