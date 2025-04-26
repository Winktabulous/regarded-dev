package com.near_reality.game.content.dt2.npc.vardorvis.attacks.impl.headgaze

import com.zenyte.game.util.Direction
import com.zenyte.game.world.entity.Entity
import com.zenyte.game.world.entity.Location
import com.zenyte.game.world.entity.npc.NPC
import com.zenyte.game.world.entity.npc.NpcId
import com.zenyte.game.world.entity.npc.combat.CombatScript
/**
 * @author John J. Woloszyk / Kryeus
 * @date 5.9.2024
 */
class VardorvisHead(
    tile: Location
) : NPC(
    NpcId.VARDORVIS_HEAD,
    tile,
    Direction.SOUTH,
    0,
    true
), CombatScript {

    override fun attack(target: Entity): Int {
        return 5
    }

    override fun isForceAggressive(): Boolean = false
    override fun isMovementRestricted(): Boolean = true

    override fun setRespawnTask() {}
    override fun isSpawned(): Boolean = true
}