package com.near_reality.game.content.dt2.npc.theduke

import com.near_reality.game.content.dt2.area.DukeSucellusInstance
import com.zenyte.game.util.Direction
import com.zenyte.game.world.Position
import com.zenyte.game.world.entity.Location
import com.zenyte.game.world.`object`.WorldObject
import com.zenyte.game.world.`object`.WorldObject.Companion.DEFAULT_TYPE

/**
 * Mack wrote original logic - Kry rewrote in NR terms
 * @author John J. Woloszyk / Kryeus
 * @date 8.14.2024
 */
data class Extremity(
    var location: Location,
    var asleepId: Int,
    var awakenedId: Int,
    val rotation: Int
) {
    fun awakened(): WorldObject = WorldObject(
        id = awakenedId,
        rotation = rotation,
        tile = location
    )

    fun sleeping(): WorldObject = WorldObject(
        id = asleepId,
        rotation = rotation,
        tile = location
    )

}