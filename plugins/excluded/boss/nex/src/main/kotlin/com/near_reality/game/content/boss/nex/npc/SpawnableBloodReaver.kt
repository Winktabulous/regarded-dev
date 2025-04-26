package com.near_reality.game.content.boss.nex.npc

import com.zenyte.game.util.Direction
import com.zenyte.game.world.entity.Location
import com.zenyte.game.world.entity.npc.NpcId
import com.zenyte.game.world.entity.npc.Spawnable

/**
 * Represents a [Spawnable] [BloodReaver] npc instance.
 *
 * This NPC can be found in the Ancient Prison, outside of Nex's dungeon.
 *
 * @author Stan van der Bend
 */
@Suppress("unused")
class SpawnableBloodReaver(id: Int, tile: Location?, facing: Direction, radius: Int) :
    BloodReaver(id, tile, facing, radius), Spawnable {

    override fun validate(id: Int, name: String) = id == NpcId.BLOOD_REAVER
}
