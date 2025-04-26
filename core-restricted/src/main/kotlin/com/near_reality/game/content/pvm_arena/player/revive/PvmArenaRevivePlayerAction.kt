package com.near_reality.game.content.pvm_arena.player.revive

import com.zenyte.game.world.entity.masks.Animation
import com.zenyte.game.world.entity.player.Action
import com.zenyte.game.world.entity.player.Player

/**
 * Represents an [Action] that handles the reviving of a player,
 * the action is not interrupted by incoming combat and is only stopped when the player is out of reach,
 * or when the player stops reviving by engaging in combat, switching items, or doing some other action.
 *
 * @author Stan van der Bend
 */
class PvmArenaRevivePlayerAction : Action() {

    /**
     * The player being revived by the [player performing the action][player].
     */
    private val playerBeingRevived: Player?
        get() = player.pvmArenaReviveState.let {
            if (it is PvmArenaReviveState.Reviving)
                it.playerBeingRevived
            else
                null
        }

    /**
     * Checks if the [playerBeingRevived] is within [MAX_DISTANCE] of the [player].
     */
    private fun inReachOfPlayerBeingRevived() = playerBeingRevived?.location?.withinDistance(player, MAX_DISTANCE)?:false

    override fun start(): Boolean = inReachOfPlayerBeingRevived()

    override fun process(): Boolean = inReachOfPlayerBeingRevived()

    /**
     * Processes the action with a delay of 2 game ticks.
     */
    override fun processWithDelay(): Int {
        val playerBeingRevived = playerBeingRevived?:return -1
        player.faceEntity(playerBeingRevived)
        player.forceAnimation(REVIVE_ANIMATION)
        return ACTION_CYCLE_DURATION_IN_TICKS
    }

    override fun interruptedByCombat(): Boolean =
        false

    override fun stop() {
        cancel(player)
    }

    companion object {

        private const val MAX_DISTANCE = 1
        private const val ACTION_CYCLE_DURATION_IN_TICKS = 2
        private val REVIVE_ANIMATION = Animation(7118)

        /**
         * Cancels the reviving action of the [revivingPlayer].
         *
         * @return `true` if the reviving action was cancelled, `false` if the [revivingPlayer] was not reviving.
         */
        internal fun cancel(revivingPlayer: Player): Boolean {
            val myState = revivingPlayer.pvmArenaReviveState
            if (myState is PvmArenaReviveState.Reviving) {
                revivingPlayer.pvmArenaReviveState = PvmArenaReviveState.None
                val playerBeingRevived = myState.playerBeingRevived
                if (playerBeingRevived == null) {
                    revivingPlayer.sendDeveloperMessage("The player being revived is null.")
                    return true
                }
                when(val otherState = playerBeingRevived.pvmArenaReviveState) {
                    is PvmArenaReviveState.BeingRevived -> {
                        revivingPlayer.sendMessage("You have stopped reviving ${playerBeingRevived.username}.")
                        otherState.removeRevivingPlayer(revivingPlayer)
                    }
                    else ->
                        revivingPlayer.sendDeveloperMessage("This player is not being revived. (state: $otherState)")
                }
                return true
            } else
                return false
        }
    }
}
