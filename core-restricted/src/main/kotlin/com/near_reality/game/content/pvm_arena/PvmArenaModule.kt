package com.near_reality.game.content.pvm_arena

import com.google.common.eventbus.Subscribe
import com.near_reality.game.content.pvm_arena.PvmArenaModule.notifyPlayerOfCombatBoostTimeLeft
import com.near_reality.game.content.pvm_arena.PvmArenaModule.registerCommands
import com.near_reality.game.content.pvm_arena.PvmArenaModule.spawnObjects
import com.near_reality.game.content.pvm_arena.area.PvmArenaLobbyArea
import com.near_reality.game.content.pvm_arena.loc.PvmArenaTeamPortalPlugin
import com.near_reality.game.content.pvm_arena.player.spawnFakePlayers
import com.near_reality.game.content.pvm_arena.wave.*
import com.near_reality.game.item.CustomObjectId
import com.zenyte.game.GameConstants
import com.zenyte.game.util.Colour
import com.zenyte.game.world.World
import com.zenyte.game.world.entity.Location
import com.zenyte.game.world.entity.player.GameCommands
import com.zenyte.game.world.entity.player.Player
import com.zenyte.game.world.entity.player.dialogue.OptionsBuilder
import com.zenyte.game.world.entity.player.dialogue.dialogue
import com.zenyte.game.world.entity.player.dialogue.options
import com.zenyte.game.world.entity.player.privilege.PlayerPrivilege
import com.zenyte.game.world.`object`.ObjectId
import com.zenyte.game.world.`object`.WorldObject
import com.zenyte.plugins.events.LoginEvent
import com.zenyte.plugins.events.ServerLaunchEvent
import kotlin.time.Duration
import kotlin.time.Duration.Companion.milliseconds

/**
 * Registers hooks for server launch and player login events relevant to the PvM Arena activity.
 *
 * @see notifyPlayerOfCombatBoostTimeLeft   Notifies the player of the remaining time of the combat boost.
 * @see spawnObjects                        Spawns the PvM Arena objects in the world.
 * @see registerCommands                    Registers the `::pvm` command to teleport into the PvM Arena Lobby,
 *                                          and the `::managepvm` command for staff to manage the PvM Arena Activity.
 *
 * @author Stan van der Bend
 */
@Suppress("unused", "UNUSED_PARAMETER")
object PvmArenaModule {

    @Subscribe
    @JvmStatic
    fun onPlayerLoginEvent(event: LoginEvent) {
        val player = event.player?:return
        notifyPlayerOfCombatBoostTimeLeft(player)
    }

    private fun notifyPlayerOfCombatBoostTimeLeft(player: Player) {
        val boostTimeLeft = (player.variables.pvmArenaBoosterTick * 600L).milliseconds
        if (boostTimeLeft > Duration.ZERO)
            player.sendMessage(Colour.RS_PURPLE.wrap("You have ${boostTimeLeft.inWholeMinutes} minutes of Sir Eldric's combat boost left."))
    }

    @Subscribe
    @JvmStatic
    fun onServerLaunchEvent(event: ServerLaunchEvent) {
        spawnObjects()
        registerCommands()
    }

    private fun spawnObjects() {
        World.spawnObject(WorldObject(id = ObjectId.BANK_CHEST, tile = Location(1761, 4705, 0)))
        World.spawnObject(WorldObject(id = ObjectId.ALTAR_OF_THE_OCCULT, 10, 2, Location(1762, 4698, 0)))
        World.spawnObject(WorldObject(id = CustomObjectId.REJUVINATION_POOL, 10, 1, Location(1758, 4699, 0)))
        World.spawnObject(WorldObject(id = PvmArenaTeamPortalPlugin.TEAM_BLUE_PORTAL_ID, tile = Location(1763, 4705, 0)))
        World.spawnObject(WorldObject(id = PvmArenaTeamPortalPlugin.TEAM_RED_PORTAL_ID, tile = Location(1759, 4705, 0)))
    }

    private fun registerCommands() {
        GameCommands.Command(PlayerPrivilege.PLAYER, "pvm") { p, _ ->
            PvmArenaLobbyArea.teleportInto(p)
        }
        GameCommands.Command(PlayerPrivilege.ADMINISTRATOR, "managepvm") { p, _ ->
            openManagementDialogue(p)
        }
        GameCommands.Command(PlayerPrivilege.DEVELOPER, "pvmsim") { p, args ->
            val amount = args.getOrNull(0)?.toIntOrNull() ?: 10
            if (GameConstants.WORLD_PROFILE.isPublic())
                p.sendMessage("This command is only available in the development environment.")
            else
                spawnFakePlayers(p, amount)
        }
    }

