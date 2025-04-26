package com.near_reality.game.content.pvm_arena.npc

import com.zenyte.game.world.entity.EntityHitBar
import com.zenyte.game.world.entity.npc.NPC

/**
 * Represents a hit bar for a PvM Arena boss.
 *
 * @author Stan van der Bend
 */
class PvmArenaNpcHealthBar(npc: NPC) : EntityHitBar(npc) {

    override fun getType(): Int = 17
}
