package com.near_reality.game.world.entity.player

import com.near_reality.game.content.consumables.drinks.DivinePotion
import com.zenyte.game.world.entity.player.Player
import com.zenyte.game.world.entity.player.SkillConstants


/**
 * Handles skill stat restoration.
 *
 * @author Stan van der Bend
 */
class SkillRestoration {

    /**
     * Increments or decrements the current skill level towards the baseline level,
     * given that the [tick] is a multiple of [PERIOD].
     *
     * The baseline in most cases is the level in the skill calculated from the experience in the skill,
     * however, in the case of [divine potions][DivinePotion], the baseline is the boosted level in the skill.
     *
     * TODO: figure out if the baseline for divine potions is static at point of consumption,
 *           or is re-calculated at every check, currently the latter applies.
     */
    fun pulse(tick: Int, player: Player) {

        if (tick % PERIOD == 0) {

            for (skill in processedSkills) {

                val currentLevel: Int = player.skills.getLevel(skill)
                val normalLevel: Int = player.skills.getLevelForXp(skill)

                if (decrement(player, skill, currentLevel, normalLevel))
                    player.skills.setLevel(skill, currentLevel - 1)
                else if (currentLevel < normalLevel)
                    if (skill != SkillConstants.HITPOINTS)
                        player.skills.setLevel(skill, currentLevel + 1)
            }
        }
    }

    private fun decrement(player: Player, skill: Int, currentLevel: Int, targetLevel: Int): Boolean {
        if (currentLevel > targetLevel)
            return DivinePotion.all
                .filter { potion -> potion.isActive(player) }
                .flatMap { potion -> potion.boosts()
                .filter { boost -> boost.skill == skill } }
                .maxOfOrNull { it.getBoostedLevel(player) }
                ?.let { currentLevel > targetLevel.coerceAtLeast(it) }
                ?:true
        return false
    }

    private companion object {

        const val PERIOD = 100

        val processedSkills = SkillConstants.SKILLS.indices.toList() - SkillConstants.PRAYER
    }
}
