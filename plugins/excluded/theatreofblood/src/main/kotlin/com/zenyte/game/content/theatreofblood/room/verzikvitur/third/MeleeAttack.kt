package com.zenyte.game.content.theatreofblood.room.verzikvitur.third

import com.zenyte.game.content.theatreofblood.room.verzikvitur.VerzikVitur
import com.zenyte.game.world.entity.Entity
import com.zenyte.game.world.entity.masks.Animation

/**
 * @author Jire
 */
internal fun VerzikVitur.meleeAttack(victim: Entity) {
    animation = meleeAttackAnimation
    for (p in room.validTargets) {
        if (!p.location.withinDistance(victim, 1)) continue
        delayHit(1, p, melee(p, 60))
    }
}

private val meleeAttackAnimation = Animation(8123)