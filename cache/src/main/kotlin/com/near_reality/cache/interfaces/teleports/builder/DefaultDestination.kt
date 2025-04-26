package com.near_reality.cache.interfaces.teleports.builder

import com.near_reality.cache.interfaces.teleports.Destination
import com.zenyte.game.world.entity.Location

/**
 * @author Jire
 */
internal data class DefaultDestination(
    override val structID: Int,

    override val name: String,
    override val location: Location,
    override val spriteID: Int,
    override val wikiURL: String
) : Destination