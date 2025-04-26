package com.near_reality.game.content.tournament.preset.component

import com.near_reality.game.content.tournament.preset.component.RuneEntry.Companion.of
import com.zenyte.game.content.skills.magic.Rune

/**
 * @author Tommeh | 22/07/2019 | 22:10
 * @see [Rune-Server profile](https://www.rune-server.ee/members/tommeh/)
 */
data class RunePouchComponent(val entries: Set<RuneEntry>) {
    companion object {
        val VENGEANCE: RunePouchComponent = of(of(Rune.ASTRAL, 1000), of(Rune.DEATH, 1000), of(Rune.EARTH, 1000))
        val ICE_BARRAGE: RunePouchComponent = of(of(Rune.WATER, 1000), of(Rune.DEATH, 1000), of(Rune.BLOOD, 1000))

        fun of(vararg entries: RuneEntry): RunePouchComponent =
            RunePouchComponent(entries.toSet())
    }
}
