package com.zenyte.game.world.region.areatype.filebased

import com.near_reality.util.io.ByteBufferUtils
import com.zenyte.CacheManager
import com.zenyte.game.world.region.XTEALoader
import com.zenyte.game.world.region.areatype.AreaType
import com.zenyte.game.world.region.areatype.AreaType.Companion.MAX_X
import com.zenyte.game.world.region.areatype.AreaType.Companion.MAX_Y
import com.zenyte.game.world.region.areatype.AreaType.Companion.MAX_Z
import com.zenyte.game.world.region.areatype.cachebased.MultiwayAreaTypeCB
import com.zenyte.game.world.region.areatype.cachebased.SinglesPlusAreaTypeCB
import it.unimi.dsi.fastutil.ints.IntArrayList
import it.unimi.dsi.fastutil.ints.IntList
import mgi.tools.jagcached.cache.Cache
import java.io.File
import java.nio.ByteBuffer
import java.util.*

/**
 * @author Jire
 */
object GenerateAreaTypeFiles {

    @JvmStatic
    fun main(args: Array<String>) {
        val cache = Cache.openCache("cache/data/cache")
        CacheManager.loadCache(cache)
        XTEALoader.load("cache/data/objects/xteas.json")

        generateAreaFile(SinglesPlusAreaTypeCB)
        generateAreaFile(MultiwayAreaTypeCB)
    }

    fun generateAreaFile(type: AreaType) {
        type.load()
        type.map()

        val data = type.regionIDs.toIntArray()
        val bb = ByteBuffer.allocate(data.size * MAX_Z)
        val ib = bb.asIntBuffer()
        ib.put(data)
        val array: ByteArray = bb.array()
        areaFileRegions(type).writeBytes(array)

        for (z in 0 until MAX_Z) {
            val bitSet = BitSet(MAX_Y * MAX_X)
            var bitIndex = 0
            for (y in 0 until MAX_Y) {
                for (x in 0 until MAX_X) {
                    bitSet.set(bitIndex++, type.matches(x, y, z))
                }
            }

            areaFile(type, z).writeBytes(bitSet.toByteArray())
        }
    }

    fun areaFileBase() = "data/areatypes/"

    fun areaFileBase(type: AreaType) = "${areaFileBase()}${type.javaClass.simpleName}"

    fun areaFileName(type: AreaType, plane: Int) = "${areaFileBase(type)}-plane$plane.dat"

    fun areaFileNameRegions(type: AreaType) = "${areaFileBase(type)}-regionids.dat"

    fun areaFileRegions(type: AreaType) = File(areaFileNameRegions(type))

    fun areaFile(type: AreaType, plane: Int) = File(areaFileName(type, plane))

    fun readAreaFile(type: AreaType): Array<BitSet> = Array(MAX_Z) { plane -> readAreaFile(areaFile(type, plane)) }

    fun readAreaFile(file: File): BitSet = BitSet.valueOf(ByteBufferUtils.load(file))

    fun readAreaFileRegions(file: File): IntList {
        val bb = ByteBufferUtils.load(file)
        val capacity = bb.remaining() / Int.SIZE_BYTES

        val list: IntList = IntArrayList(capacity)
        for (i in 0 until capacity) {
            list.add(bb.int)
        }
        return list
    }

    fun readAreaFileRegions(type: AreaType) = readAreaFileRegions(areaFileRegions(type))

}