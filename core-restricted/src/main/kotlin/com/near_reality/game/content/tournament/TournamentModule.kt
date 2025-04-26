package com.near_reality.game.content.tournament

import com.google.common.eventbus.Subscribe
import com.near_reality.game.content.shop.ShopCurrencyHandler
import com.near_reality.game.content.tournament.area.TournamentFightArea
import com.near_reality.game.content.tournament.area.TournamentLobbyArea
import com.near_reality.game.content.tournament.area.randomFightingWaitAreaLocation
import com.near_reality.game.content.tournament.npc.TournamentGuardHomeNpc
import com.near_reality.game.content.tournament.preset.TournamentPreset
import com.near_reality.game.content.tournament.spectating.TournamentSpectatorUpdateHook
import com.near_reality.game.plugin.administratorCommand
import com.near_reality.game.plugin.developerCommand
import com.near_reality.game.plugin.optionsMenu
import com.near_reality.game.plugin.seniorModeratorCommand
import com.near_reality.game.world.entity.player.onLogin
import com.near_reality.game.world.hook
import com.zenyte.game.GameConstants
import com.zenyte.game.model.shop.ShopCurrency
import com.zenyte.game.packet.out.FreeCam
import com.zenyte.game.task.WorldTasksManager
import com.zenyte.game.world.World
import com.zenyte.game.world.entity.player.Player
import com.zenyte.game.world.entity.player.dialogue.dialogue
import com.zenyte.game.world.entity.player.dialogue.options
import com.zenyte.plugins.events.ServerLaunchEvent
import kotlin.time.Duration.Companion.minutes

/**
 * Tournament module for registering global hooks.
 *
 * @author Stan van der Bend
 */
@Suppress("unused")
object TournamentModule {

    @JvmStatic
    @Subscribe
    fun onServerLaunchEvent(event: ServerLaunchEvent) {
        event.worldThread.hook(TournamentManager)
        event.worldThread.hook(TournamentSpectatorUpdateHook)
        registerCommands()
        TournamentGuardHomeNpc().spawn()
        TournamentLobbyArea::class.onLogin(::movePlayerAndRestoreState)
        TournamentFightArea::class.onLogin(::movePlayerAndRestoreState)
    }

    private fun movePlayerAndRestoreState(player: Player) {
        removeOverlaysAndResetState(player)
        disableSpectatorMode(player)
    }

    private fun disableSpectatorMode(player: Player) {
        player.isHidden = false
        player.send(FreeCam(false))
        player.packetDispatcher.sendClientScript(2070, 0)
        player.packetDispatcher.sendClientScript(2221, 1)
    }

    private fun registerCommands() {
        developerCommand("tt") { _, _ ->
            if (GameConstants.WORLD_PROFILE.isPublic())
                return@developerCommand
            TournamentController.Global.getTournamentIfActive()?.run {
                World.getPlayers().take(20).forEach {
                    it.teleport(lobby.randomFightingWaitAreaLocation)
                }
                WorldTasksManager.schedule(5) {
                    TournamentController.Global.start()
                }
            }
        }
        seniorModeratorCommand("starttournament") { player, _ ->
            player.optionsMenu(TournamentPreset.entries) { preset ->
                player.dialogueManager.finish()
                player.sendInputInt("Enter minutes till start of the tournament.") { duration ->
                    player.dialogueManager.finish()
                    TournamentManager.schedule(preset, duration.minutes)
                }
            }
        }
        administratorCommand("tournaments") { player, _ ->
            player.optionsMenu(TournamentManager.listActiveTournaments()) { tournament ->
                val controller = TournamentManager.getController(tournament)
                if (controller == null) {
                    player.sendMessage("No controller found for tournament.")
                    return@optionsMenu
                }
                player.dialogue {
                    options {
                        "Teleport to Lobby" {
                            tournament.lobby.teleportPlayer(player)
                        }
                        if (tournament.state is TournamentState.Scheduled) {
                            "Start" {
                                controller.start()
                            }
                        }
                    }
                }
            }
        }
        administratorCommand("tpoints") { player, args ->
            val amount = args.getOrNull(0)?.toIntOrNull()?:return@administratorCommand
            ShopCurrencyHandler.add(ShopCurrency.TOURNAMENT_POINTS, player, amount)
            player.sendMessage("You have received $amount tournament points.")
        }
    }
}
