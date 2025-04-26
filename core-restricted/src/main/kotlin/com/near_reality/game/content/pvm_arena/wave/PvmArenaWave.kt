package com.near_reality.game.content.pvm_arena.wave

import com.near_reality.game.content.pvm_arena.PvmArenaTeam
import com.near_reality.game.content.pvm_arena.npc.PvmArenaNpcSpawnGroup
import com.near_reality.game.content.pvm_arena.npc.PvmArenaNpc
import com.near_reality.game.world.entity.player.pvmArenaMvpCountDuringGame
import com.near_reality.game.world.entity.player.pvmArenaPoints
import com.near_reality.game.world.entity.player.pvmArenaPointsGainedDuringGame
import com.runespawn.util.weakMutableSetOf
import com.zenyte.game.util.Colour.RS_RED
import com.zenyte.game.world.entity.npc.NPC
import com.zenyte.game.world.entity.player.Player
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import org.slf4j.LoggerFactory
import kotlin.time.Duration

var MVP_BONUS_PVM_ARENA_POINTS = 0.05 // extra points rewarded as cb_level * this
var PVM_ARENA_POINTS_MODIFIER = 0.5

/**
 * Represents a wave of npcs in the PvM Arena activity,
 * each npc in [npcProducerQueue] is spawned after the previous has been defeated.
 * When all npcs have been spawned and defeated, this wave is finished.
 *
 * @author Stan van der Bend
 */
