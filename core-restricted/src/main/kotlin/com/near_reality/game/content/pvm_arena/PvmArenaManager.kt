package com.near_reality.game.content.pvm_arena

import com.near_reality.game.content.pvm_arena.PvmArenaManager.state
import com.near_reality.game.content.pvm_arena.area.PvmArenaFightArea
import com.near_reality.game.content.pvm_arena.area.PvmArenaLobbyArea
import com.near_reality.game.content.pvm_arena.npc.PvmArenaNpc
import com.near_reality.game.content.pvm_arena.npc.PvmArenaNpcHealthBarHudManager
import com.near_reality.game.content.pvm_arena.player.revive.PvmArenaRevivePlayerActionPlugin
import com.near_reality.game.content.pvm_arena.player.revive.PvmArenaReviveState
import com.near_reality.game.content.pvm_arena.player.updatePvmArenaVarbits
import com.near_reality.game.content.pvm_arena.wave.PvmArenaWaveController
import com.near_reality.game.content.pvm_arena.wave.pvmArenaWaveConfig
import com.near_reality.game.util.inWholeTicks
import com.near_reality.game.world.entity.player.pvmArenaAppearanceBeardOffset
import com.near_reality.game.world.entity.player.pvmArenaMvpCountDuringGame
import com.near_reality.game.world.entity.player.pvmArenaPoints
import com.near_reality.game.world.entity.player.pvmArenaPointsGainedDuringGame
import com.zenyte.game.GameInterface
import com.zenyte.game.item.Item
import com.zenyte.game.item.ItemId
import com.zenyte.game.task.WorldTask
import com.zenyte.game.task.WorldTasksManager
import com.zenyte.game.util.Colour
import com.zenyte.game.util.Colour.RS_RED
import com.zenyte.game.world.World
import com.zenyte.game.world.broadcasts.BroadcastType
import com.zenyte.game.world.broadcasts.WorldBroadcasts
import com.zenyte.game.world.entity.masks.UpdateFlag
import com.zenyte.game.world.entity.npc.NpcId
import com.zenyte.game.world.entity.player.Player
import com.zenyte.game.world.entity.player.cutscene.FadeScreen
import com.zenyte.game.world.entity.player.dialogue.Dialogue
import com.zenyte.game.world.entity.player.dialogue.dialogue
import com.zenyte.utils.TimeUnit
import org.slf4j.LoggerFactory
import kotlin.time.Duration.Companion.hours
import kotlin.time.Duration.Companion.minutes

/**
 * Manages the state of the PvM Arena activity.
 *
 * @author Stan van der Bend
 */
internal object PvmArenaManager {

    private val bandageSpawnInterval = TimeUnit.MINUTES.toTicks(1L)

    /**
     * Represents the current [state][PvmArenaState] of the PvM Arena activity.
     */
    var state: PvmArenaState = PvmArenaState.Idle()
        private set

    private var stateHandlerTask: WorldTask? = null

    private val waveController = PvmArenaWaveController(waveProducers = listOf(pvmArenaWaveConfig))

    private val logger = LoggerFactory.getLogger(PvmArenaManager::class.java)

