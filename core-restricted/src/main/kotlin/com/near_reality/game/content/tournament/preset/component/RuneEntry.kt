package com.near_reality.game.content.tournament.preset.component

import com.zenyte.game.content.skills.magic.Rune

/**
 * @author Tommeh | 22/07/2019 | 22:11
 * @see [Rune-Server profile](https://www.rune-server.ee/members/tommeh/)
 */
data class RuneEntry(val rune: Rune, val amount: Int) {
    override fun toString(): String {
        return "RuneEntry(rune=" + this.rune + ", amount=" + this.amount + ")"
    }

    companion object {
        @JvmStatic
        fun of(rune: Rune, amount: Int): RuneEntry {
            return RuneEntry(rune, amount)
        }
    }
}
