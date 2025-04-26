package com.zenyte.game.content.theatreofblood.room.verzikvitur.second

import com.zenyte.game.content.theatreofblood.room.verzikvitur.VerzikVitur
import com.zenyte.game.task.WorldTasksManager
import com.zenyte.game.util.Utils
import com.zenyte.game.world.Projectile
import com.zenyte.game.world.World
import com.zenyte.game.world.entity.masks.Animation
import com.zenyte.game.world.entity.masks.Graphics
import com.zenyte.game.world.entity.npc.combat.Default
import com.zenyte.game.world.entity.player.action.combat.magic.CombatSpell
import kotlin.math.floor

/**
 * @author Jire
 */
internal fun VerzikVitur.bloodAttack() {
    animation = bloodAttackAnimation
    val p = Utils.getRandomCollectionElement(room.validTargets) ?: return
    val ticks = World.sendProjectile(this, p, projectile)

    WorldTasksManager.schedule({
        val hit = magic(p, CombatSpell.BLOOD_BARRAGE.maxHit).onLand {
            val damage = it.damage
            if (damage > 0) {
                p.graphics = bloodAttackGraphics

                val healAmount = floor(damage / 2.0).toInt()
                if (healAmount > 0)
                    heal(healAmount)
            } else {
                p.graphics = Default.SPLASH_GRAPHICS
            }
        }
        p.scheduleHit(this, hit, -1)
    }, ticks)
}

private val bloodAttackAnimation = Animation(8114)
private val bloodAttackGraphics = Graphics(1592)
private val projectile = Projectile(1591, 52, 26, 30, 0, 40, 128, 0)