package com.zenyte.game.content.theatreofblood.room.verzikvitur.third

import com.zenyte.game.content.theatreofblood.room.verzikvitur.VerzikVitur
import com.zenyte.game.task.WorldTasksManager
import com.zenyte.game.world.Projectile
import com.zenyte.game.world.World
import com.zenyte.game.world.entity.masks.Animation

/**
 * @author Jire
 */

internal fun VerzikVitur.rangeAttack() {
    animation = Animation(8125)
    WorldTasksManager.schedule({
        for (p in room.validTargets) {
            val delay = World.sendProjectile(this, p, projectile)
            delayHit(delay, p, ranged(p, 32))
        }
    }, 1)
}

private val projectile = Projectile(1593, 32, 10, 0, 32, 60, 128, 0)