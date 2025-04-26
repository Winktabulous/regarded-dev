package com.near_reality.game.content.dt2.npc.whisperer

import com.zenyte.game.util.Utils

object WhispyUtils {
    fun rand(i: Int): Int {
        return Utils.random(i)
    }

    fun rand(min: Int, max: Int): Int {
        return Utils.random(min, max)
    }
}