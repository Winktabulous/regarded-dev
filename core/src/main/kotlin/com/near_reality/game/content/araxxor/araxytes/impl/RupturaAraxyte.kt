package com.near_reality.game.content.araxxor.araxytes.impl

import com.near_reality.game.content.araxxor.AraxxorInstance
import com.near_reality.game.content.araxxor.araxytes.Araxyte
import com.near_reality.game.content.seq
import com.near_reality.game.content.spotanim
import com.zenyte.game.task.WorldTasksManager.schedule
import com.zenyte.game.world.entity.Entity
import com.zenyte.game.world.entity.Location
import com.zenyte.game.world.entity.masks.Hit
import com.zenyte.game.world.entity.masks.HitType
import com.zenyte.game.world.entity.npc.NpcId.RUPTURA_ARAXYTE
import com.zenyte.game.world.entity.npc.NpcId.RUPTURA_ARAXYTE_EGG

/**
 * @author Glabay | Glabay-Studios
 * @project near-reality-server
 * @social Discord: Glabay
 * @since 2024-10-20
 */
class RupturaAraxyte(
    val instance: AraxxorInstance,
    spawnLocation: Location,
    private var exploding: Boolean = false
): Araxyte(
    RUPTURA_ARAXYTE_EGG,
    spawnLocation
) {

    override fun attack(target: Entity?): Int {
        if (target == null) return 1
        if (exploding) return 1
        if (this.middleLocation.withinDistance(target, 2)) {
            exploding = true
            lock()
            this seq 11510
            schedule(2) {
                if (instance.araxxor.middleLocation.withinDistance(this.location, 5))
                    instance.araxxor.applyHit(Hit(this, 80, HitType.DEFAULT))
                else if (instance.araxxor.middleLocation.withinDistance(this.location, 6))
                    instance.araxxor.applyHit(Hit(this, 64, HitType.DEFAULT))

                if (target.location.withinDistance(this.location, 2))
                    target.applyHit(Hit(this, 49, HitType.DEFAULT))
                else if (target.location.withinDistance(this.location, 3))
                    target.applyHit(Hit(this, 28, HitType.DEFAULT))
                else if (target.location.withinDistance(this.location, 4))
                    target.applyHit(Hit(this, 7, HitType.DEFAULT))

                remove()
            }
        }
        return 1
    }

    override fun hatchEgg(target: Entity?) {
        if (target == null) return
        this seq 11509
        this spotanim 2928
        schedule(1) {
            setTransformationPreservingStats(RUPTURA_ARAXYTE)
            addWalkSteps(instance.centerArenaTile.x, instance.centerArenaTile.y, 6)
            schedule(3) { setTarget(target) }
        }
    }

}