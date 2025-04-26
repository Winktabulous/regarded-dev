package com.near_reality.game.content.araxxor.araxytes

import com.zenyte.game.world.entity.Entity
import com.zenyte.game.world.entity.Location
import com.zenyte.game.world.entity.masks.Hit
import com.zenyte.game.world.entity.npc.NPC
import com.zenyte.game.world.entity.npc.combat.CombatScript
import com.zenyte.game.world.entity.npc.combat.CombatScript.CRUSH
import com.zenyte.game.world.entity.player.Player
import com.zenyte.game.world.entity.player.container.impl.equipment.EquipmentSlot

/**
 * @author Glabay | Glabay-Studios
 * @project near-reality-server
 * @social Discord: Glabay
 * @since 2024-10-20
 */
abstract class Araxyte(
    araxyteId: Int,
    spawnLocation: Location
): NPC(
    araxyteId,
    spawnLocation,
    true
), CombatScript {

    override fun handleIngoingHit(hit: Hit?) {
        if (hit == null) return
        if (hit.source is Player) {
            val player = hit.source as Player
            val weapon = player.equipment.getItem(EquipmentSlot.WEAPON)
            if (weapon.name.contains("noxious halberd", true) ||
                weapon.name.contains("crossbow", true) ||
                weapon.name.contains("ballista", true) ||
                hit.hitType == CRUSH)

                hit.damage = getHitpoints()
        }
        super.handleIngoingHit(hit)
    }

    override fun isEntityClipped(): Boolean  = false

    abstract fun hatchEgg(target: Entity?)
}