package com.near_reality.game.content.tournament.spectating

import com.near_reality.game.content.tournament.tournamentPairSpectating
import com.zenyte.game.GameInterface
import com.zenyte.game.model.ui.Interface
import com.zenyte.game.model.ui.InterfacePosition
import com.zenyte.game.model.ui.PaneType
import com.zenyte.game.packet.out.FreeCam
import com.zenyte.game.packet.out.IfOpenTop
import com.zenyte.game.task.WorldTasksManager.schedule
import com.zenyte.game.util.AccessMask
import com.zenyte.game.world.entity.player.Player
import com.zenyte.game.world.entity.player.Player.StopType.*
import com.zenyte.game.world.entity.player.dialogue.dialogue

internal const val deadman_spectator_enable_script_id = 2070
internal const val set_renderself_script_id = 2221

/**
 * @author Kris | 04/06/2019 23:06
 * @see [Rune-Server profile](https://www.rune-server.ee/members/kris/)
 */
@Suppress("unused")
class TournamentSpectatingInterface : Interface() {

    override fun attach() {
    }

    override fun open(player: Player) {
        val tournamentPair = player.tournamentPairSpectating?:return
        if (!tournamentPair.canSpectate()){
            player.dialogue { plain("You cannot spectate this fight.") }
            return
        }
        player.setLocation(tournamentPair.spectatorLocation.copy())
        player.isHidden = true
        player.stop(INTERFACES, ROUTE_EVENT, WALK, ACTIONS, ANIMATIONS, WORLD_MAP)
        player.lock()
        player.freeze(Int.MAX_VALUE)
        player.send(IfOpenTop(PaneType.GAME_SCREEN))
        with(player.interfaceHandler) {
            sendInterface(id, 3, PaneType.GAME_SCREEN, false)
            visible.forcePut(pane.id shl 16 or InterfacePosition.CENTRAL.getComponent(pane), id)
            schedule({
                if (isPresent(GameInterface.TOURNAMENT_SPECTATING))
                    player.send(FreeCam(true))
            })
        }
        with(player.packetDispatcher) {
            sendClientScript(deadman_spectator_enable_script_id, 2)
            sendComponentSettings(id, 56, -1, 28, AccessMask.CLICK_OP10)
            sendComponentSettings(id, 57, -1, 28, AccessMask.CLICK_OP10)
            sendComponentVisibility(id, 60, true) //hide sigils
        }
    }

    override fun build() {
    }

    override fun getInterface(): GameInterface =
        GameInterface.TOURNAMENT_SPECTATING
}
