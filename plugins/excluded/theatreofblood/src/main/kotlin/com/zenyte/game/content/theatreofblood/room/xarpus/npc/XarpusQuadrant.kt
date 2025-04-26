package com.zenyte.game.content.theatreofblood.room.xarpus.npc

import com.zenyte.game.content.theatreofblood.room.xarpus.XarpusRoom
import com.zenyte.game.util.Utils
import com.zenyte.game.world.entity.Location
import java.util.function.BiPredicate

/**
 * @author Tommeh
 * @author Jire
 */
internal enum class XarpusQuadrant(x: Int, y: Int, private val insidePredicate: BiPredicate<Location, Location>) {

    SOUTH_EAST(3177, 4380, BiPredicate { target, center -> target.x > center.x && target.y <= center.y }),
    SOUTH_WEST(3163, 4380, BiPredicate { target, center -> target.x <= center.x && target.y <= center.y }),
    NORTH_WEST(3163, 4394, BiPredicate { target, center -> target.x <= center.x && target.y > center.y }),
    NORTH_EAST(3177, 4394, BiPredicate { target, center -> target.x > center.x && target.y > center.y });

    val cornerTile = Location(x, y, XarpusRoom.PLANE)

    fun randomOther(): XarpusQuadrant = Utils.getRandomCollectionElement(values.filter { it != this })

    fun isInside(location: Location, xarpus: Xarpus) =
        insidePredicate.test(location, xarpus.room.getLocation(3170, 4387, XarpusRoom.PLANE))

    companion object {

        private val values = values()

        fun random(): XarpusQuadrant = Utils.getRandomElement(*values)

        fun of(faceLocation: Location): XarpusQuadrant {
            for (quadrant in values)
                if (faceLocation == quadrant.cornerTile)
                    return quadrant
            throw IllegalArgumentException("Could not find Xarpus quadrant for tile: $faceLocation")
        }

    }

}