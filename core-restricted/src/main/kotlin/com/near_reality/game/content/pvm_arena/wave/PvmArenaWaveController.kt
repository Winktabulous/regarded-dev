package com.near_reality.game.content.pvm_arena.wave

import com.near_reality.game.content.pvm_arena.PvmArenaManager
import com.near_reality.game.content.pvm_arena.PvmArenaState
import com.near_reality.game.content.pvm_arena.PvmArenaTeam
import com.near_reality.game.content.pvm_arena.npc.PvmArenaNpc
import kotlinx.datetime.Clock
import org.slf4j.LoggerFactory
import kotlin.time.Duration.Companion.seconds

/**
 * Represents the wave controller for the PvM Arena activity,
 * which manages a sub controller for each team.
 *
 * @author Stan van der Bend
 */
internal class PvmArenaWaveController(
    private val waveProducers: List<PvmArenaWave.Config>,
) {
    private val logger = LoggerFactory.getLogger(this::class.java)

    private var teamBlueWaveController: PvmArenaTeamWaveController? = null
    private var teamRedWaveController: PvmArenaTeamWaveController? = null

    /**
     * Resets both team's wave controllers.
     */
    fun reset() {
        logger.info("Resetting both team's wave controllers...")
        teamBlueWaveController?.reset()
        teamRedWaveController?.reset()
        teamBlueWaveController = PvmArenaTeamWaveController(PvmArenaTeam.Blue, ArrayDeque(waveProducers))
        teamRedWaveController = PvmArenaTeamWaveController(PvmArenaTeam.Red, ArrayDeque(waveProducers))
    }

    /**
     * Cycles both team's wave controllers.
     */
    fun cycle() {
        val teamBlueWaveController = teamBlueWaveController
        val teamRedWaveController = teamRedWaveController
        if (teamBlueWaveController == null || teamRedWaveController == null)
            error(
                "Either or both team's wave controllers are null, " +
                        "this should never happen ($teamBlueWaveController, $teamRedWaveController)"
            )
        val stateBlue = teamBlueWaveController.cycle()
        val stateRed = teamRedWaveController.cycle()
        logger.info("Cycled both team's wave controllers (blue=$stateBlue, red=$stateRed)")
        val newArenaState = when {
            stateBlue is State.FinishedAllWaves && stateRed is State.FinishedAllWaves -> PvmArenaState.Ended.Tie
            stateBlue is State.FinishedAllWaves -> PvmArenaState.Ended.WonBy(PvmArenaTeam.Blue)
            stateRed is State.FinishedAllWaves -> PvmArenaState.Ended.WonBy(PvmArenaTeam.Red)
            stateBlue is State.Reset || stateRed is State.Reset -> {
                if (stateBlue is State.Reset && stateRed is State.Reset)
                    PvmArenaState.Ended.Canceled
                else
                    PvmArenaState.Ended.WonBy(if (stateBlue is State.Reset) PvmArenaTeam.Red else PvmArenaTeam.Blue)
            }
            else -> return
        }
        PvmArenaManager.changeState(newArenaState)
    }

    /**
     * Handles the death of an [npc] by delegating it to the appropriate team's wave controller.
     */
    fun onNpcDeath(npc: PvmArenaNpc) {
        val waveController = findWaveControllerForTeam(npc.config.team)
            ?: error("No wave controller found for npc team ${npc.config.team} for npc $npc")
        waveController.onNpcDeath(npc)
    }

    private fun findWaveControllerForTeam(pvmArenaTeam: PvmArenaTeam) = when (pvmArenaTeam) {
        PvmArenaTeam.Blue -> teamBlueWaveController
        PvmArenaTeam.Red -> teamRedWaveController
    }

    private class PvmArenaTeamWaveController(
        private val team: PvmArenaTeam,
        val waveProducers: ArrayDeque<PvmArenaWave.Config>,
    ) {
        private val logger = LoggerFactory.getLogger("PvmArenaWaveController(team=$team)")
        private var state: State = State.Waiting

        /**
         * Clears [future waves][waveProducers] and cancels the current wave if active.
         */
        fun reset() {
            waveProducers.clear()
            when (val currentState = state) {
                is State.ActiveWave -> {
                    logger.info("$currentState - resetting current wave...")
                    currentState.wave.cancel()
                }
                else -> logger.info("$currentState - no waves to reset.")
            }
            state = State.Reset
        }

        /**
         * Cycles the current wave controller state.
         */
        fun cycle() : State {

            if (state is State.Reset)
                return state

            if (team.isEmpty()) {
                logger.debug("Team is empty, resetting wave...")
                reset()
            }

            when (val currentState = state) {
                is State.Waiting -> spawnNextWave()
                is State.ActiveWave -> {
                    logger.info("$currentState - cycling current wave...")
                    val wave = currentState.wave
                    val waveState = wave.cycle()
                    if (waveState is PvmArenaWave.State.Finished)
                        state = State.FinishedWave()
                }
                is State.FinishedWave -> {
                    when {
                        waveProducers.isEmpty() -> {
                            logger.info("$currentState - no more waves to spawn.")
                            state = State.FinishedAllWaves
                        }
                        currentState.isReadyToSpawnNext() -> spawnNextWave()
                        else -> logger.info("$currentState - spawning next wave in ${currentState.timeUntilNextWave}.")
                    }
                }
                else -> logger.info("$currentState - no more waves to cycle.")
            }
            return state
        }

        /**
         * Handles the death of an [npc] by delegating it to the current wave.
         */
        fun onNpcDeath(npc: PvmArenaNpc) {
            when (val currentState = state) {
                is State.ActiveWave -> currentState.wave.onNpcDeath(npc)
                else -> logger.warn("$currentState - ignoring npc death $npc")
            }
        }

        private fun spawnNextWave() {
            logger.info("$state - spawning next wave...")
            val waveConfig = waveProducers.removeFirst()
            val wave = PvmArenaWave(team, waveConfig)
            state = State.ActiveWave(wave)
        }
    }

    private sealed class State {

        /**
         * Represents the state where the wave controller is waiting for the next wave to spawn.
         */
        data object Waiting : State()

        /**
         * Represents the state where the wave controller is actively running a wave.
         */
        data class ActiveWave(val wave: PvmArenaWave) : State()

        /**
         * Represents the state where the wave controller has finished running a wave.
         */
        class FinishedWave : State() {

            val durationUntilNextWave = 10.seconds
            val finishTime = Clock.System.now()
            val nextWaveTime = finishTime + durationUntilNextWave
            val timeUntilNextWave get() = nextWaveTime - Clock.System.now()

            fun isReadyToSpawnNext() = Clock.System.now() > nextWaveTime
        }

        /**
         * Represents the state where the wave controller has finished running all waves.
         */
        data object FinishedAllWaves : State()

        /**
         * Represents the state where the wave controller has been reset.
         */
        data object Reset : State()
    }
}
