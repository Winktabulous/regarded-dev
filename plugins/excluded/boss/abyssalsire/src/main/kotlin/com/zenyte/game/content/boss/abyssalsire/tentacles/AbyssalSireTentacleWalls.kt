package com.zenyte.game.content.boss.abyssalsire.tentacles

import com.zenyte.game.content.boss.abyssalsire.AbyssalNexusCorner
import com.zenyte.game.content.boss.abyssalsire.AbyssalSire
import com.zenyte.game.util.Direction
import com.zenyte.game.world.World
import com.zenyte.game.world.entity.Location

/**
 * @author Jire
 * @author Kris
 */
internal class AbyssalSireTentacleWalls(private val corner: AbyssalNexusCorner) {

    constructor(sire: AbyssalSire) : this(sire.corner)

    private val collection: MutableCollection<AbyssalSireTentacleWall> = HashSet()

    fun removeAll() {
        collection.forEach(AbyssalSireTentacleWall::remove)
        collection.clear()
    }

    fun buildAll() {
        // Just in case, re-remove the walls if any are still present.
        removeAll()

        for (rectangle in wallRectangles) {
            val swCorner = corner.translate(Location(rectangle.minX.toInt(), rectangle.minY.toInt()))
            val neCorner = corner.translate(Location(rectangle.maxX.toInt(), rectangle.maxY.toInt()))

            val minX = swCorner.x
            val minY = swCorner.y
            val maxX = neCorner.x
            val maxY = neCorner.y

            // Build western wall
            for (y in minY..maxY) {
                collection.add(AbyssalSireTentacleWall(Location(minX, y), Direction.WEST))
                /* TODO: Check if a player is standing there,
                    they shall be pushed east in the case of western walls, vice versa for eastern ones. */
            }

            // Build eastern wall
            for (y in minY..maxY)
                collection.add(AbyssalSireTentacleWall(Location(maxX, y), Direction.EAST))

            // Build northern wall (excluding both diagonal corners)
            for (x in minX + 1 until maxX)
                collection.add(AbyssalSireTentacleWall(Location(x, maxY), Direction.NORTH))

            // Build southern wall (excluding both diagonal corners)
            for (x in minX + 1 until maxX)
                collection.add(AbyssalSireTentacleWall(Location(x, minY), Direction.SOUTH))
        }

        collection.forEach(AbyssalSireTentacleWall::build)
    }

    private companion object {
        val wallRectangles = listOf(
            World.getRectangle(2966, 2971, 4841, 4849),
            World.getRectangle(2988, 2991, 4841, 4849),
            World.getRectangle(2969, 2972, 4830, 4837),
            World.getRectangle(2989, 2994, 4830, 4836)
        )
    }

}