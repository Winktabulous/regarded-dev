package com.zenyte.game.content.boss.abyssalsire.lairs

import com.zenyte.game.content.boss.abyssalsire.AbyssalNexusArea
import com.zenyte.game.content.boss.abyssalsire.AbyssalNexusCorner
import com.zenyte.game.world.region.RSPolygon

/**
 * @author Jire
 */
class AbyssalNexusNorthWestLair : AbyssalNexusArea() {
	override fun polygons() = arrayOf(RSPolygon(AbyssalNexusCorner.NORTH_WEST.regionID))

	override fun name() = AbyssalNexusCorner.NORTH_WEST.areaName
}