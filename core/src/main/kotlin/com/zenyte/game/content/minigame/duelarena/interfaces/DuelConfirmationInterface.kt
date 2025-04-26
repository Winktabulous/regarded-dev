package com.zenyte.game.content.minigame.duelarena.interfaces

import com.zenyte.game.GameInterface
import com.zenyte.game.content.minigame.duelarena.DuelStage
import com.zenyte.game.model.ui.Interface
import com.zenyte.game.world.entity.player.Player
import java.util.*

/**
 * @author Tommeh | 27-10-2018 | 20:14
 * @see [Rune-Server profile](https://www.rune-server.ee/members/tommeh/)}
 */
class DuelConfirmationInterface : Interface() {

    override fun attach() {
        put(78, "Confirm")
        put(80, "Decline")
    }

    override fun open(player: Player) {
        player.interfaceHandler.sendInterface(getInterface())
    }

    override fun close(player: Player, replacement: Optional<GameInterface>) {
        player.duel?.close(true)
    }

    override fun build() {
        bind("Confirm") { player: Player ->
            if (!player.inArea("Duel Arena")) {
                return@bind
            }
            player.duel?.confirm(DuelStage.CONFIRMATION)
        }
        bind("Decline") { player: Player ->
            if (!player.inArea("Duel Arena")) {
                return@bind
            }
            player.duel?.close(true)
        }
    }

    override fun getInterface() = GameInterface.DUEL_CONFIRMATION
}