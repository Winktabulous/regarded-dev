package com.near_reality.game.content.pvm_arena.player.revive

import com.near_reality.game.world.entity.player.pvmArenaInRevivalState
import com.near_reality.game.world.entity.player.pvmArenaRevivalCount
import com.runespawn.util.weakReference
import com.zenyte.game.task.WorldTask
import com.zenyte.game.task.WorldTasksManager
import com.zenyte.game.world.entity.masks.*
import com.zenyte.game.world.entity.player.Player

/**
 * Represents a [WorldTask] that handles the downed state of a [player].
 *
 * @author Stan van der Bend
 */
class PvmArenaDownedPlayerWorldTask private constructor(player: Player) : WorldTask {

    private val player by weakReference(player)
    private var cycle = 0

    override fun run() {
        val player = player
        if (player == null) {
            stop()
            return
        }
        when(val myState = player.pvmArenaReviveState) {
            is PvmArenaReviveState.BeingRevived -> handleBeingRevived(myState, player)
            PvmArenaReviveState.Down -> handleNotBeingRevived(player)
            else -> {
                stop()
            }
        }
    }

    private fun handleBeingRevived(
        myState: PvmArenaReviveState.BeingRevived,
        player: Player,
    ) {
        when (val result = myState.processRevive(player)) {
            is PvmArenaReviveState.BeingRevived.Result.InProcess -> {
                player.pvmArenaRevivalCount = result.counter
                player.updateFlags.flag(UpdateFlag.APPEARANCE)
            }
            is PvmArenaReviveState.BeingRevived.Result.NoPlayers -> {
                player.pvmArenaReviveState = PvmArenaReviveState.Down
                player.pvmArenaRevivalCount = 0
                player.updateFlags.flag(UpdateFlag.APPEARANCE)
            }
            is PvmArenaReviveState.BeingRevived.Result.Success -> {
                player.pvmArenaReviveState = PvmArenaReviveState.None
                player.pvmArenaRevivalCount = 0
                player.appearance.resetRenderAnimation()
                player.reset()
            }
        }
    }

    private fun handleNotBeingRevived(player: Player) {
        if (++cycle % DOWN_DAMAGE_INTERVAL_IN_TICKS == 0)
            player.applyHit(Hit(DOWN_DAMAGE_AMOUNT, HitType.DEFAULT))
    }

    override fun stop() {
        super.stop()
        PvmArenaReviveState.clear(player ?: return)
    }

    companion object {

        private const val DOWN_DAMAGE_INTERVAL_IN_TICKS = 5
        private const val DOWN_DAMAGE_AMOUNT = 5

        private val DOWN_RENDER_ANIMATION = RenderAnimation(4758, 4759, 4759, 4759, 4759, 4759, 4759)

        /**
         * Schedules a [PvmArenaDownedPlayerWorldTask] for the [player].
         */
        internal fun schedule(player: Player) {
            player.isRun = false
            player.pvmArenaReviveState = PvmArenaReviveState.Down
            player.pvmArenaRevivalCount = 0
            player.pvmArenaInRevivalState = true
            player.sendMessage("You have been downed. You will bleed out once your HP reaches 0.")
            player.forceAnimation(Animation(4167))
            player.appearance.renderAnimation = DOWN_RENDER_ANIMATION
            player.setHitpoints(player.maxHitpoints)
            WorldTasksManager.schedule(PvmArenaDownedPlayerWorldTask(player), periodDelay = 1)
        }
    }
}