    internal fun changeState(newState: PvmArenaState) {
        stateHandlerTask?.let(WorldTasksManager::stop)
        state = newState
        waveController.reset()
        when (newState) {
            is PvmArenaState.Idle -> {
                logger.info("$newState - doing nothing.")
                var previousMinutes = 0L
                var previousHours = 0L
                scheduleStateHandlerTask(tickCyclePeriod = 0) { tick ->
                    if (newState.isOpening())
                        changeState(PvmArenaState.Open())
                    else {
                        val timeTillOpen = newState.timeLeft
                        val ticksTillOpen = timeTillOpen.inWholeTicks
                        if (ticksTillOpen > 1.hours.inWholeTicks) {
                            if (ticksTillOpen % 1.hours.inWholeTicks == 0L) {
                                val hoursTillOpen = timeTillOpen.inWholeHours
                                if (previousHours != hoursTillOpen && hoursTillOpen > 0) {
                                    logger.info("The PvM Arena is opening $hoursTillOpen hours.")
                                    sendWorldWideBroadcast("The PvM Arena is opening in $hoursTillOpen hours.")
                                }
                                previousHours = hoursTillOpen
                            }
                        } else if (ticksTillOpen % 10.minutes.inWholeTicks == 0L) {
                            val minutesTillOpen = timeTillOpen.inWholeMinutes
                            if (previousMinutes != minutesTillOpen && minutesTillOpen > 0) {
                                logger.info("The PvM Arena is opening $minutesTillOpen minutes.")
                                sendWorldWideBroadcast("The PvM Arena is opening in $minutesTillOpen minutes.")
                            }
                            previousMinutes = minutesTillOpen
                        }
                    }
                }
            }
            is PvmArenaState.Open -> {
                logger.info("$newState - players can join a team.")
                sendWorldWideBroadcast("The PvM Arena is now open, join a team at ::pvm!")
                PvmArenaTeam.Blue.reset()
                PvmArenaTeam.Red.reset()
                var previousMinutesStart = 0L
                scheduleStateHandlerTask(tickCyclePeriod = 0) { _ ->
                    if (newState.isStartingSoon())
                        changeState(PvmArenaState.StartingSoon())
                    else {
                        val secondsTillStart = newState.timeLeft.inWholeSeconds
                        if (secondsTillStart % 60L == 0L) {
                            val minutesTillStart = secondsTillStart / 60
                            if ((minutesTillStart == 10L || minutesTillStart == 5L || minutesTillStart == 1L) && previousMinutesStart != minutesTillStart) {
                                logger.info("The PvM Arena activity is starting in $minutesTillStart minutes.")
                                sendWorldWideBroadcast("The PvM Arena activity is starting in $minutesTillStart minutes.")
                            }
                            previousMinutesStart = minutesTillStart
                        }
                    }
                }
            }
            is PvmArenaState.StartingSoon -> {
                logger.info("$newState - countdown started.")
                sendWorldWideBroadcast("The PvM Arena activity is starting in ${newState.maxDuration}, join at ::pvm!")
                var previousSecondsStart = 0L
                scheduleStateHandlerTask(tickCyclePeriod = 0) { _ ->
                    if (newState.isStarting())
                        changeState(PvmArenaState.Started())
                    else {
                        val secondsTillStart = newState.timeLeft.inWholeSeconds
                        if (secondsTillStart % 5L == 0L) {
                            if (secondsTillStart > 0 && previousSecondsStart != secondsTillStart) {
                                logger.info("The PvM Arena activity is starting in $secondsTillStart seconds.")
                                messageEachPlayerInTeam("The PvM Arena activity is starting in $secondsTillStart seconds.")
                            }
                            previousSecondsStart = secondsTillStart
                        }
                    }
                }
            }
            is PvmArenaState.Started -> {
                if (PvmArenaTeam.Blue.isEmpty() || PvmArenaTeam.Red.isEmpty()) {
                    logger.info("The PvM Arena activity is canceled for not having enough players.")
                    changeState(PvmArenaState.Ended.Canceled)
                } else {
                    logger.info("The PvM Arena activity is starting now.")
                    messageEachPlayerInTeam("The PvM Arena activity is starting now.")
                    scheduleStateHandlerTask(tickCyclePeriod = 0) { cycle ->
                        if (newState.isTimedOut())
                            changeState(PvmArenaState.Ended.TimedOut)
                        else {
                            waveController.cycle()
                            if (cycle > 0 && cycle % bandageSpawnInterval == 0L){
                                /* Every 1 minutes drop 3 bandages for each player */
                                forEachPlayerInTeam { player ->
                                    logger.info("Spawning bandages for ${player}")
                                    World.spawnFloorItem(Item(ItemId.BANDAGES_25730, 3), player, 300, 0)
                                    player.sendMessage("You have received 3 bandages for staying alive for ${TimeUnit.TICKS.toMinutes(cycle.toLong()).coerceAtLeast(1)} minutes.")
                                }
                            }
                            forEachPlayerInTeam(::updatePvmArenaVarbits)
                        }
                    }
                }
            }
            is PvmArenaState.Ended -> {
                PvmArenaNpcHealthBarHudManager.clear()
                when(newState) {
                    is PvmArenaState.Ended.TimedOut -> {
                        logger.info("The PvM Arena activity has timed out, neither team finished in time.")
                        moveEachPlayerInTeamToLobby(reason = "Neither team finished in time.")
                    }
                    is PvmArenaState.Ended.Canceled -> {
                        logger.info("The PvM Arena activity is canceled.")
                        moveEachPlayerInTeamToLobby(reason = "The activity has been cancelled.")
                    }
                    is PvmArenaState.Ended.WonBy -> {
                        val winningTeam = newState.winningTeam
                        logger.info("The PvM Arena activity was won by team ${winningTeam}.")
                        forEachPlayerInTeam { player ->
                            messagesGameStatisticsToPlayer(player)
                            if (winningTeam.containsPlayer(player)) {
                                dialogueWhenTeamWon(player)
                                applySirEldricCombatBoost(player)
                            } else
                                dialogueWhenTeamLost(player)
                            PvmArenaLobbyArea.moveInto(player)
                            resetTemporaryPlayerState(player)
                        }
                        sendWorldWideBroadcast("The PvM Arena activity was won by team ${winningTeam.formattedName}.")
                    }
                    is PvmArenaState.Ended.Tie -> {
                        logger.info("The PvM Arena activity ended in a tie.")
                        forEachPlayerInTeam { player ->
                            messagesGameStatisticsToPlayer(player)
                        }
                        moveEachPlayerInTeamToLobby(reason = "The activity ended in a tie.")
                    }
                }
                runCatching {
                    World.getAllFloorItems()
                        .filter { PvmArenaFightArea.inAnyFightArea(it.location) }
                        .forEach { World.destroyFloorItem(it) }
                }.onFailure {
                    logger.error("Failed to destroy floor items in PvM Arena.", it)
                }
                changeState(PvmArenaState.Idle())
            }
        }
        forEachPlayerInTeam(::updatePvmArenaVarbits)
    }

