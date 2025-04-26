package com.near_reality.game.content.pvm_arena.player.revive

import com.near_reality.game.world.entity.player.pvmArenaInRevivalState
import com.near_reality.game.world.entity.player.pvmArenaPoints
import com.near_reality.game.world.entity.player.pvmArenaRevivalCount
import com.runespawn.util.weakMutableSetOf
import com.runespawn.util.weakReference
import com.zenyte.game.util.Colour
import com.zenyte.game.world.WorldThread
import com.zenyte.game.world.entity.masks.UpdateFlag
import com.zenyte.game.world.entity.player.Player
import com.zenyte.utils.TimeUnit
import kotlin.math.ceil
import kotlin.math.roundToInt

private const val REVIVE_PVM_ARENA_POINTS_REWARD = 15
private const val REVIVE_TIME_IN_TICKS = 5 // at most 10 ticks, due to skull icon
private const val MAX_REVIVE_TIME_IN_TICKS = 10

/**
 * Represents the [PvmArenaReviveState] of a player being revived or a player reviving someone.
 *
 * @author Stan van der Bend
 */
sealed class PvmArenaReviveState(val canBeAttacked: Boolean) {

    /**
     * Represents the state of a player that is not reviving or being revived (default).
     */
    data object None : PvmArenaReviveState(true)

    /**
     * Represents the state of a player that is currently downed and can be revived.
     */
    data object Down : PvmArenaReviveState(false)

    /**
     * Represents the state of a player that is down and is currently being revived.
     */
    class BeingRevived : PvmArenaReviveState(false) {

        private val playersReviving = weakMutableSetOf<Player>()
        private var ticksUntilUp = REVIVE_TIME_IN_TICKS

        /**
         * Processes the revival of the [playerBeingRevived] by the players currently reviving them.
         */
        internal fun processRevive(playerBeingRevived: Player): Result {
            playersReviving.removeIf { !it.isReviving(playerBeingRevived) }
            if (playersReviving.isEmpty())
                return Result.NoPlayers
            val decrement = ceil(playersReviving.size / 2.0).roundToInt().coerceIn(1..3)
            ticksUntilUp -= decrement
            return when {
                ticksUntilUp < 0 -> {
                    playersReviving.forEach { playerReviving ->
                        playerReviving.pvmArenaReviveState = None
                        val ticksSinceLastRewards = WorldThread.getCurrentCycle() - playerReviving.pvmArenaLastReviveRewardTick
                        val timeSinceLastRewards = TimeUnit.TICKS.toSeconds(ticksSinceLastRewards)
                        if (timeSinceLastRewards >= 60 || ++playerReviving.pvmArenaReviveCountDuringCooldown < 3) {
                            playerReviving.pvmArenaReviveCountDuringCooldown = 0
                            playerReviving.pvmArenaLastReviveRewardTick = WorldThread.getCurrentCycle()
                            playerReviving.pvmArenaPoints += REVIVE_PVM_ARENA_POINTS_REWARD
                            playerReviving.sendMessage("You have successfully revived ${playerBeingRevived.username} and received ${Colour.RS_RED.wrap(
                                REVIVE_PVM_ARENA_POINTS_REWARD
                            )} PvM Arena points.")
                        } else
                            playerReviving.sendMessage("You have successfully revived ${playerBeingRevived.username}.")
                    }
                    Result.Success
                }
                else -> Result.InProcess((MAX_REVIVE_TIME_IN_TICKS - ticksUntilUp).coerceIn(0..MAX_REVIVE_TIME_IN_TICKS))
            }
        }

        /**
         * Adds a [player] to the list of players that are currently reviving the player being revived.
         */
        internal fun addRevivingPlayer(player: Player) =
            playersReviving.add(player)

        /**
         * Removes a [player] from the list of players that are currently reviving the player being revived.
         */
        internal fun removeRevivingPlayer(player: Player) =
            playersReviving.remove(player)

        private fun Player.isReviving(other: Player): Boolean {
            val myState = pvmArenaReviveState
            return myState is Reviving && myState.playerBeingRevived == other
        }

        /**
         * Represents the result of a player being revived, evaluated after each game tick.
         *
         * @see PvmArenaDownedPlayerWorldTask for the evaluation of the [BeingRevived] state.
         */
        internal sealed class Result {

            /**
             * Represents the result of a successful revival.
             */
            data object Success : Result()

            /**
             * Represents the result of a revival that is still in process, taking [counter] more ticks to complete.
             */
            data class InProcess(val counter: Int) : Result()

            /**
             * Represents the result of a revival that has been cancelled due to no players reviving the player.
             */
            data object NoPlayers : Result()
        }
    }

    /**
     * Represents the state of a player that is currently reviving another player.
     */
    class Reviving(playerBeingRevived: Player) : PvmArenaReviveState(true) {
        val playerBeingRevived by weakReference(playerBeingRevived)
    }

    internal companion object {

        /**
         * Checks if the [player] is in a state where they are downed and send an optional [messageOnDown].
         * If the [player] is in a state where they are reviving another player,
         * the reviving action is [cancelled][PvmArenaRevivePlayerAction.cancel].
         *
         * @return `true` if the [player] is downed, `false` otherwise.
         */
        fun isDownOrCancelActionAndFalse(player: Player, messageOnDown: String? = null) = when (player.pvmArenaReviveState) {
            is BeingRevived,
            is Down
            -> {
                messageOnDown?.let(player::sendMessage)
                true
            }
            is None -> false
            is Reviving -> {
                PvmArenaRevivePlayerAction.cancel(player)
                false
            }
        }

        /**
         * Clears the revival state of the [player], resetting their appearance and flags.
         */
        fun clear(player: Player) {
            player.pvmArenaReviveState = None
            player.pvmArenaRevivalCount = 0
            player.pvmArenaInRevivalState = false
            player.appearance.resetRenderAnimation()
            player.updateFlags.flag(UpdateFlag.APPEARANCE)
        }
    }
}
