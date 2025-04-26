package com.near_reality.game.content.tournament

import com.near_reality.game.content.tournament.area.TournamentLobbyArea
import com.near_reality.game.content.tournament.preset.TournamentPreset
import com.near_reality.game.util.WorldTimer
import com.near_reality.game.util.formattedString
import com.near_reality.util.collection.refillPoolOf
import com.zenyte.game.GameInterface
import com.zenyte.game.world.World
import com.zenyte.game.world.entity.player.MessageType
import com.zenyte.game.world.entity.player.Player
import com.zenyte.game.world.region.dynamicregion.MapBuilder
import org.slf4j.LoggerFactory
import kotlin.time.Duration
import kotlin.time.Duration.Companion.hours
import kotlin.time.Duration.Companion.minutes
import kotlin.time.Duration.Companion.seconds

private const val CONTINUE_PROCESSING = true
private const val STOP_PROCESSING = false

sealed class TournamentController {

    private val logger = LoggerFactory.getLogger(this::class.java)
    private val announcementTimes = listOf(15.minutes, 10.minutes, 5.minutes, 1.minutes)
    private lateinit var tournament: Tournament

    abstract fun nextPreset(): TournamentPreset

    abstract fun durationUntilStart(): Duration

    abstract fun autoScheduleNext(): Boolean

    abstract fun minimumPlayers(): Int

    fun isControlling(tournament: Tournament): Boolean =
        this::tournament.isInitialized && this.tournament == tournament

    fun getTournamentIfActive(): Tournament? =
        if (this::tournament.isInitialized && tournament.state !is TournamentState.Finished) tournament else null

    fun process(): Boolean {
        if (!this::tournament.isInitialized)
            schedule()
        when (val state = tournament.state) {
            is TournamentState.Scheduled -> {
                val tournamentStartTimer = state.startTimer
                val tournamentPreset = tournament.preset
                updateBroadcastPeriodically(tournamentStartTimer, tournamentPreset)
                updateTournamentOverlay(tournamentStartTimer, tournamentPreset)
                if (tournamentStartTimer.elapsed())
                    start()
            }
            is TournamentState.RoundActive -> {
                val playersInCombat = state.playersInCombat
                if (!state.started) {
                    val countdownTimer = state.countdownTimer
                    val countdownTicks = countdownTimer.ticksRemaining().toInt()
                    updateCountdownAboveHeadPeriodically(countdownTimer, playersInCombat, countdownTicks)
                    if (countdownTimer.elapsed()) {
                        playersInCombat.forEach { player -> player.setForceTalk("FIGHT!") }
                        state.started = true
                    }
                }
                val expirationTimer = state.expirationTimer
                val remainingCombatPairs = state.combatPairs.toMutableSet()
                updateTournamentOverlay(expirationTimer, tournament.preset)
                notifyPlayerOfFightExpirationPeriodically(expirationTimer, playersInCombat)
                if (remainingCombatPairs.isEmpty())
                    tournament.endRound(false)
                else if (expirationTimer.elapsed()) {
                    tournament.endRound(true)
                    tournament.logger.warn("Tournament {} round {} has expired.", state.round, tournament)
                }
            }
            is TournamentState.RoundOver -> {
                val nextRoundTimer = state.nextRoundTimer
                val remainingParticipants = tournament.participants.toSet()
                updateTournamentOverlay(nextRoundTimer, tournament.preset)
                when {
                    remainingParticipants.isEmpty() -> tournament.cancel("as there are no participants remaining.")
                    remainingParticipants.size == 1 -> tournament.win(remainingParticipants.first())
                    nextRoundTimer.elapsed() -> tournament.tryStartNextRound()
                }
            }
            is TournamentState.Finished -> {
                if (autoScheduleNext()) {
                    logger.info("Auto-scheduling next tournament.")
                    schedule()
                } else {
                    tournament.logger.info("The tournament has finished!")
                    return STOP_PROCESSING
                }
            }
        }
        return CONTINUE_PROCESSING
    }

