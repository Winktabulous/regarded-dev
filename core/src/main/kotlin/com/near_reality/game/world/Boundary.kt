package com.near_reality.game.world

import com.google.common.base.Preconditions
import com.zenyte.game.world.Position
import com.zenyte.game.world.entity.AbstractEntity
import com.zenyte.game.world.entity.Location
import com.zenyte.game.world.entity.npc.NPC
import com.zenyte.game.world.entity.player.Player


/**
 * This represents an invisible "field" that can be utilized to check
 * whether players are within a specific area based on X, Y, & Plane bounds
 * @author John J. Woloszyk / Kryeus
 */
data class Boundary(
    var minX: Int = 0,
    var minY: Int = 0,
    var highX: Int = 0,
    var highY: Int = 0,
    var height: Int = -1,
    var considerBoundaryHeight: Boolean = false
) {
    constructor(x1: Int, y1: Int, x2: Int, y2: Int) : this() {
        minX = x1.coerceAtMost(x2)
        highX = x1.coerceAtLeast(x2)
        minY = y1.coerceAtMost(y2)
        highY = y1.coerceAtLeast(y2)
        height = -1
        considerBoundaryHeight = false
    }

    constructor(low: Location, high: Location) : this(low.x, low.y, high.x, high.y) {
        considerBoundaryHeight = true
        height = -1
    }

    constructor(low: Location, high: Location, height: Int) : this(low.x, low.y, high.x, high.y, height) {
        considerBoundaryHeight = true
    }


    fun intersects(boundary: Boundary): Boolean {
        val x1 = minX
        val y1 = minY
        val x2 = highX
        val y2 = highY
        val x3 = boundary.minX
        val y3 = boundary.minY
        val x4 = boundary.highX
        val y4 = boundary.highY
        return x1 < x4 && x3 < x2 && y1 < y4 && y3 < y2
    }

    fun `in`(position: Location): Boolean {
        return position.x in minX..highX && position.y in minY..highY
    }

    fun getBoundedPosition(position: Location): Location {
        var x: Int = position.x
        var y: Int = position.y
        if (x < minX) {
            x = minX
        } else if (x > highX) {
            x = highX
        }
        if (y < minY) {
            y = minY
        } else if (y > highY) {
            y = highY
        }
        return Location(x, y, position.plane)
    }

    fun `in`(entity: AbstractEntity): Boolean {
        return isIn(entity, this)
    }

    fun `in`(x: Int, y: Int): Boolean {
        return `in`(x, y, 0)
    }

    fun `in`(boundary: Boundary): Boolean {
        return minX >= boundary.minX && highX <= boundary.highX && minY >= boundary.minY && highY <= boundary.highY
    }

    fun `in`(x: Int, y: Int, z: Int): Boolean {
        return x in minX..highX && y in minY..highY
    }

    fun `in`(entity: Player, considerBoundaryHeight: Boolean): Boolean {
        return isIn(entity, this, considerBoundaryHeight)
    }

    fun isIn(player: Player): Boolean {
        return isIn(player, this)

    }

    fun getOuter(): Set<Location> {
        val outerSet: MutableSet<Location> = HashSet()
        for (x in minX..this.highX) {
            outerSet.add(Location(x, this.minY))
            outerSet.add(Location(x, this.highY))
        }
        for (y in this.minY..this.highY) {
            outerSet.add(Location(this.minX, y))
            outerSet.add(Location(this.highX, y))
        }
        return outerSet
    }

    companion object {
        val WILDERNESS = Boundary(2941, 3525, 3392, 3968)
        val WILDERNESS_UNDERGROUND = Boundary(2941, 9918, 3392, 10366)
        val FEROX_ENCLAVE1 = Boundary(3123, 3622, 3150, 3632)
        val FEROX_ENCLAVE2 = Boundary(3125, 3633, 3153, 3638)
        val FEROX_ENCLAVE3 = Boundary(3138, 3639, 3145, 3645)
        val FEROX_ENCLAVE4 = Boundary(3146, 3639, 3155, 3646)
        val FEROX_ENCLAVE5 = Boundary(3154, 3636, 3155, 3638)
        val FEROX_ENCLAVE6 = Boundary(3151, 3626, 3160, 3633)
        val FEROX_ENCLAVE7 = Boundary(3126, 3618, 3143, 3621)
        val FEROX_ENCLAVE8 = Boundary(3130, 3617, 3139, 3617)
        val FEROX_ENCLAVE9 = Boundary(3125, 3639, 3137, 3639)
        val FEROX_ENCLAVE10 = Boundary(3154, 3633, 3154, 3636)

        @JvmField
        val AFK_ZONE = Boundary(3096, 3469, 3113, 3483)


        @JvmStatic fun isIn(player: Player, boundary: Boundary, considerBoundaryHeight: Boolean): Boolean {
            if (considerBoundaryHeight) {
                if (boundary.height >= 0) {
                    if (player.plane != boundary.height) {
                        return false
                    }
                }
            }
            return player.x >= boundary.minX && player.x <= boundary.highX && player.y >= boundary.minY && player.y <= boundary.highY
        }

        @JvmStatic fun isIn(player: Player, boundary: Boundary): Boolean {
            if (boundary.height >= 0) {
                if (player.plane != boundary.height) {
                    return false
                }
            }
            return player.x >= boundary.minX && player.x <= boundary.highX && player.y >= boundary.minY && player.y <= boundary.highY
        }

        @JvmStatic fun isIn(npc: NPC, boundary: Boundary): Boolean {
            if (boundary.height >= 0) {
                if (npc.plane != boundary.height) {
                    return false
                }
            }
            return npc.x >= boundary.minX && npc.x <= boundary.highX && npc.y >= boundary.minY && npc.y <= boundary.highY
        }

        @JvmStatic fun isIn(npc: NPC, vararg boundaries: Boundary): Boolean {
            for(boundary in boundaries) {
                if(boundary.height >= 0) {
                    if(npc.plane != boundary.height)
                        continue
                }
                if(npc.x >= boundary.minX && npc.x <= boundary.highX && npc.y >= boundary.minY && npc.y <= boundary.highY) {
                    return true
                }
            }
            return false
        }

        @JvmStatic fun isIn(entity: AbstractEntity, vararg boundaries: Boundary): Boolean {
            Preconditions.checkState(boundaries.isNotEmpty(), "No boundaries specified.")
            return isIn(entity.position, *boundaries)
        }

        @JvmStatic fun isIn(position: Location, vararg boundaries: Boundary): Boolean {
            for(boundary in boundaries) {
                if (boundary.height >= 0) {
                    if (position.plane != boundary.height) {
                        continue
                    }
                }
                if(position.x >= boundary.minX && position.x <= boundary.highX && position.y >= boundary.minY && position.y <= boundary.highY)
                    return true
            }
            return false
        }

        fun createSurroundingBoundary(entity: AbstractEntity): Boundary? {
            return createSurroundingBoundary(entity, 1)
        }

        fun createSurroundingBoundary(entity: AbstractEntity, offset: Int): Boundary {
            return createSurroundingBoundary(entity.position, offset, entity.size)
        }

        fun createSurroundingBoundary(position: Position, offset: Int): Boundary {
            return createSurroundingBoundary(position, offset, 0)
        }

        fun createSurroundingBoundary(position: Position, offset: Int, size: Int): Boundary {
            var size = size
            size -= 1
            val minDeltaX: Int = position.position.x - offset
            val minDeltaY: Int = position.position.y - offset
            val maxDeltaX: Int = position.position.x + size + offset
            val maxDeltaY: Int = position.position.y + size + offset
            return Boundary(minDeltaX, minDeltaY, maxDeltaX, maxDeltaY, position.position.plane)
        }



        fun isIn(player: Player, boundaries: MutableList<Boundary>): Boolean {
            for(boundary in boundaries)
                if(isIn(player, boundary))
                    return true
            return false
        }


    }

}