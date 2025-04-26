package com.near_reality.game.content.araxxor.attacks

import com.zenyte.game.world.entity.Location
import com.zenyte.game.world.`object`.ObjectId.ACID_POOL_54148
import com.zenyte.game.world.`object`.WorldObject

/**
 * @author Glabay | Glabay-Studios
 * @project near-reality-server
 * @social Discord: Glabay
 * @since 2024-10-22
 */
class AcidPool(
    val spawn: Location
): WorldObject(
    ACID_POOL_54148,
    10,
    0,
    spawn
)