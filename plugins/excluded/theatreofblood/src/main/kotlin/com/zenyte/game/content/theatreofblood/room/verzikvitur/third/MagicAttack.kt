package com.zenyte.game.content.theatreofblood.room.verzikvitur.third

import com.zenyte.game.content.theatreofblood.room.verzikvitur.VerzikVitur
import com.zenyte.game.task.WorldTasksManager
import com.zenyte.game.world.Projectile
import com.zenyte.game.world.World
import com.zenyte.game.world.entity.masks.Animation

/**
 * @author Jire
 */

internal fun VerzikVitur.magicAttack() {
    animation = magicAttackAnimation
    WorldTasksManager.schedule({
        for (p in room.validTargets) {
            val delay = World.sendProjectile(this, p, projectile)
            delayHit(delay, p, magic(p, 32))
        }
    }, 1)
}

private val magicAttackAnimation = Animation(8124)
private val projectile = Projectile(1594, 32, 10, 0, 32, 50, 128, 0)