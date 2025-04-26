package com.near_reality.game.content.araxxor.araxytes.impl

import com.near_reality.game.content.araxxor.AraxxorInstance
import com.near_reality.game.content.araxxor.araxytes.Araxyte
import com.near_reality.game.content.damage
import com.near_reality.game.content.hit
import com.near_reality.game.content.seq
import com.near_reality.game.content.spotanim
import com.zenyte.game.task.WorldTasksManager.schedule
import com.zenyte.game.world.Projectile
import com.zenyte.game.world.World
import com.zenyte.game.world.entity.Entity
import com.zenyte.game.world.entity.Location
import com.zenyte.game.world.entity.masks.Graphics
import com.zenyte.game.world.entity.npc.NpcId.ACIDIC_ARAXYTE
import com.zenyte.game.world.entity.npc.NpcId.ACIDIC_ARAXYTE_EGG
import com.zenyte.game.world.entity.npc.combatdefs.AttackType
import com.zenyte.game.world.entity.player.action.combat.CombatUtilities

/**
 * @author Glabay | Glabay-Studios
 * @project near-reality-server
 * @social Discord: Glabay
 * @since 2024-10-20
 */
class AcidicAraxyte(
    val instance: AraxxorInstance,
    spawnLocation: Location
): Araxyte(
    ACIDIC_ARAXYTE_EGG,
    spawnLocation
) {

    private val projectile: Projectile = Projectile(2925, 16, 32, 64, 0)

    private val projectileBlob: Projectile = Projectile(2924, 64, 32, 64, 0)

    override fun attack(target: Entity?): Int {
        if (getId() == ACIDIC_ARAXYTE_EGG) return 1
        if (target == null) return 1
        this seq 11497
        val damage = CombatUtilities.getRandomMaxHit(this, 15, AttackType.RANGED, target)
        val delay = World.sendProjectile(this, target, projectile)
        target.scheduleHit(this, this hit target damage damage, delay)
        return 6
    }

    override fun hatchEgg(target: Entity?) {
        if (target == null) return
        if (isDead || isFinished) return
        this seq 11509
        this spotanim 2927
        schedule(1) {
            setTransformationPreservingStats(ACIDIC_ARAXYTE)
            addWalkSteps(instance.centerArenaTile.x, instance.centerArenaTile.y, 6)
            schedule(3) { setTarget(target) }
        }
    }

    override fun onDeath(source: Entity?) {
        super.onDeath(source)
        val deathLocation = this.location.copy()
        repeat(9) {
            val landing = instance.getAcidSplatterLocation(deathLocation)
            val delay = World.sendProjectile(deathLocation, landing, projectileBlob)
            World.sendGraphics(Graphics(2923, delay, 0), landing)
            instance.spawnAcidPool(landing)
            instance.araxytes.remove(this)
        }
    }

}