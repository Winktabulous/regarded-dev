package com.near_reality.game.content.dt2.npc.whisperer.attacks.basic.impl

import com.near_reality.game.content.*
import com.near_reality.game.content.dt2.npc.whisperer.WhispererCombat
import com.near_reality.game.content.dt2.npc.whisperer.WhispyUtils.rand
import com.zenyte.game.world.entity.Entity
import com.zenyte.game.world.entity.player.Player

/**
 * @author Glabay | Glabay-Studios
 * @project near-reality-server
 * @social Discord: Glabay
 * @since 2024-09-23
 */
class MeleeAttack {

    operator fun invoke(whisperer: WhispererCombat, target: Entity) {
        // Perform the melee attack seq.
        whisperer seq 10234
        val meleeDamage = if (target is Player && target prayerActive ProtectFromMelee) rand(28, 36)
        else rand(28, 42)

        repeat(2) {
            val hit = whisperer hit target damage meleeDamage
            target.scheduleHit(whisperer, hit, it)
        }
    }
}