class PvmArenaWave(
    private val team: PvmArenaTeam,
    private val waveConfig: Config,
) {
    private val logger = LoggerFactory.getLogger("PvmArenaWave(team=${team})")

    private var state: State = State.Waiting
    private val npcProducerQueue = ArrayDeque(waveConfig.npcSpawns.toList())

    /**
     * Cycles the wave, spawning npcs when ready.
     */
    fun cycle(): State {
        when (val state = state) {
            is State.Waiting -> {
                logger.info("$state - spawning first npc.")
                trySpawnNextNpcOrFinish()
            }
            is State.Cancelled -> logger.warn("$state - not spawning any more npcs.")
            is State.Exception -> logger.error("$state - not spawning any more npcs.", state.throwable)
            is State.Active -> when {
                npcProducerQueue.isEmpty() -> {
                    if (state.npcGroupCount() != 0)
                        logger.info("$state - waiting for ${state.npcGroupCount()} more npcs to be defeated before finishing.")
                    else {
                        logger.info("$state - finishing because no more npcs to spawn.")
                        this.state = State.Finished
                    }
                }
                state.isReadyToSpawnNext(waveConfig) -> {
                    logger.info("$state - spawning next npc...")
                    trySpawnNextNpcOrFinish()
                }
                state.hasMaxNpcSpawnedAtATime(waveConfig) -> {
                    logger.info("$state - max npcs spawned at a time cap reached, postponing spawning next npc.")
                    state.resetLastSpawnTime()
                }
                else -> logger.info("$state - spawning next npc in ${state.getTimeUntilNextSpawnNPC(waveConfig)}.")
            }
            is State.Finished -> logger.info("$state -> doing nothing")
        }
        return state
    }

    /**
     * Handles the death of a npc, awarding points to players, advancing the wave if necessary.
     */
    fun onNpcDeath(npc: PvmArenaNpc) = runCatchingWithStateChange { currentState ->
        if (currentState is State.Active) {
            logger.info("$state - progressed wave by killing npc $npc")
            npc as NPC
            team.killCount++
            val maxDamageDealerPlayerName = fetchMostDamageDealerForNpc(npc)
            team.players.forEach { player ->
                val isMvp = player.username == maxDamageDealerPlayerName
                val damageDoneByPlayer = fetchDamageDoneByPlayerForNpc(npc, player)
                val totalDamageDoneByPlayer = damageDoneByPlayer.sumOf { it.damage }
                val percentageDamageDoneByPlayer = if (totalDamageDoneByPlayer <= 0)
                    0.0
                else
                    npc.maxHitpoints.toDouble().div(totalDamageDoneByPlayer).coerceAtMost(0.10)
                val pointsReceivedByPlayer = (PVM_ARENA_POINTS_MODIFIER * percentageDamageDoneByPlayer * npc.combatLevel).toInt()
                if (pointsReceivedByPlayer > 0) {
                    player.pvmArenaPoints += pointsReceivedByPlayer
                    player.pvmArenaPointsGainedDuringGame += pointsReceivedByPlayer
                    player.sendMessage("You received ${RS_RED.wrap(pointsReceivedByPlayer)} PvM Arena points for defeating ${RS_RED.wrap(npc.name)}.")
                    if (isMvp) {
                        val mvpPointsBonus = (MVP_BONUS_PVM_ARENA_POINTS * npc.combatLevel).toInt()
                        player.pvmArenaPoints += mvpPointsBonus
                        player.pvmArenaPointsGainedDuringGame += mvpPointsBonus
                        player.pvmArenaMvpCountDuringGame++
                        player.sendMessage("You received an additional ${RS_RED.wrap(mvpPointsBonus)} PvM Arena points for being the MVP.")
                    } else {
                        player.sendMessage("${RS_RED.wrap(maxDamageDealerPlayerName)} was the MVP in defeating ${RS_RED.wrap(npc.name)}.")
                    }
                } else
                    player.sendMessage("You did not receive any PvM Arena points for defeating ${RS_RED.wrap(npc.name)}.")
            }
            currentState.removeNpc(npc)
            currentState
        } else {
            logger.warn("$state - tried to finish wave with npc $npc, but the current currentState is $currentState")
            currentState // We don't throw here because a player might have spawned a npc in here
        }
    }

    /**
     * Cancels the wave, removing all npcs from the world, and clearing future npc spawns.
     */
    fun cancel() = runCatchingWithStateChange {
        val state = state
        if (state is State.Active)
            state.clearAndRemoveNpcsFromWorld()
        npcProducerQueue.clear()
        State.Cancelled
    }

    private fun trySpawnNextNpcOrFinish() = runCatchingWithStateChange { currentState ->
        if (npcProducerQueue.isEmpty())
            State.Finished
        else {
            val nextState = if (currentState !is State.Active)
                State.Active()
            else
                currentState
            val npcProducer = npcProducerQueue.removeFirst()
            val npcGroup = npcProducer(PvmArenaNpc.SpawnConfig(team, waveConfig.maxHitPointsModifierProvider))
            npcGroup.forEach { npc ->
                npc.isForceMultiArea = true
                npc.isForceAttackable = true
                npc.isForceAggressive = true
                npc.spawn()
            }
            team.messagePlayers("${RS_RED.wrap(npcGroup.name)} has spawned.")
            nextState.addNpcGroup(npcGroup)
            nextState
        }
    }

    private fun runCatchingWithStateChange(action: (State) -> State) =
        runCatching { action(state) }
            .onSuccess { state = it }
            .onFailure { state = State.Exception(it) }

    /**
     * Represents the configuration for a wave of npcs in the PvM Arena activity.
     *
     * @param maxHitPointsModifierProvider a function that provides the maximum hit points modifier for the npcs in this wave.
     * @param maxNpcsToSpawnAtATimeProvider a function that provides the maximum amount of npcs that can be spawned at a time.
     * @param npcSpawnIntervalProvider a function that provides the interval between spawning npcs.
     */
    class Config(
        val maxHitPointsModifierProvider: () -> Double,
        val maxNpcsToSpawnAtATimeProvider: () -> Int,
        val npcSpawnIntervalProvider: () -> Duration,
        vararg val npcSpawns: (PvmArenaNpc.SpawnConfig) -> PvmArenaNpcSpawnGroup,
    )

    sealed class State {

        /**
         * Represents the state where the wave is waiting for the first npc to spawn.
         */
        data object Waiting : State()

        /**
         * Represents the state where the wave is actively running, spawning npcs.
         */
        class Active : State() {

            private val npcGroups = weakMutableSetOf<PvmArenaNpcSpawnGroup>()
            private lateinit var lastSpawnTime: Instant

            fun addNpcGroup(npc: PvmArenaNpcSpawnGroup) =
                npcGroups.add(npc).also { resetLastSpawnTime() }

            fun npcGroupCount(): Int =
                npcGroups.size

            fun removeNpc(npc: NPC) {
                npcGroups.find { group -> group.contains(npc) }?.remove(npc)
                npcGroups.removeIf { it.isEmpty() }
            }

            fun clearAndRemoveNpcsFromWorld() {
                npcGroups.forEach { group ->
                    group.forEach { npc -> npc.remove() }
                    group.clear()
                }
                npcGroups.clear()
            }

            fun resetLastSpawnTime() {
                lastSpawnTime = Clock.System.now()
            }

            fun isReadyToSpawnNext(config: Config): Boolean =
                !hasMaxNpcSpawnedAtATime(config) && Clock.System.now() >= getNextSpawnTime(config)

            fun hasMaxNpcSpawnedAtATime(config: Config): Boolean =
                npcGroups.size >= config.maxNpcsToSpawnAtATimeProvider()

            fun getTimeUntilNextSpawnNPC(config: Config): Duration =
                getNextSpawnTime(config) - Clock.System.now()

            private fun getNextSpawnTime(config: Config): Instant {
                val spawnIntervalTimer = config.npcSpawnIntervalProvider()
                val nextSpawnTime = lastSpawnTime + spawnIntervalTimer
                return nextSpawnTime
            }

            override fun toString(): String =
                "Active(npcs=$npcGroups)"
        }

        /**
         * Represents the state where the wave has finished running all npcs.
         */
        data object Finished : State()

        /**
         * Represents the state where the wave has been cancelled, e.g. both teams died.
         */
        data object Cancelled : State()

        /**
         * Represents the state where the wave has encountered an exception.
         */
        data class Exception(val throwable: Throwable) : State()
    }
}

private fun fetchDamageDoneByPlayerForNpc(
    npc: NPC,
    player: Player,
) = npc.receivedDamage.entries.filter { it.key.first == player.username }.flatMap { it.value }

private fun fetchMostDamageDealerForNpc(
    npc: NPC,
) = npc.receivedDamage.entries.groupBy { it.key.first }
    .mapValues { it.value.sumOf { it.value.sumOf { damage -> damage.damage } } }
    .maxByOrNull { it.value }?.key

