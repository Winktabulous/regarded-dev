package com.near_reality.game.content.pvm_arena.wave

import com.near_reality.game.content.pvm_arena.PvmArenaTeam
import com.near_reality.game.content.pvm_arena.npc.PvmArenaNpc
import com.near_reality.game.content.pvm_arena.npc.PvmArenaNpcSpawnGroup
import com.near_reality.game.content.pvm_arena.npc.boss.PvmArenaBoss
import com.zenyte.game.world.entity.npc.NPC
import kotlin.reflect.full.findParameterByName
import kotlin.time.Duration.Companion.seconds


@Target(AnnotationTarget.CLASS, AnnotationTarget.FUNCTION, AnnotationTarget.TYPE)
@DslMarker
annotation class WaveConfigDsl

@WaveConfigDsl
fun buildWaves(block: WaveConfigBuilder.() -> Unit): PvmArenaWave.Config {
    return WaveConfigBuilder().apply(block).build()
}

@WaveConfigDsl
class WaveConfigBuilder : WaveBuilder() {

    // TODO: ensure that both teams always have the same final config, atm it randomizes it for both teams differently
//    @WaveConfigDsl
//    inline fun random(block: @WaveConfigDsl WaveBuilder.() -> Unit) {
//        val randomPickWaves = WaveBuilder().apply(block).waves
//        if (randomPickWaves.isEmpty())
//            return
//        waves.add { config -> randomPickWaves.random().invoke(config) }
//    }

    fun build() : PvmArenaWave.Config {
        return PvmArenaWave.Config(
            maxHitPointsModifierProvider = {
                val totalPlayersAlive = PvmArenaTeam.Blue.players.size + PvmArenaTeam.Red.players.size
                p(totalPlayersAlive.coerceIn(2..100).toDouble())
            },
            maxNpcsToSpawnAtATimeProvider = {
                val totalPlayersAlive = PvmArenaTeam.Blue.players.size + PvmArenaTeam.Red.players.size
                s(totalPlayersAlive.coerceIn(2..100).toDouble()).toInt().coerceIn(1..4)
            },
            npcSpawnIntervalProvider = {
                val totalPlayersAlive = PvmArenaTeam.Blue.players.size + PvmArenaTeam.Red.players.size
                t(totalPlayersAlive.coerceIn(2..100).toDouble()).seconds
            },

            *waves.toTypedArray()
        )
    }
}

typealias SpawnConfig = (PvmArenaNpc.SpawnConfig) -> List<NPC>

open class WaveBuilder {

    @PublishedApi
    internal val waves = mutableListOf<(PvmArenaNpc.SpawnConfig) -> PvmArenaNpcSpawnGroup>()

    @WaveConfigDsl
    inline fun <reified T : PvmArenaBoss> single() {
        val defaultConstructor = T::class.constructors.find { it.findParameterByName(PvmArenaBoss::config.name) != null }
        if (defaultConstructor == null)
            error("No default constructor found for ${T::class.simpleName} (must have a constructor with a single parameter of type PvmArenaNpc.SpawnConfig)")
        waves.add { config ->
            val newBoss = defaultConstructor.call(config)
            if (newBoss is NPC)
                PvmArenaNpcSpawnGroup(newBoss as NPC)
            else
                error("Failed to create boss ${T::class.simpleName} with constructor ${defaultConstructor.parameters}")
        }
    }

    @WaveConfigDsl
    internal fun multi(name: String, block: MutableList<PvmArenaNpc>.(PvmArenaNpc.SpawnConfig) -> Unit) {
        waves.add { config ->
            val npcs = buildList { block(config) }.filterIsInstance<NPC>().toSet()
            PvmArenaNpcSpawnGroup(name, npcs)
        }
    }
}
