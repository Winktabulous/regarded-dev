package com.near_reality.game.content.bountyhunter

import com.near_reality.game.world.Border
import com.near_reality.game.world.Boundary
import com.zenyte.game.world.World
import com.zenyte.game.world.entity.Location
import com.zenyte.game.world.entity.player.Player
import com.zenyte.game.world.`object`.WorldObject
import java.util.*

/**
 * This represents all hotspots for Bounty Hunter and their associated boundaries
 * @author John J. Woloszyk / Kryeus
 */
enum class BountyHunterHotspot (val zoneId: Int, val zoneName: String, vararg val boundaries: Boundary){
    ZONE_1(1, "Frozen Waste Plateau", Boundary(2944, 3840, 2981, 3966)),
    ZONE_2(2, "Mage Arena & Pirates' Hideout", Boundary(3012, 3970, 3133, 3904)),
    ZONE_3(3, "Resource Area & Deserted Keep", Boundary(3137, 3969, 3196, 3904)),
    ZONE_4(4, "Rogue's Castle", Boundary(3261, 3966, 3321, 3904)),
    ZONE_5(5, "Lava Maze", Boundary(3020, 3894, 3130, 3811)),
    ZONE_6(6, "Lava Dragon Isle", Boundary(3173, 3856, 3236, 3797), Boundary(3190, 3856, 3236, 3897)),
    ZONE_7(7, "Demonic Ruins", Boundary(3243, 3903, 3350, 3843)),
    ZONE_8(8, "Forgotten Cemetery", Boundary(2942, 3802, 3010, 3711), Boundary(3011, 3802, 3035, 3751), Boundary(3011, 3750, 3024, 3736)),
    ZONE_9(9, "Hobgoblin Mine", Boundary(3054, 3802, 3114, 3738)),
    ZONE_10(10, "Bandit Camp & Western Ruins", Boundary(2942, 3711, 3055, 3652)),
    ZONE_11(11, "Graveyard of Shadows", Boundary(3134, 3752, 3191, 3653)),
    ZONE_12(12, "Eastern Coast", Boundary(3328, 3655, 3389, 3718)),
    ZONE_13(13, "Dark Warriors' Fortress", Boundary(3046, 3648, 2957, 3584)),
    ZONE_14(14, "Chaos Temple", Boundary(3220, 3624, 3255, 3593), Boundary(3220, 3624, 3255, 3659),
        Boundary(3289, 3677, 3260, 3660), Boundary(3255, 3659, 3279, 3629), Boundary(3256, 3628, 3265, 3617)
    );

    private lateinit var positions: MutableList<Location>
    private lateinit var objects: MutableList<WorldObject>
    val OBJ_ID = 60010
    val spacing = 3

    /**
     * This checks the hotspot to determine if a player is currently within
     * the current boundaries defined for the hotspot
     */
    fun checkPlayer(player: Player): Boolean {
        for(boundary in boundaries) {
            if(boundary.isIn(player))
                return true
        }
        return false
    }

    /**
     * Spawn's the boundary in the game world using [Boundary]s and [Border]s to
     * define object spawn locations.
     */
    fun spawnBoundary() {
        positions = mutableListOf()
        objects = mutableListOf()
        if(boundaries.size == 1) {
            val boundary = boundaries[0]

            val x1 = boundary.minX
            val x2 = boundary.highX
            val y1 = boundary.minY
            val y2 = boundary.highY

            for (i in x1..x2 step spacing) {
                /* Min Y line from x1 to x2 */
                positions.add(Location(i, y1))
                /* Min Y line from x1 to x2 */
                positions.add(Location(i, y2))
            }

            for (i in y1..y2 step spacing) {
                /* Min X line from y1 to y2 */
                positions.add(Location(x1, i))
                /* Max X line from y1 to y2 */
                positions.add(Location(x2, i))
            }
        } else {
            for(border in getBorders(this)) {
                if(border.type == Border.BorderType.X) {
                    for (i in border.start..border.end step spacing) {
                        positions.add(Location(i, border.otherCross))
                    }
                }
                if(border.type == Border.BorderType.Y) {
                    for (i in border.start..border.end step spacing) {
                        positions.add(Location(border.otherCross, i))
                    }
                }
            }
        }
        for(position in positions) {
            createBarrier(position)
        }
    }

