package com.near_reality.game.content.elven.obj

import com.zenyte.game.util.Utils
import com.zenyte.game.world.entity.player.Player

enum class LootRarity(
    val myName: String = "",
    private val normalScale: Int = 1,
    private val enrichedScale: Int = 1,
    val always: Boolean = false,
    val default: Boolean = false
) {
    ALWAYS("Always", always = true),
    COMMON("Common", 1, default = true),
    RARE("Rare", 100, 50),
    SUPER_RARE("Super Rare", 500, 250),
    JACKPOT("Jackpot", 1500, 750);

    fun getDivisor(enhanced: Boolean): Int {
        return if (enhanced) enrichedScale else normalScale
    }

    fun rolledTable(player: Player, enhanced: Boolean): Boolean {
        val dr = (player.dropRateBonus / 100) + 1.00
        val divisor = (getDivisor(enhanced) / dr).toInt()
        return Utils.random(divisor) == 0
    }
}


