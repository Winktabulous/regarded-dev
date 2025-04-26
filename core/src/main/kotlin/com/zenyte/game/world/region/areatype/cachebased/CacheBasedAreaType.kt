package com.zenyte.game.world.region.areatype.cachebased

import com.zenyte.game.world.region.RSPolygon
import com.zenyte.game.world.region.areatype.AbstractAreaType
import com.zenyte.game.world.region.areatype.AreaType
import com.zenyte.utils.efficientarea.Area
import com.zenyte.utils.efficientarea.EfficientArea
import it.unimi.dsi.fastutil.ints.*

/**
 * @author Jire
 */
abstract class CacheBasedAreaType : AbstractAreaType() {

    private val areas: Array<Area?> = arrayOfNulls(AreaType.MAX_Z)
    private val region2Areas: Array<Int2ObjectMap<MutableList<EfficientArea>>> =
        Array(4) { Int2ObjectOpenHashMap() }

    override val regionIDs: IntList by lazy(LazyThreadSafetyMode.NONE) {
        val regionIDs = IntArrayList()
        for (areasPlane in region2Areas) {
            val keys: IntSet = areasPlane.keys
            val it = keys.iterator()
            while (it.hasNext()) {
                val key = it.nextInt()
                @Suppress("UNUSED_VARIABLE") val rz = key ushr 16
                val rxy = key and 0xFFFF
                val rx = rxy ushr 8
                val ry = rxy and 0xFF
                val rid = rx shl 8 or ry
                regionIDs.add(rid)
            }
        }
        return@lazy regionIDs
    }

    abstract fun loadPlaneArea(plane: Int): Area

    override fun load() {
        for (plane in 0 until AreaType.MAX_Z) {
            val area = loadPlaneArea(plane)
            areas[plane] = area

            val list: MutableList<IntArray> = ArrayList()
            val it = area.getPathIterator(null)
            while (!it.isDone) {
                val coords = FloatArray(6)
                it.currentSegment(coords)
                list.add(intArrayOf(coords[0].toInt(), coords[1].toInt()))
                it.next()
            }

            val coords = Array(list.size) { IntArray(2) }
            var i = 0
            for (intarr in list) coords[i++] = intarr

            val polygonList: MutableList<RSPolygon> = ArrayList()
            var currentPolygon: MutableList<IntArray> = ArrayList()

            i = coords.size - 1
            while (i >= 0) {
                val coord = coords[i]
                val x = coord[0]
                val y = coord[1]
                if (x == 0 && y == 0) {
                    AreaType.constructAndAddPolygon(currentPolygon, polygonList, plane)
                    currentPolygon = ArrayList()
                    i--
                    continue
                }
                currentPolygon.add(coord)
                i--
            }
            AreaType.constructAndAddPolygon(currentPolygon, polygonList, plane)
            for (rsPolygon in polygonList) area.add(Area(rsPolygon.polygon))
        }
    }

    override fun map() {
        for (z in 0..areas.lastIndex) {
            val regionSet = AreaType.regionSet
            val it = regionSet.iterator()
            while (it.hasNext()) {
                val id = it.nextInt()

                val x = id shr 8 shl 6
                val y = id and 0xFF shl 6
                val chunkPolygon = RSPolygon(
                    arrayOf(
                        intArrayOf(x, y),
                        intArrayOf(x + 64, y),
                        intArrayOf(x + 64, y + 64),
                        intArrayOf(x, y + 64)
                    ),
                    z
                ).polygon
                val chunkArea = Area(chunkPolygon)

                val polygon: Area = areas[z]!!
                if (!polygon.bounds2D.intersects(chunkArea.bounds2D)) continue
                val area = Area(polygon)
                area.intersect(chunkArea)
                if (!area.isEmpty) {
                    val regionId = AreaType.regionHash(x, y, z)
                    var list = region2Areas[z][regionId]
                    if (list == null) {
                        list = ArrayList()
                        region2Areas[z][regionId] = list
                    }
                    list.add(EfficientArea(polygon))
                }
            }
        }
    }

    override fun matches(x: Int, y: Int, z: Int): Boolean {
        val regionId = AreaType.regionHash(x, y, z)
        val list = region2Areas[z][regionId] ?: return false

        val xd = x.toDouble()
        val yd = y.toDouble()
        for (i in list.lastIndex downTo 0) {
            val polygon = list[i]
            if (polygon.contains(xd, yd)) {
                return true
            }
        }

        return false
    }

}