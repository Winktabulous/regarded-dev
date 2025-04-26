package com.zenyte.game.content.theatreofblood.room.verzikvitur.second

import com.zenyte.game.content.theatreofblood.room.verzikvitur.VerzikVitur
import com.zenyte.game.task.WorldTasksManager
import com.zenyte.game.util.Utils
import com.zenyte.game.world.Projectile
import com.zenyte.game.world.World
import com.zenyte.game.world.entity.masks.Animation
import com.zenyte.game.world.entity.masks.Graphics
import com.zenyte.game.world.entity.masks.Hit
import com.zenyte.game.world.entity.masks.HitType

/**
 * @author Jire
 */

internal fun VerzikVitur.lampAttack() {
    animation = lampAttackAnimation
    for (p in room.validTargets) {
        val endLoc = p.location.copy()
        val delay = World.sendProjectile(this, endLoc, projectile)
        WorldTasksManager.schedule({
            World.sendGraphics(Graphics(1584), endLoc)
            if (p.location == endLoc) {
                p.scheduleHit(this, Hit(this, Utils.random(50), HitType.RANGED), -1)
            }
        }, delay)
    }
}

val lampAttackAnimation = Animation(8114)
private val projectile = Projectile(1583, 52, 0, 30, 0, 40, 128, 0)