package com.zenyte.game.content.boss.abyssalsire.lairs

import com.zenyte.game.content.boss.abyssalsire.AbyssalNexusArea
import com.zenyte.game.content.boss.abyssalsire.AbyssalNexusCorner
import com.zenyte.game.world.region.RSPolygon

/**
 * @author Jire
 */
class AbyssalNexusSouthEastLair : AbyssalNexusArea() {
	override fun polygons() = arrayOf(RSPolygon(AbyssalNexusCorner.SOUTH_EAST.regionID))

	override fun name() = AbyssalNexusCorner.SOUTH_EAST.areaName
}