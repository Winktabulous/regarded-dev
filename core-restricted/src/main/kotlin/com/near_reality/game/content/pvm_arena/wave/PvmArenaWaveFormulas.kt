package com.near_reality.game.content.pvm_arena.wave


import com.near_reality.game.content.pvm_arena.PvmArenaTeam
import com.near_reality.game.content.pvm_arena.npc.PvmArenaNpcSpawnGroup
import com.near_reality.game.content.pvm_arena.npc.PvmArenaNpc
import com.near_reality.game.content.pvm_arena.npc.boss.*
import com.near_reality.game.content.pvm_arena.npc.boss.impl.*
import com.near_reality.game.content.pvm_arena.npc.impl.*
import com.near_reality.game.content.pvm_arena.npc.impl.PvmArenaBarrowsAhrimTheBlighted
import com.near_reality.game.content.pvm_arena.npc.impl.PvmArenaBarrowsDharokTheWretched
import com.near_reality.game.content.pvm_arena.npc.impl.PvmArenaBarrowsGuthanTheInfested
import com.near_reality.game.content.pvm_arena.npc.impl.PvmArenaBarrowsKarilTheTainted
import com.near_reality.game.content.pvm_arena.npc.impl.PvmArenaDemonicGorilla
import com.zenyte.game.world.entity.npc.NPC
import kotlin.math.pow
import kotlin.reflect.full.findParameterByName
import kotlin.time.Duration.Companion.seconds
/**
 * https://www.desmos.com/calculator/ezeir9teip
 *
 * p\left(x\right)=p_1*x^{4}+p_2*x^{3}+p_3*x^{2}+p_4*x+1
 *
 * Where x is capped to 150 as to avoid the function going berserk.
 */
var p1 = 0.000000001
var p2 = 0.0
var p3 = -0.0003
var p4 = 0.10
internal val p : (Double) -> Double = { x ->
    p1 * x.pow(4) + p2 * x.pow(3) + p3 * x.pow(2) + p4 * x + 1.0
}

/**
 * https://www.desmos.com/calculator/ftguuphgdr
 *
 * s\left(x\right)=s_1*x^{4}+s_2*x^{3}+s_3*x^{2}+s_4*x+1
 *
 * Where x is capped to 100 as to avoid the function going berserk.
 */
var s1 = 0.000000075
var s2 = 0.0000009
var s3 = -0.00101
var s4 = 0.08
internal val s : (Double) -> Double = { x ->
    s1 * x.pow(4) + s2 * x.pow(3) + s3 * x.pow(2) + s4 * x + 1.0
}

/**
 * https://www.desmos.com/calculator/bupgmoyihu
 *
 * t\left(x\right)=\frac{t_1}{x}+t_2
 *
 * Where x is capped between 2 and 100
 */
var t1 = 200.0
var t2 = 1.5
internal val t : (Double) -> Double = { x ->
    t1 / x + t2
}