    fun onNpcDeath(pvmArenaNpc: PvmArenaNpc) {
        waveController.onNpcDeath(pvmArenaNpc)
    }

    fun tryJoinTeam(player: Player, team: PvmArenaTeam) {
        if (!state.canJoinArena) {
            dialogueWhenPortalIsNotOpen(player)
            return
        }
        if (PvmArenaTeam.findTeamContaining(player) != null) {
            player.sendMessage("Not adding to team ${team.formattedName} as you are already in a team.")
            return
        }
        if (PvmArenaTeam.anyTeamContainsIp(player.ip)) {
            player.sendMessage("You can only enter PVM Arena on one ip address.")
            return
        }
        val otherTeam = team.otherTeam
        if (team.players.size > otherTeam.players.size) {
            dialogueWhenTeamsImbalanced(player)
            return
        }

        val teleportLocation = team.area.randomSpawnLocation()
        val fadeScreen = FadeScreen(player)
        fadeScreen.fade(2, true) {
            player.teleport(teleportLocation)
            replenishHealthAndPrayer(player)
        }
    }

    fun onEnterFightArea(player: Player) {
        fun Player.moveToLobbyWithMessage(message: String) {
            PvmArenaLobbyArea.moveInto(this)
            sendMessage(message)
        }
        val teamContaining = PvmArenaTeam.findTeamContaining(player)
        if (teamContaining != null) {
            player.moveToLobbyWithMessage("Not adding to team as you are already in team $teamContaining")
            return
        }
        val teamByLocation = PvmArenaTeam.findTeamByLocationOf(player)
        if (teamByLocation == null) {
            player.moveToLobbyWithMessage("Not adding to team, not in either of the team areas.")
            return
        }
        val otherTeam = teamByLocation.otherTeam
        if (teamByLocation.players.size > otherTeam.players.size) {
            player.moveToLobbyWithMessage("Not adding to team, teams are unbalanced.")
            return
        }

        player.viewDistance = Player.LARGE_VIEWPORT_RADIUS
        player.pvmArenaAppearanceBeardOffset = teamByLocation.iconItemId
        player.updateFlags.flag(UpdateFlag.APPEARANCE)
        teamByLocation.addPlayer(player)
        GameInterface.PVM_ARENA_HUD.open(player)
        PvmArenaRevivePlayerActionPlugin.addOption(player)
        updatePvmArenaVarbits(player)
    }

    fun onLeaveFightArea(player: Player) {
        if (state !is PvmArenaState.Ended) {
            PvmArenaTeam.findTeamContaining(player)?.removePlayer(player)
            resetTemporaryPlayerState(player)
            updatePvmArenaVarbits(player)
        } else
            player.sendDeveloperMessage("Not handling leave as it's handled by the PvM Arena Manager task.")
    }

    private fun scheduleStateHandlerTask(
        ticksTillStart: Int = WorldTasksManager.DEFAULT_INITIAL_DELAY,
        tickCyclePeriod: Int = WorldTasksManager.NO_PERIOD_DELAY,
        taskWithCycle: (Int) -> Unit,
    ) {
        var cycle = 0
        val worldTask = WorldTask { taskWithCycle(cycle++) }
        WorldTasksManager.schedule(worldTask, ticksTillStart, tickCyclePeriod)
        this.stateHandlerTask = worldTask
    }
}

