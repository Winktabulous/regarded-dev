package com.near_reality.game.content.araxxor.araxytes.impl

import com.near_reality.game.content.araxxor.AraxxorInstance
import com.near_reality.game.content.araxxor.araxytes.Araxyte
import com.near_reality.game.content.damage
import com.near_reality.game.content.hit
import com.near_reality.game.content.seq
import com.near_reality.game.content.spotanim
import com.zenyte.game.item.ItemId
import com.zenyte.game.item.ItemId.NOXIOUS_HALBERD
import com.zenyte.game.task.WorldTasksManager.schedule
import com.zenyte.game.world.entity.Entity
import com.zenyte.game.world.entity.Location
import com.zenyte.game.world.entity.masks.Hit
import com.zenyte.game.world.entity.masks.HitType
import com.zenyte.game.world.entity.npc.NpcId.MIRRORBACK_ARAXYTE
import com.zenyte.game.world.entity.npc.NpcId.MIRRORBACK_ARAXYTE_EGG
import com.zenyte.game.world.entity.npc.combatdefs.AttackType
import com.zenyte.game.world.entity.player.Player
import com.zenyte.game.world.entity.player.action.combat.CombatUtilities
import java.util.*

/**
 * @author Glabay | Glabay-Studios
 * @project near-reality-server
 * @social Discord: Glabay
 * @since 2024-10-20
 */
class MirrorbackAraxyte(
    val instance: AraxxorInstance,
    spawnLocation: Location
): Araxyte(
    MIRRORBACK_ARAXYTE_EGG,
    spawnLocation
) {

    override fun attack(target: Entity?): Int {
        if (getId() == MIRRORBACK_ARAXYTE_EGG) return 1
        if (target == null) return 1
        this seq 11497
        val damage = CombatUtilities.getRandomMaxHit(this, 15, AttackType.MELEE, target)
        target.scheduleHit(this, this hit target damage damage, 0)
        return 6
    }

    override fun hatchEgg(target: Entity?) {
        if (target == null) return
        if (isDead || isFinished) return
        this seq 11509
        this spotanim 2929
        schedule(1) {
            setTransformationPreservingStats(MIRRORBACK_ARAXYTE)
            addWalkSteps(instance.centerArenaTile.x, instance.centerArenaTile.y, 6)
            schedule(3) { setTarget(target) }
        }
    }

    override fun handleIngoingHit(hit: Hit?) {
        if (hit == null) return
        super.handleIngoingHit(hit)
        if (isDead || isFinished) return
        if (hit.hitType == HitType.MAGIC || hit.hitType == HitType.RANGED) return
        if (hit.source is Player) {
            val weaponId = (hit.source as Player).weapon.id
            if (Objects.equals(NOXIOUS_HALBERD, weaponId)) return
        }
        val damage = hit.damage
        if (damage > 1) {
            val hitBack = damage / 2
            hit.source.applyHit(Hit(this, hitBack, HitType.TYPELESS))
        }
    }
}