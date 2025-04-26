package com.near_reality.game.content.pvm_arena.player

import com.near_reality.game.content.pvm_arena.PvmArenaManager.state
import com.near_reality.game.content.pvm_arena.PvmArenaState
import com.near_reality.game.content.pvm_arena.PvmArenaTeam
import com.zenyte.game.world.entity.player.Player
import com.zenyte.game.world.entity.player.varbit

/**
 * Represents the number of kills the team of this player has in the PvM Arena.
 */
internal var Player.pvmArenaMyTeamKills by varbit(4820)

/**
 * Represents the number of kills the opposing team has in the PvM Arena.
 */
internal var Player.pvmArenaOpposingTeamKills by varbit(4821)

/**
 * Represents the time left in minutes for the PvM Arena.
 */
internal var Player.pvmArenaTimeInMinutes by varbit(4822)

/**
 * Updates the player's PvM Arena varbits based on the current state of the PvM Arena.
 */
internal fun updatePvmArenaVarbits(player: Player) {
    when(val state = state) {
        is PvmArenaState.Ended.WonBy,
        is PvmArenaState.Idle,
        is PvmArenaState.Open,
        is PvmArenaState.StartingSoon
        -> {
            player.pvmArenaMyTeamKills = 0
            player.pvmArenaOpposingTeamKills = 0
            player.pvmArenaTimeInMinutes = Int.MAX_VALUE // expected by cs2
        }
        is PvmArenaState.Started,
        is PvmArenaState.Ended
        -> {
            val playerTeam = PvmArenaTeam.findTeamContaining(player) ?: return
            val otherTeam = playerTeam.otherTeam
            player.pvmArenaMyTeamKills = playerTeam.killCount
            player.pvmArenaOpposingTeamKills = otherTeam.killCount
            player.pvmArenaTimeInMinutes = if (state is PvmArenaState.Started)
                state.timeLeft.inWholeMinutes.toInt()
            else
                0
        }
    }
}
