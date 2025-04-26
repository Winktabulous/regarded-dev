package com.zenyte.game.content.boss.abyssalsire.tentacles

import com.zenyte.game.world.entity.ImmutableLocation
import com.zenyte.game.world.entity.Location
import com.zenyte.game.world.entity.npc.NpcId

/**
 * Sorted from left to right, top to bottom.
 *
 * @author Jire
 * @author Kris
 */
internal enum class AbyssalSireTentacleSpawns(val npcID: Int, val location: Location) {

    A(NpcId.TENTACLE_5910, 2967, 4844),
    B(NpcId.TENTACLE_5910, 2984, 4844),
    C(NpcId.TENTACLE_5911, 2970, 4835),
    D(NpcId.TENTACLE_5911, 2982, 4835),
    E(NpcId.TENTACLE_5909, 2968, 4826),
    F(NpcId.TENTACLE_5909, 2985, 4826);

    constructor(npcID: Int, x: Int, y: Int, z: Int = 0) : this(npcID, ImmutableLocation(x, y, z))

    companion object {
        val values = values()
    }

}