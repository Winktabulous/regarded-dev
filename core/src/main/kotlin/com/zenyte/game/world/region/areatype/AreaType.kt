package com.zenyte.game.world.region.areatype

import com.zenyte.CacheManager
import com.zenyte.game.world.Position
import com.zenyte.game.world.entity.Location
import com.zenyte.game.world.region.RSPolygon
import com.zenyte.utils.MapLocations
import it.unimi.dsi.fastutil.ints.IntList
import it.unimi.dsi.fastutil.ints.IntOpenHashSet
import it.unimi.dsi.fastutil.ints.IntSet
import mgi.tools.jagcached.ArchiveType

/**
 * @author Jire
 */
interface AreaType {

    val regionIDs: IntList

    fun matches(x: Int, y: Int, z: Int): Boolean

    fun matches(location: Location) = matches(location.x, location.y, location.plane)

    fun matches(position: Position) = matches(position.position)

    fun load()

    fun map()

    companion object {
        const val MAX_X = 6400
        const val MAX_Y = 16384
        const val MAX_Z = MapLocations.MAX_Z

        fun regionHash(x: Int, y: Int, z: Int) = ((x shr 6) shl 8) + (y shr 6) or (z shl 16)

        fun constructAndAddPolygon(
            currentPolygon: List<IntArray>,
            polygonList: MutableList<RSPolygon>,
            plane: Int
        ) {
            if (currentPolygon.isEmpty()) return

            val currentPolyLines = Array(currentPolygon.size) {
                currentPolygon[it]
            }
            polygonList.add(RSPolygon(currentPolyLines, plane))
        }

        @JvmStatic
        val regionSet: IntSet by lazy(LazyThreadSafetyMode.NONE) {

            // Basically what it does is see if there's a map file for each of the 64x64 regions
            // and adds the region id to the list if so - if there's no map, there's no multi.

            val set: IntSet = IntOpenHashSet(2560)
            val archive = CacheManager.getCache().getArchive(ArchiveType.MAPS)
            for (rx in 0..99) {
                for (ry in 0..255) {
                    archive.findGroupByName("m" + rx + "_" + ry) ?: continue
                    set.add(rx shl 8 or ry)
                }
            }

            set
        }
    }

}