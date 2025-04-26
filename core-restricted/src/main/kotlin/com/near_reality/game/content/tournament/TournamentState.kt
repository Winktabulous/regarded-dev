package com.near_reality.game.content.tournament

import com.near_reality.game.content.tournament.area.TournamentFightArea
import com.near_reality.game.util.WorldTimer
import com.runespawn.util.weakReference
import com.zenyte.game.world.entity.player.Player
import java.util.*
import kotlin.time.Duration.Companion.minutes
import kotlin.time.Duration.Companion.seconds

sealed class TournamentState {

    class Scheduled(val startTimer: WorldTimer) : TournamentState()

    sealed class Ongoing(val round: Int) : TournamentState() {
    }

    class RoundActive(
        val fightArea: TournamentFightArea,
        round: Int,
        val combatPairs: Queue<TournamentPair>,
        unpairedPlayer: Player? = null
    ) : Ongoing(round) {
        var unpairedPlayer by weakReference(unpairedPlayer)
            private set
        var started = false
        val expirationTimer = WorldTimer(10.minutes)
        val countdownTimer = WorldTimer(10.seconds)
        val playersInCombat : Set<Player> get() =
            combatPairs.flatMap { setOf(it.first, it.second) }.filterNotNull().toSet()

        fun removeUnpairedPlayerIfEquals(player: Player): Boolean {
            if (unpairedPlayer == player) {
                unpairedPlayer = null
                return true
            } else
                return false
        }
    }

    class RoundOver(
        round: Int,
    ) : Ongoing(round) {

        val nextRoundTimer = WorldTimer(60.seconds)
    }

    sealed class Finished : TournamentState() {

        val autoDestroyRegionTimer = WorldTimer(1.minutes)

        class WonBy(winner: Player) : Finished() {
            val winner by weakReference(winner)
        }

        data object NoWinner : Finished()
    }
}
