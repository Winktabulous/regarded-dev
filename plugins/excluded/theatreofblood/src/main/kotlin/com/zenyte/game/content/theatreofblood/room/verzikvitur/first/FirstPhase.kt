package com.zenyte.game.content.theatreofblood.room.verzikvitur.first

import com.zenyte.game.content.theatreofblood.room.verzikvitur.VerzikVitur
import com.zenyte.game.task.WorldTasksManager
import com.zenyte.game.util.Utils
import com.zenyte.game.world.Projectile
import com.zenyte.game.world.World
import com.zenyte.game.world.entity.masks.Animation
import com.zenyte.game.world.entity.masks.Graphics
import com.zenyte.game.world.entity.masks.Hit
import com.zenyte.game.world.entity.masks.HitType
import it.unimi.dsi.fastutil.objects.Object2IntMap
import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap

/**
 * @author Jire
 */

internal fun VerzikVitur.firstPhase() {
    ticks++
    if (ticks > 0 && ticks % (if (firstHit) 19 else 15) != 0) return

    firstHit = false
    animation = attackAnimation
    WorldTasksManager.schedule({
        val pillarToCount: Object2IntMap<SupportingPillar> = Object2IntOpenHashMap(room.supportingPillars.size)
        val validTargets = room.validTargets
        for (p in validTargets) {
            val behindPillar = room.supportingPillars.firstOrNull { it?.playerIsBehind(p) ?: false }
            if (behindPillar == null) {
                val blueProjectile = blueProjectile(28)
                val delay = World.sendProjectile(this, p, blueProjectile)

                WorldTasksManager.schedule({
                    p.graphics = attackPlayerGraphics
                    p.scheduleHit(this, magic(p, 137), -1)
                }, delay)
            } else {
                pillarToCount[behindPillar] =
                    pillarToCount.getInt(behindPillar) + 1
            }
        }

        /* And attack that pillar if one was found */
        val pillarToAttack = pillarToCount
            .filter { !it.key.isDead && !it.key.isDying && !it.key.isFinished }
            .minByOrNull { (_, v) -> v }
            ?.key
            ?: return@schedule
        val blueProjectile = blueProjectile(52)
        val delay = World.sendProjectile(this, pillarToAttack, blueProjectile)

        WorldTasksManager.schedule({
            pillarToAttack.graphics = attackPillarGraphics
            pillarToAttack.applyHit(Hit(this, Utils.random(40, 60), HitType.MAGIC))
        }, delay)
    }, 1)
}

private val attackAnimation = Animation(8109)
private val attackPillarGraphics = Graphics(1582)
private val attackPlayerGraphics = Graphics(1581)

private fun blueProjectile(endHeight: Int) = Projectile(
    1580, 126, endHeight,
    20, 29, 90, 64, 0
)