package com.zenyte.game.content.theatreofblood.room.nylocas.model

import com.zenyte.game.util.Utils
import com.zenyte.game.world.entity.Location

/**
 * @author Tommeh
 * @author Jire
 */
internal enum class PillarLocation(
    val rotation: Int,
    val location: Location,
    vararg corners: PillarCorner
) {

    NORTH_WEST(
        1, Location(3289, 4253, 0),
        PillarCorner(Location(3290, 4252, 0), Location(3291, 4252, 0)),
        PillarCorner(Location(3292, 4254, 0), Location(3292, 4253, 0))
    ),
    SOUTH_WEST(
        0, Location(3289, 4242, 0),
        PillarCorner(Location(3290, 4245, 0), Location(3291, 4245, 0)),
        PillarCorner(Location(3292, 4243, 0), Location(3292, 4244, 0))
    ),
    NORTH_EAST(
        2, Location(3300, 4253, 0),
        PillarCorner(Location(3301, 4252, 0), Location(3300, 4252, 0)),
        PillarCorner(Location(3299, 4254, 0), Location(3299, 4253, 0))
    ),
    SOUTH_EAST(
        3, Location(3300, 4242, 0),
        PillarCorner(Location(3301, 4245, 0), Location(3300, 4245, 0)),
        PillarCorner(Location(3299, 4243, 0), Location(3299, 4244, 0))
    );

    val corners: Array<out PillarCorner>

    init {
        this.corners = corners
    }

    companion object {

        val values = values()

        fun getRandom() = values[Utils.random(values.size - 1)]

    }

}