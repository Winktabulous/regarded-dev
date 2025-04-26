package com.near_reality.cache.interfaces.teleports

import com.zenyte.game.world.entity.Location

/**
 * @author Jire
 */
interface Destination {

    val structID: Int

    val name: String
    val location: Location
    val spriteID: Int
    val wikiURL: String

}