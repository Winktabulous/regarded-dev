package com.near_reality.game.content.araxxor.attacks.impl

import com.near_reality.game.content.araxxor.Araxxor
import com.near_reality.game.content.araxxor.attacks.Attack
import com.near_reality.game.content.damage
import com.near_reality.game.content.hit
import com.near_reality.game.content.seq
import com.zenyte.game.content.skills.prayer.Prayer
import com.zenyte.game.world.Projectile
import com.zenyte.game.world.World
import com.zenyte.game.world.entity.Entity
import com.zenyte.game.world.entity.npc.combatdefs.AttackType
import com.zenyte.game.world.entity.player.Player
import com.zenyte.game.world.entity.player.action.combat.CombatUtilities

/**
 * @author Glabay | Glabay-Studios
 * @project near-reality-server
 * @social Discord: Glabay
 * @since 2024-10-20
 */
class RangedAttack: Attack {

    private val projectileBlob : Projectile = Projectile(2924, 64, 32, 64, 0)

    override fun invoke(araxxor: Araxxor, target: Entity?) {
        if (target == null) return
        araxxor seq 11478
        var maxHit = 34
        if ((target as Player).prayerManager.isActive(Prayer.PROTECT_FROM_MISSILES))
            maxHit = (maxHit * 0.20).toInt()
        val damage = CombatUtilities.getRandomMaxHit(araxxor, maxHit, AttackType.RANGED, target)
        val delay = World.sendProjectile(araxxor, target, projectileBlob)
        val hit = araxxor hit target damage damage

        target.scheduleHit(araxxor, hit, delay)
    }
}