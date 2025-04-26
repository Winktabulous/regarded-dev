package com.near_reality.cache.interfaces.teleports.builder

import com.near_reality.cache.interfaces.teleports.Category
import com.near_reality.cache.interfaces.teleports.Destination
import com.zenyte.game.world.entity.Location
import it.unimi.dsi.fastutil.objects.ObjectArrayList

/**
 * @author Jire
 */
internal class CategoryBuilder(
    override val name: String,
    override val enumID: Int,
    override val id: Int
) : Category {

    override val destinations: MutableList<Destination> = ObjectArrayList()

    operator fun String.invoke(spriteID: Int, x: Int, y: Int, z: Int = 0, wikiURL: String = ""): Destination {
        val destination = DefaultDestination(
            TeleportsBuilder.nextStructID++, this,
            Location(x, y, z), spriteID, wikiURL
        )
        destinations.add(destination)
        return destination
    }

}