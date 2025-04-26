package com.near_reality.game.content.dt2.npc.whisperer.attacks.basic.impl

import com.near_reality.game.content.*
import com.near_reality.game.content.dt2.npc.whisperer.WhispererCombat
import com.near_reality.game.content.dt2.npc.whisperer.attacks.basic.WhispererBasicAttack
import com.zenyte.game.task.WorldTasksManager
import com.zenyte.game.world.Projectile
import com.zenyte.game.world.entity.AbstractEntity
import com.zenyte.game.world.entity.masks.Graphics
import com.zenyte.game.world.entity.player.Player

/**
 * @author Glabay | Glabay-Studios
 * @project near-reality-server
 * @social Discord: Glabay
 * @since 2024-09-23
 */
class MagicAttack : WhispererBasicAttack {

    private val hitSpotAnimId = 2446

    override operator fun invoke(whisperer: WhispererCombat, target: AbstractEntity) {
        // Construct a hit from the npc onto the target entity.
        val hit = whisperer hit target
        // Fire the projectile and return the estimated amount of game ticks until the projectile
        // arrives to the target.
        val ticks = whisperer fire Projectile(2445, 96, 32) at target
        // Whisperer attacks don't determine damage output until AFTER the hit has arrived
        // so we perform damage calculate accordingly.
        WorldTasksManager.schedule(ticks) {
            if (target is Player && target prayerActive ProtectFromMagic)
                hit.miss()
            else
                hit.rollDamage(24, 36)
            target.scheduleHit(whisperer, hit, ticks)
            target.graphics = Graphics(hitSpotAnimId)
        }
    }
}