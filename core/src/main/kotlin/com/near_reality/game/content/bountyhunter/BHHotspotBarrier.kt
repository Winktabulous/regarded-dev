package com.near_reality.game.content.bountyhunter

import com.zenyte.game.world.entity.Location
import com.zenyte.game.world.`object`.WorldObject

/**
 * This represents a hotspot barrier object in the game world
 * @author John J. Woloszyk / Kryeus
 */
class BHHotspotBarrier(id: Int, type: Int = DEFAULT_TYPE, rotation: Int = DEFAULT_ROTATION, tile: Location) :
    WorldObject(id, type, rotation, tile) {
}