    private fun openManagementDialogue(initiatingStaffMember: Player): Unit = initiatingStaffMember.dialogue {
        fun OptionsBuilder.cancelOption(initiatingStaffMember: Player) = "Cancel the PvM Arena activity" {
            initiatingStaffMember.dialogue {
                plain("Are you sure?<br>This will teleport players back to the lobby.")
                options("Cancel the PvM Arena activity") {
                    "Yes, cancel the PvM Arena activity" {
                        PvmArenaManager.changeState(PvmArenaState.Ended.Canceled)
                    }
                }
            }
        }
        fun OptionsBuilder.openOption(initiatingStaffMember: Player) = "Open the PvM Arena activity" {
            PvmArenaManager.changeState(PvmArenaState.Open())
            initiatingStaffMember.dialogue {
                plain(
                    "You have opened up the PvM Arena Lobby,<br>players can join a team now."
                ).executeAction {
                    openManagementDialogue(initiatingStaffMember)
                }
            }
        }
        fun OptionsBuilder.balanceOption(initiatingStaffMember: Player) = "Open the Balancer" {

            fun OptionsBuilder.modifyFunctionComponent(
                name: String,
                getter: () -> Double,
                setter: (Double) -> Unit,
            ) {
                fun Player.awaitInputDouble() {
                    dialogueManager.finish()

                    sendInputString("Set $name (=${getter()})") { input ->
                        val newValue = input.toDoubleOrNull()
                        if (newValue == null) {
                            initiatingStaffMember.dialogue {
                                plain(Colour.RS_RED.wrap("Invalid input, please enter a number.")).executeAction {
                                    awaitInputDouble()
                                }
                            }
                        } else {
                            setter(newValue)
                            openManagementDialogue(initiatingStaffMember)
                        }
                    }
                }
                dialogueOption("$name = ${getter()}", noPlayerMessage = true) { initiatingStaffMember.awaitInputDouble() }
            }
            initiatingStaffMember.dialogue {
                plain(Colour.RS_RED.wrap("Please be careful modifying, try out the resulting function first!"))
                options("Choose function to balance") {
                    "Max health modifier function" {
                        initiatingStaffMember.options("(a * x^4) + (b * x^3) + (c * x^2) + (d * x ) + 1.0") {
                            modifyFunctionComponent("a", { p1 }) { p1 = it }
                            modifyFunctionComponent("b", { p2 }) { p2 = it }
                            modifyFunctionComponent("c", { p3 }) { p3 = it }
                            modifyFunctionComponent("d", { p4 }) { p4 = it }
                        }
                    }
                    "Max NPC alive at a time function" {
                        initiatingStaffMember.options("(a * x^4) + (b * x^3) + (c * x^2) + (d * x ) + 1.0") {
                            modifyFunctionComponent("a", { s1 }) { s1 = it }
                            modifyFunctionComponent("b", { s2 }) { s2 = it }
                            modifyFunctionComponent("c", { s3 }) { s3 = it }
                            modifyFunctionComponent("d", { s4 }) { s4 = it }
                        }
                    }
                    "NPC spawn interval function" {
                        initiatingStaffMember.options("(a / x) + b") {
                            modifyFunctionComponent("a", { t1 }) { t1 = it }
                            modifyFunctionComponent("b", { t2 }) { t2 = it }
                        }
                    }
                    "PVM Arena Points" {
                        initiatingStaffMember.options("PVM Arena Points") {
                            modifyFunctionComponent("MVP Multiplier", { MVP_BONUS_PVM_ARENA_POINTS }) { MVP_BONUS_PVM_ARENA_POINTS = it.coerceIn(0.001..1.0) }
                            modifyFunctionComponent("Base Multiplier", { PVM_ARENA_POINTS_MODIFIER }) { PVM_ARENA_POINTS_MODIFIER = it.coerceIn(0.001..2.0) }
                        }
                    }
                    "Nevermind"()
                }
            }
        }
        when (PvmArenaManager.state) {
            is PvmArenaState.Idle -> {
                options("The PvM Arena is currently idle") {
                    openOption(initiatingStaffMember)
                    balanceOption(initiatingStaffMember)
                    "Nevermind"()
                }
            }
            is PvmArenaState.Open -> {
                options("The PvM Arena is currently open") {
                    "Start the PVM Arena timer (lobby closes after 30s)" {
                        PvmArenaManager.changeState(PvmArenaState.StartingSoon())
                        openManagementDialogue(initiatingStaffMember)
                    }
                    balanceOption(initiatingStaffMember)
                    cancelOption(initiatingStaffMember)
                }
            }
            is PvmArenaState.StartingSoon -> {
                options("The PvM Arena is starting soon") {
                    "Force start the activity" {
                        PvmArenaManager.changeState(PvmArenaState.Started())
                        openManagementDialogue(initiatingStaffMember)
                    }
                    balanceOption(initiatingStaffMember)
                    cancelOption(initiatingStaffMember)
                }
            }
            is PvmArenaState.Started -> {
                options("The PvM Arena is active") {
                    "Force team left win" {
                        PvmArenaManager.changeState(PvmArenaState.Ended.WonBy(PvmArenaTeam.Blue))
                    }
                    "Force team right win" {
                        PvmArenaManager.changeState(PvmArenaState.Ended.WonBy(PvmArenaTeam.Red))
                    }
                    balanceOption(initiatingStaffMember)
                    cancelOption(initiatingStaffMember)
                }
            }
            is PvmArenaState.Ended -> {
                options("The PvM Arena is ended") {
                    openOption(initiatingStaffMember)
                    balanceOption(initiatingStaffMember)
                    "Nevermind"()
                }
            }
        }
    }
}