private fun replenishHealthAndPrayer(player: Player) {
    player.heal(99)
    player.prayerManager.restorePrayerPoints(99)
}

private fun applySirEldricCombatBoost(player: Player) {
    player.variables.pvmArenaBoosterTick = (1.hours.inWholeMilliseconds / 600).toInt()
    player.sendMessage(Colour.RS_PURPLE.wrap("Sir Eldric boosted your combat against monsters by 5% for 1 hour."))
}

private fun resetTemporaryPlayerState(player: Player) {
    PvmArenaRevivePlayerActionPlugin.removeOption(player)
    PvmArenaReviveState.clear(player)
    PvmArenaNpcHealthBarHudManager.removeHud(player)
    replenishHealthAndPrayer(player)
    player.interfaceHandler.closeInterface(GameInterface.PVM_ARENA_HUD)
    player.viewDistance = Player.SMALL_VIEWPORT_RADIUS
    player.pvmArenaAppearanceBeardOffset = 0
    player.pvmArenaPointsGainedDuringGame = 0
    player.pvmArenaMvpCountDuringGame = 0
    player.updateFlags.flag(UpdateFlag.APPEARANCE)
}

private fun sendWorldWideBroadcast(message: String): Unit =
    WorldBroadcasts.sendMessage(message, BroadcastType.PVM_ARENA, true)

private fun moveEachPlayerInTeamToLobby(reason: String? = null) {
    forEachPlayerInTeam { player ->
        PvmArenaLobbyArea.moveInto(player)
        resetTemporaryPlayerState(player)
        if (reason != null)
            player.sendMessage(reason)
    }
}

private fun messageEachPlayerInTeam(message: String): Unit =
    forEachPlayerInTeam { it.sendMessage(message) }

private fun forEachPlayerInTeam(action: (Player) -> Unit): Unit =
    (PvmArenaTeam.Blue.players + PvmArenaTeam.Red.players).forEach(action)

private fun messagesGameStatisticsToPlayer(player: Player) {
    player.sendMessage("You gained ${RS_RED.wrap(player.pvmArenaPointsGainedDuringGame.toString())} PvM Arena points this game, you now have a total of ${RS_RED.wrap(player.pvmArenaPoints.toString())} points.")
    player.sendMessage("You were the most valuable player ${RS_RED.wrap(player.pvmArenaMvpCountDuringGame.toString())} times this game.")
}

/* Sir Eldrick Dialogues */

private fun dialogueWhenPortalIsNotOpen(player: Player) {
    player.dialogue(NpcId.GHOST_3516) {
        if (state is PvmArenaState.Idle) {
            sirEldricDialogue(
                "Stay thy steps, noble warrior! ",
                "The arena gates are currently sealed and the spirits of battle rest."
            )
            sirEldricDialogue(
                "Keep watch for an announcement to know when next you can test your might and mettle."
            )
        } else {
            sirEldricDialogue(
                "Halt, brave adventurer! The path you seek to tread cannot be taken at this moment.",
                "The arena is on the brink of battle, and the teams are already assembled."
            )
            sirEldricDialogue(
                "I must ask you to wait for the next opportunity to prove your valor. ",
                "Keep watch for an announcement to know when next you can test your might and mettle."
            )
        }
    }
}

private fun dialogueWhenTeamsImbalanced(player: Player) {
    player.dialogue(NpcId.GHOST_3516) {
        sirEldricDialogue(
            "Halt, brave adventurer! The path you seek to tread cannot be taken at this moment.",
            "The team you wish to join already boasts a greater number of warriors than its rival."
        )
        sirEldricDialogue(
            "I must ask you to join the opposing team, to ensure the scales of competition remain balanced.",
            "Seek the portal that leads to the lesser assembled forces and prove your valor there."
        )
    }
}


private fun dialogueWhenTeamLost(it: Player) {
    it.dialogue(NpcId.GHOST_3516) {
        sirEldricDialogue(
            "Alas, noble warrior,",
            "our team has been defeated in the PvM Arena."
        )
    }
}

private fun dialogueWhenTeamWon(it: Player) {
    it.dialogue(NpcId.GHOST_3516) {
        sirEldricDialogue(
            "Congratulations, brave warrior!",
            "Your team has emerged victorious in the PvM Arena.",
            "As a reward for your valor, you have been granted a temporary boost to your combat prowess."
        )
    }
}

private fun Dialogue.sirEldricDialogue(vararg messages: String) =
    npc("Sir Eldric", messages.joinToString("<br>"))
