package com.near_reality.game.content.pvm_arena.player.revive

import com.near_reality.game.content.pvm_arena.player.revive.PvmArenaRevivePlayerActionPlugin.Companion.addOption
import com.near_reality.game.content.pvm_arena.player.revive.PvmArenaRevivePlayerActionPlugin.Companion.removeOption
import com.near_reality.game.world.entity.player.PlayerActionPlugin
import com.zenyte.game.world.entity.player.Player

/**
 * Represents a [PlayerActionPlugin] that binds the "Revive" player option.
 * See the companion object for [enabling][addOption] and [disabling][removeOption] the "Revive" option.
 *
 * @author Stan van der Bend
 */
@Suppress("unused")
class PvmArenaRevivePlayerActionPlugin : PlayerActionPlugin() {

    override fun handle() {
        bind("Revive") { player, other ->
            revivePlayer(player, other)
            true
        }
    }

    private fun revivePlayer(revivingPlayer: Player, playerBeingRevived: Player) {
        val myReviveState = revivingPlayer.pvmArenaReviveState
        when (myReviveState) {
            PvmArenaReviveState.None -> {
                when(val targetReviveState = playerBeingRevived.pvmArenaReviveState){
                    is PvmArenaReviveState.BeingRevived, PvmArenaReviveState.Down -> {
                        val revivingState = if (targetReviveState is PvmArenaReviveState.BeingRevived) {
                            targetReviveState
                        } else
                            PvmArenaReviveState.BeingRevived().also { playerBeingRevived.pvmArenaReviveState = it}
                        revivingState.addRevivingPlayer(revivingPlayer)
                        revivingPlayer.pvmArenaReviveState = PvmArenaReviveState.Reviving(playerBeingRevived)
                        revivingPlayer.actionManager.setAction(PvmArenaRevivePlayerAction())
                        playerBeingRevived.sendMessage("You are being revived by ${revivingPlayer.username}.")
                        revivingPlayer.sendMessage("You are reviving ${playerBeingRevived.username}.")
                    }
                    else -> revivingPlayer.sendMessage("This player is not downed.")
                }
            }
            is PvmArenaReviveState.Reviving -> {
                revivingPlayer.sendMessage("You are already reviving another player.")
                return
            }
            else -> {
                revivingPlayer.sendMessage("You are downed and cannot revive other players.")
                return
            }
        }
    }

    internal companion object {

        /**
         * Adds the "Revive" player option to the [player].
         */
        fun addOption(player: Player) =
            player.setPlayerOption(1, "Revive", false)

        /**
         * Removes the "Revive" player option from the [player].
         */
        fun removeOption(player: Player) =
            player.setPlayerOption(1, null, false)
    }
}
