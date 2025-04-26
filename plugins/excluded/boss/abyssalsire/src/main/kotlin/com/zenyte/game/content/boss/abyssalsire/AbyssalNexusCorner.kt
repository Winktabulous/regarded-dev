package com.zenyte.game.content.boss.abyssalsire

import com.zenyte.game.world.entity.Location
import mgi.utilities.StringFormatUtil

/**
 * @author Jire
 * @author Kris
 */
enum class AbyssalNexusCorner(
    internal val regionID: Int,
    private val xOffset: Int, private val yOffset: Int) {

    NORTH_WEST(11851, 0, 0),
    NORTH_EAST(12363, -3 + 128, 0),
    SOUTH_WEST(11850, -10, -64),
    SOUTH_EAST(12362, 2 + 128, -64);

    val areaName: String = "Abyssal Nexus: ${StringFormatUtil.formatString(name.replace('_', '-'))}"

    fun translate(northWestLocation: Location): Location = northWestLocation.transform(xOffset, yOffset, 0)

    internal companion object {
        val values = values()
    }

}