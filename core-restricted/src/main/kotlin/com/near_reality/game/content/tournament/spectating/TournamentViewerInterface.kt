package com.near_reality.game.content.tournament.spectating

import com.near_reality.game.content.tournament.TournamentState
import com.near_reality.game.content.tournament.area.TournamentLobbyArea
import com.near_reality.game.content.tournament.tournamentPairSpectating
import com.near_reality.game.content.tournament.tournamentPairsAvailableToSpectate
import com.near_reality.game.content.tournament.tournamentSpectatorLocation
import com.zenyte.GameToggles
import com.zenyte.game.GameInterface
import com.zenyte.game.model.ui.Interface
import com.zenyte.game.packet.PacketDispatcher
import com.zenyte.game.util.AccessMask
import com.zenyte.game.world.entity.player.Player
import com.zenyte.game.world.entity.player.SkillConstants

/**
 * @author Tommeh | 02/06/2019 | 21:35
 * @see [Rune-Server profile](https://www.rune-server.ee/members/tommeh/)
 */
class TournamentViewerInterface : Interface() {

    override fun attach() {
        put(4, "View Fight")
        put(8, "Players")
        put(9, "Round")
        put(10, "Refresh")
    }

    override fun open(player: Player) {
        player.interfaceHandler.sendInterface(this)
        refresh(player)
        player.tournamentSpectatorLocation = player.location
    }

    override fun build() {
        bind("Refresh") { player: Player -> this.refresh(player) }
        bind("View Fight") { player: Player, slotId: Int, _: Int, _: Int ->
            if (GameToggles.TOURNAMENT_SPECTATING_DISABLED) {
                player.sendMessage("Due to a bug, this has been temporarily disabled.")
                return@bind
            }
            val pairs = player.tournamentPairsAvailableToSpectate!!.stream().toList()
            val pair = pairs[slotId / 2] // or null
            if (pair == null) {
                player.sendMessage("That fight has already ended!")
                return@bind
            }
            player.tournamentPairSpectating = pair
            player.interfaceHandler.closeInterfaces()
            pair.addSpectator(player)
            GameInterface.TOURNAMENT_SPECTATING.open(player)
        }
    }

    private fun refresh(player: Player) {
        val tournament = ( player.area as? TournamentLobbyArea)?.tournament?:return
        val tournamentState = tournament.state
        val pairsString = if (tournamentState is TournamentState.RoundActive)
            tournamentState.combatPairs.joinToString("|")
        else
            ""
        with(player.packetDispatcher) {
            sendClientScript(10610, pairsString)
            sendComponentText(getInterface(), getComponent("Round"), tournament.currentRound())
            sendComponentText(getInterface(), getComponent("Players"), tournament.countPlayersParticipating())
            sendComponentSettings(getInterface(), getComponent("View Fight"), 0, tournament.countPlayersFighting(), AccessMask.CLICK_OP1)
        }
    }

    override fun getInterface(): GameInterface =
        GameInterface.TOURNAMENT_VIEWER

    companion object {
        fun refreshSpectator(spectator: Player) {
            val pairObject = spectator.tournamentPairSpectating ?: return
            val left = pairObject.first?:return
            val right = pairObject.second?:return
            fun PacketDispatcher.sendPlayerStats(player: Player, index: Int) {
                sendClientScript(
                    2180 + index,
                    player.hitpoints,
                    player.maxHitpoints,
                    player.prayerManager.prayerPoints,
                    player.skills.getLevelForXp(SkillConstants.PRAYER),
                    player.combatDefinitions.specialEnergy * 10,
                    0,
                    0,
                    0,
                    index,
                    player.name,
                    player.skills.combatLevel.toString()
                )
            }
            with(spectator.packetDispatcher) {
                sendUpdateItemContainer(93, -70001, 0, left.inventory.container)
                sendUpdateItemContainer(611, -1, 0, right.inventory.container)
                sendPlayerStats(left, 0)
                sendPlayerStats(right, 1)
            }
        }
    }
}
