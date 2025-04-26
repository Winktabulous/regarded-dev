package com.near_reality.game.content.tormented_demon.attacks.impl

import com.near_reality.game.content.damage
import com.near_reality.game.content.hit
import com.near_reality.game.content.seq
import com.near_reality.game.content.tormented_demon.TormentedDemon
import com.near_reality.game.content.tormented_demon.attacks.Attack
import com.zenyte.game.content.skills.prayer.Prayer
import com.zenyte.game.world.Projectile
import com.zenyte.game.world.World
import com.zenyte.game.world.entity.AbstractEntity
import com.zenyte.game.world.entity.Entity
import com.zenyte.game.world.entity.npc.combatdefs.AttackType
import com.zenyte.game.world.entity.player.Player
import com.zenyte.game.world.entity.player.action.combat.CombatUtilities

/**
 * @author Glabay | Glabay-Studios
 * @project near-reality-server
 * @social Discord: Glabay
 * @since 2024-10-16
 */
class RangedAttack : Attack {

    private val projectile : Projectile =
        Projectile(2857, 64, 32, 96, 0)

    override fun invoke(demon: TormentedDemon, target: Entity?) {
        if (target == null) return
        demon seq demon.getRangeAttackAnimation()
        var damageMax = 31
        if (target is Player && target.prayerManager.isActive(Prayer.PROTECT_FROM_MISSILES))
            damageMax = 0
        val damage = CombatUtilities.getRandomMaxHit(demon, damageMax, AttackType.RANGED, target)
        val delay = World.sendProjectile(demon, target, projectile)
        target.scheduleHit(demon, demon hit target damage damage, delay)
    }
}