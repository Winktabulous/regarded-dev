package com.near_reality.game.content.dt2.npc.whisperer.attacks.special.impl.siphon

import com.zenyte.game.world.entity.Tinting

/**
 * @author Glabay | Glabay-Studios
 * @project near-reality-server
 * @social Discord: Glabay
 * @since 2024-09-28
 */
enum class LostSoulType(
    val chant: String,
    private val hue: Int,
    private val saturation: Int,
    private val luminance: Int,
    private val opacity: Int,
    private val duration: Int = 3_000,
    val soulTinting: Tinting = Tinting(hue, saturation, luminance, opacity, 0, duration)
) {
    MORS(   "Mors!",    148,    40, 150,    100), // Green
    ORATIO( "Oratio!",  211,    40, 150,    100), // Blue
    SANITAS("Sanitas!", 182,    40, 150,    100), // Cyan
    VITA(   "Vita!",    67,     40, 150,    100)  // Yellow / Default
}