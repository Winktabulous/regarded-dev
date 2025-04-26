package com.near_reality.game.content.araxxor.attacks.spec

import com.near_reality.game.content.araxxor.Araxxor
import com.near_reality.game.content.araxxor.AraxxorInstance
import com.near_reality.game.content.araxxor.attacks.AcidPool
import com.near_reality.game.content.araxxor.attacks.Attack
import com.near_reality.game.content.damage
import com.near_reality.game.content.hit
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
class AcidDrip(val instance: AraxxorInstance) : Attack {

    private val projectileBlob: Projectile = Projectile(2924, 64, 32, 96, 0)

    override fun invoke(araxxor: Araxxor, target: Entity?) {
        if (target == null || target.isDead || target.isFinished) return
        araxxor seq 11478
        World.sendProjectile(araxxor, target, projectileBlob)
        val delay = araxxor.middleLocation.getDistance(target.location).toInt() - 3
        schedule(delay) {
            World.spawnObject(AcidPool(target.location))
            target.temporaryAttributes["araxxor_acid_drip"] = 6
        }
    }
}