    private fun createBarrier(position: Location) {
        val obj = BHHotspotBarrier(OBJ_ID, tile = position)
        objects.add(obj)
        World.spawnObject(obj)
    }

    /**
     * This removes all of the [BHHotspotBarrier]s from the gameworld
     * when the current hotspot is cycled out.
     */
    fun removeBoundary() {
        if(objects.size == 0)
            return
        for(obj in objects) {
            World.removeObject(obj)
        }
        objects.clear()
        positions.clear()
    }

    companion object {
        private val HOTSPOTS: Set<BountyHunterHotspot> = Collections.unmodifiableSet(EnumSet.allOf(BountyHunterHotspot::class.java))

        /**
         * This is used to define custom borders for zones that have complex edges and are not
         * a simple rectangular box.
         */
        fun getBorders(spot: BountyHunterHotspot): MutableList<Border> {
            val borders = mutableListOf<Border>()
            when (spot) {
                ZONE_6 -> {
                    borders.add(Border.ofX(3173, 3190, 3856))
                    borders.add(Border.ofY(3797, 3897, 3236))
                    borders.add(Border.fromBoundary(spot.boundaries[0], Border.BoundaryBorder.EAST))
                    borders.add(Border.fromBoundary(spot.boundaries[0], Border.BoundaryBorder.SOUTH))
                    borders.add(Border.fromBoundary(spot.boundaries[1], Border.BoundaryBorder.NORTH))
                    borders.add(Border.fromBoundary(spot.boundaries[1], Border.BoundaryBorder.WEST))
                }
                ZONE_8 -> {
                    borders.add(Border.fromBoundary(spot.boundaries[0], Border.BoundaryBorder.WEST))
                    borders.add(Border.fromBoundary(spot.boundaries[0], Border.BoundaryBorder.SOUTH))
                    borders.add(Border.fromBoundary(spot.boundaries[0], Border.BoundaryBorder.NORTH))
                    borders.add(Border.fromBoundary(spot.boundaries[1], Border.BoundaryBorder.NORTH))
                    borders.add(Border.fromBoundary(spot.boundaries[1], Border.BoundaryBorder.WEST))
                    borders.add(Border.fromBoundary(spot.boundaries[2], Border.BoundaryBorder.EAST))
                    borders.add(Border.fromBoundary(spot.boundaries[2], Border.BoundaryBorder.SOUTH))
                    borders.add(Border.ofX(3025, 3035, 3751))
                    borders.add(Border.ofY(3711, 3735, 3010))
                }
                ZONE_14 -> {
                    borders.add(Border.fromBoundary(spot.boundaries[1], Border.BoundaryBorder.NORTH))
                    borders.add(Border.fromBoundary(spot.boundaries[1], Border.BoundaryBorder.WEST))
                    borders.add(Border.fromBoundary(spot.boundaries[0], Border.BoundaryBorder.WEST))
                    borders.add(Border.fromBoundary(spot.boundaries[0], Border.BoundaryBorder.SOUTH))
                    borders.add(Border.fromBoundary(spot.boundaries[4], Border.BoundaryBorder.SOUTH))
                    borders.add(Border.fromBoundary(spot.boundaries[4], Border.BoundaryBorder.EAST))
                    borders.add(Border.fromBoundary(spot.boundaries[3], Border.BoundaryBorder.EAST))
                    borders.add(Border.fromBoundary(spot.boundaries[2], Border.BoundaryBorder.WEST))
                    borders.add(Border.fromBoundary(spot.boundaries[2], Border.BoundaryBorder.NORTH))
                    borders.add(Border.fromBoundary(spot.boundaries[2], Border.BoundaryBorder.EAST))
                    borders.add(Border.ofX(3280, 3289, 3660))
                    borders.add(Border.ofX(3266, 3279, 3629))
                    borders.add(Border.ofY(3593, 3616, 3255))
                }
                else -> {}
            }
            return borders
        }

        fun random(): BountyHunterHotspot {
            val hs = HOTSPOTS.iterator().asSequence().shuffled().find { true } ?: ZONE_1
            hs.spawnBoundary()
            return hs
        }

        fun randomExcludeCurrent(): BountyHunterHotspot {
            return HOTSPOTS.iterator().asSequence().shuffled().find { it != BountyHunterController.currentHotspot } ?: ZONE_1
        }


    }


}