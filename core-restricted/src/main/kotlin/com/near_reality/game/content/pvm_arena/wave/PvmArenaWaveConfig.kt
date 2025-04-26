package com.near_reality.game.content.pvm_arena.wave

import com.near_reality.game.content.pvm_arena.npc.boss.impl.*
import com.near_reality.game.content.pvm_arena.npc.impl.*

internal val pvmArenaWaveConfig = buildWaves {
    // Cycle 1
    single<PvmArenaKreeArra>()
    single<PvmArenaCommanderZilyana>()
    single<PvmArenaGeneralGraardor>()
    single<PvmArenaKrillTsutsaroth>()
    single<PvmArenaDagannothSupreme>()
    single<PvmArenaDagannothPrime>()
    single<PvmArenaDagannothRex>()
    multi("Barrows Brothers") { config ->
        add(PvmArenaBarrowsAhrimTheBlighted(config))
        add(PvmArenaBarrowsDharokTheWretched(config))
        add(PvmArenaBarrowsGuthanTheInfested(config))
        add(PvmArenaBarrowsKarilTheTainted(config))
        add(PvmArenaBarrowsToragTheCorrupted(config))
        add(PvmArenaBarrowsVeracTheDefiled(config))
    }
    single<PvmArenaKalphiteQueen>()
    single<PvmArenaKingBlackDragon>()
    single<PvmArenaCallisto>()
    single<PvmArenaVenenatis>()
    single<PvmArenaChaosElemental>()
    single<PvmArenaCorporealBeast>()

    // Cycle 2
    single<PvmArenaGeneralGraardor>()
    single<PvmArenaGiantMole>()
    single<PvmArenaCommanderZilyana>()
    single<PvmArenaVenenatis>()
    single<PvmArenaCallisto>()
    multi("2 Demonic gorillas") { config ->
        add(PvmArenaDemonicGorilla(config))
        add(PvmArenaDemonicGorilla(config))
    }
    single<PvmArenaKalphiteQueen>()
    single<PvmArenaDagannothSupreme>()
    single<PvmArenaKrillTsutsaroth>()
    single<PvmArenaDagannothRex>()
    multi("3 Demonic gorillas") { config ->
        add(PvmArenaDemonicGorilla(config))
        add(PvmArenaDemonicGorilla(config))
        add(PvmArenaDemonicGorilla(config))
    }
    single<PvmArenaKreeArra>()
    single<PvmArenaKingBlackDragon>()
    single<PvmArenaDagannothPrime>()
    single<PvmArenaChaosElemental>()
    single<PvmArenaCorporealBeast>()
}
