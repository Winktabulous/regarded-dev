package com.near_reality.game.content.tournament.preset.component

import java.util.*

/**
 * @author Tommeh | 25/05/2019 | 17:05
 * @see [Rune-Server profile](https://www.rune-server.ee/members/tommeh/)
 */
data class SkillsComponent(val skills: Map<Int, Int>) {
    class Builder {
        private val skills: MutableMap<Int, Int> = TreeMap()

        init {
            for (skill in 0..22) {
                skills[skill] = 1
            }
        }

        fun set(skill: Int, level: Int): Builder {
            skills[skill] = level
            return this
        }

        fun build(): SkillsComponent {
            return SkillsComponent(skills)
        }
    }
}