    private fun notifyPlayerOfFightExpirationPeriodically(
        expirationTimer: WorldTimer,
        playersInCombat: Set<Player>,
    ) {
        expirationTimer.every(1.minutes) {
            playersInCombat.forEach { player ->
                player.sendTournamentMessage("You have ${expirationTimer.durationRemaining().formattedString} left to finish your fight.")
            }
        }
    }

    private fun updateCountdownAboveHeadPeriodically(
        countdownTimer: WorldTimer,
        playersInCombat: Set<Player>,
        countdownTicks: Int,
    ) {
        countdownTimer.every(1.seconds) {
            playersInCombat.forEach { player ->
                player.interfaceHandler.closeInterface(GameInterface.TOURNAMENT_OVERLAY)
                GameInterface.WILDERNESS_OVERLAY.open(player)
                player.setForceTalk(countdownTicks.toString())
            }
        }
    }

    private fun updateBroadcastPeriodically(
        timer: WorldTimer,
        preset: TournamentPreset,
    ) {
        timer.ifDurationRemainingIsExactlyOneOf(announcementTimes) {
            World.sendMessage(
                MessageType.GLOBAL_BROADCAST,
                buildString {
                    append(preset.announcementIcon)
                    append(preset)
                    append(" tournament starts in ${it.formattedString}, enter via blue portal west of home!")
                }
            )
        }
    }

    private fun updateTournamentOverlay(
        timer: WorldTimer,
        preset: TournamentPreset,
    ) {
        val tournamentLobby = tournament.lobby
        val tournamentParticipantsCount = tournament.countPlayersParticipating()
        val tournamentRound = tournament.currentRound()
        tournamentLobby.players.forEach { player ->
            GameInterface.TOURNAMENT_OVERLAY.open(player)
            player.packetDispatcher.sendComponentText(GameInterface.TOURNAMENT_OVERLAY.id, 8, "Preset: $preset")
            player.tournamentTimeInTicks = timer.ticksRemaining().toInt()
            player.tournamentPlayerCount = tournamentParticipantsCount
            player.tournamentRound = tournamentRound
        }
    }

    private fun schedule() {
        try {
            val preset = nextPreset()
            val durationUntilStart = durationUntilStart()
            tournament = Tournament(preset, WorldTimer(durationUntilStart))
            tournament.lobby = TournamentLobbyArea(
                allocatedArea = MapBuilder.findEmptyChunk(64, 64),
                tournament = tournament
            ).apply {
                constructRegion()
            }
            logger.info("Scheduled tournament with preset: $preset")
        } catch (e: Exception) {
            logger.error("Failed to schedule tournament", e)
        }
    }

    internal fun start() {
        when (tournament.state) {
            is TournamentState.Scheduled -> {
                logger.info("Starting tournament {}", tournament)
                tournament.tryStartNextRound()
            }
            else -> {
                logger.warn("Tournament {} already started", tournament)
            }
        }
    }

    data object Global : TournamentController() {

        private val presets = refillPoolOf(*TournamentPreset.entries.filter { it.includeInAutoSchedulePool }.toTypedArray())

        override fun nextPreset(): TournamentPreset =
            presets.poll()?: error("No presets available for scheduling.")

        override fun durationUntilStart(): Duration =
            2.hours

        override fun autoScheduleNext(): Boolean =
            CONTINUE_PROCESSING

        override fun minimumPlayers(): Int =
            2
    }

    data class Manual(
        private val preset: TournamentPreset,
        private val durationUntilStart: Duration
    ) : TournamentController() {

        override fun nextPreset(): TournamentPreset =
            preset

        override fun durationUntilStart(): Duration =
            durationUntilStart

        override fun autoScheduleNext(): Boolean =
            false

        override fun minimumPlayers(): Int =
            2
    }
}
