package com.zenyte.game.world.region.areatype.cachebased

import com.zenyte.utils.MapLocations
import com.zenyte.utils.efficientarea.Area

/**
 * @author Jire
 */
object MultiwayAreaTypeCB : CacheBasedAreaType() {
    override fun loadPlaneArea(plane: Int): Area = MapLocations.getMulticombat(plane)
}