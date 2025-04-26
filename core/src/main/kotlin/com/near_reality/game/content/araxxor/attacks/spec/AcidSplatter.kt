package com.near_reality.game.content.araxxor.attacks.spec

import com.near_reality.game.content.araxxor.Araxxor
import com.near_reality.game.content.araxxor.AraxxorInstance
import com.near_reality.game.content.araxxor.attacks.Attack
import com.near_reality.game.content.seq
import com.zenyte.game.task.WorldTasksManager.schedule
import com.zenyte.game.world.Projectile
import com.zenyte.game.world.World
import com.zenyte.game.world.entity.Entity
import com.zenyte.game.world.entity.masks.Graphics

/**
 * @author Glabay | Glabay-Studios
 * @project near-reality-server
 * @social Discord: Glabay
 * @since 2024-10-25
 */
class AcidSplatter(val instance: AraxxorInstance) : Attack {

    private val projectileBlob: Projectile = Projectile(2924, 64, 32, 64, 0)

    override fun invoke(araxxor: Araxxor, target: Entity?) {
        if (target == null || target.isDead || target.isFinished) return
        araxxor seq 11476
        repeat(9) {
            val landing = instance.getAcidSplatterLocation(target.location)
            val delay = World.sendProjectile(araxxor.middleLocation, landing, projectileBlob)
            schedule(delay + 1) {
                World.sendGraphics(Graphics(2923, 0, 0), landing)
                instance.spawnAcidPool(landing)
            }
        }
    }
}