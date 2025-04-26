package com.zenyte.game.content.theatreofblood.room.verzikvitur.third.passivespells

import com.zenyte.game.content.theatreofblood.room.verzikvitur.VerzikVitur
import com.zenyte.game.content.theatreofblood.room.verzikvitur.third.PassiveSpell
import com.zenyte.game.task.WorldTasksManager
import com.zenyte.game.util.Utils
import com.zenyte.game.world.Projectile
import com.zenyte.game.world.World
import com.zenyte.game.world.entity.Entity
import com.zenyte.game.world.entity.masks.Animation
import com.zenyte.game.world.entity.masks.Graphics
import com.zenyte.game.world.entity.masks.Hit
import com.zenyte.game.world.entity.masks.HitType
import com.zenyte.game.world.entity.player.Player

/**
 * @author Jire
 */
internal object MeteorPassiveSpell : PassiveSpell {

    override val nextSpell = NylocasPassiveSpell

    private val castAnimation = Animation(8125)
    private val finalBounceGraphics = Graphics(1600, 0, 96)
    override fun VerzikVitur.cast() {
        val first = room.validTargets.firstOrNull() ?: return
        animation = castAnimation

        fun ball(from: Entity, target: Player, bounces: Int) {
            val projectile = Projectile(1598, if (first == target) 48 else 28, 28, 0, 9, 180, 0, 5)
            val delay = World.sendProjectile(from, target, projectile)
            WorldTasksManager.schedule({
                var closest: Player? = null
                var closestDistance = Double.MAX_VALUE
                for (p in room.validTargets) {
                    if (target == p) continue
                    val distance = p.location.getDistance(target.location)
                    if (distance < closestDistance) {
                        closest = p
                        closestDistance = distance
                    }
                }

                if (closest == null || closestDistance > 2) {
                    // the final bounce
                    val hit = when(room.raid.bypassMode) {
                        true -> 74
                        false -> 30
                    }

                    target.graphics = finalBounceGraphics
                    target.applyHit(Hit(this, hit, HitType.MAGIC))
                } else {
                    target.applyHit(Hit(this, Utils.random(15), HitType.MAGIC))
                    if (bounces < 3)
                        ball(target, closest, bounces + 1)
                }
            }, delay)
        }

        WorldTasksManager.schedule({
            ball(this, first, 0)
        }, 2)
    }

}