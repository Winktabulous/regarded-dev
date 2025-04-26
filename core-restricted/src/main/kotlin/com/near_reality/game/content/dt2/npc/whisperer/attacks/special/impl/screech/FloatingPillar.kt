package com.near_reality.game.content.dt2.npc.whisperer.attacks.special.impl.screech

import com.zenyte.game.world.World
import com.zenyte.game.world.entity.Location
import com.zenyte.game.world.entity.masks.Graphics
import com.zenyte.game.world.entity.npc.NPC
import com.zenyte.game.world.entity.npc.NpcId

/**
 * @author Glabay | Glabay-Studios
 * @project near-reality-server
 * @social Discord: Glabay
 * @since 2024-09-26
 */
class FloatingPillar(
    spawnLocation: Location
) : NPC(
    NpcId.FLOATING_COLUMN_12210,
    spawnLocation,
    true
) {

    init {
        radius = 0
        hitpoints = 100
        World.sendGraphics(Graphics(2450), spawnLocation)
    }

    // No respawning
    override fun setRespawnTask() {}

    // Cannot walk through it
    override fun isEntityClipped(): Boolean {
        return true
    }
}