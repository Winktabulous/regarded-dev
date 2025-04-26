package com.zenyte.game.content.skills.magic.spells.arceuus

import com.zenyte.game.util.Utils
import com.zenyte.game.world.entity.Entity
import com.zenyte.game.world.entity.masks.Graphics
import com.zenyte.game.world.entity.player.action.combat.magic.spelleffect.SpellEffect

/**
 * @author Kris | 16/06/2022
 */
class UndeadGraspEffect : SpellEffect {
    override fun spellEffect(player: Entity, target: Entity, damage: Int) {
        if (damage == 0 || Utils.random(1) != 0) return
        target.graphics = Graphics(1863, 30, 0)
        val duration = when {
            target.hasWardOfArceuus -> 1
            target.hasMarkOfDarkness -> 8
            else -> 4
        }
        target.freezeWithNotification(duration, 0)
    }
}