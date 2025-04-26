package com.near_reality.game.content.custom

import com.zenyte.game.util.Utils
import com.zenyte.game.world.entity.Entity
import com.zenyte.game.world.entity.masks.Hit
import com.zenyte.game.world.entity.npc.NPC
import com.zenyte.game.world.entity.player.action.combat.MeleeCombat


/**
 * Handles lava whip chance of firing dragon-fire-shield effect.
 *
 * If the effect is activated, it will be applied in the next combat tick.
 *
 * @author Stan van der Bend
 */
class LavaWhipCombat(target: Entity) : MeleeCombat(target) {

    private var skipDelay = false

    override fun extra(hit: Hit) {
        super.extra(hit)
        if (target is NPC && Utils.random(100) <= DFS_EFFECT_CHANCE) {
            player.temporaryAttributes[ATTRIBUTE_KEY] = true
            skipDelay = true
        }
    }

    override fun getSpeed(): Int =  if (skipDelay) {
        skipDelay = false
        0
    } else
        super.getSpeed()

    companion object {

        const val ATTRIBUTE_KEY = "lava_whip_effect"

        /**
         * Chance out of 100 to double hit.
         */
        private const val DFS_EFFECT_CHANCE = 3
    }
}
