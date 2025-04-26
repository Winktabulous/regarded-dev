package com.near_reality.game.content.tormented_demon.attacks.impl

import com.near_reality.game.content.damage
import com.near_reality.game.content.hit
import com.near_reality.game.content.seq
import com.near_reality.game.content.tormented_demon.TormentedDemon
import com.near_reality.game.content.tormented_demon.attacks.Attack
import com.zenyte.game.content.skills.prayer.Prayer
import com.zenyte.game.world.entity.Entity
import com.zenyte.game.world.entity.masks.Graphics
import com.zenyte.game.world.entity.npc.combatdefs.AttackType
import com.zenyte.game.world.entity.player.Player
import com.zenyte.game.world.entity.player.action.combat.CombatUtilities

/**
 * @author Glabay | Glabay-Studios
 * @project near-reality-server
 * @social Discord: Glabay
 * @since 2024-10-16
 */
class MeleeSlash : Attack {

    override fun invoke(demon: TormentedDemon, target: Entity?) {
        if (target == null) return
        // Animate the attack
        demon seq demon.getMeleeSlashAnimation()
        demon.graphics = Graphics(2851)
        var damageMax = 31
        if (target is Player && target.prayerManager.isActive(Prayer.PROTECT_FROM_MELEE))
            damageMax = 0
        val damage = CombatUtilities.getRandomMaxHit(demon, damageMax, AttackType.MELEE, target)
        val hit = demon hit target damage damage
        target.scheduleHit(demon, hit, 0)
    }
}