package com.zenyte.game.world.region.areatype.filebased

import com.zenyte.game.world.region.areatype.AbstractAreaType
import com.zenyte.game.world.region.areatype.AreaType
import com.zenyte.game.world.region.areatype.AreaType.Companion.MAX_X
import com.zenyte.game.world.region.areatype.AreaType.Companion.MAX_Z
import com.zenyte.game.world.region.areatype.filebased.GenerateAreaTypeFiles.areaFileBase
import com.zenyte.game.world.region.areatype.filebased.GenerateAreaTypeFiles.readAreaFile
import com.zenyte.game.world.region.areatype.filebased.GenerateAreaTypeFiles.readAreaFileRegions
import it.unimi.dsi.fastutil.ints.IntList
import java.io.File
import java.util.*

/**
 * @author Jire
 */
abstract class FileBasedAreaType(val fileBase: String) : AbstractAreaType() {
    constructor(areaTypeBase: AreaType) : this(areaFileBase(areaTypeBase))

    override lateinit var regionIDs: IntList
    private lateinit var planes: Array<BitSet>

    override fun matches(x: Int, y: Int, z: Int): Boolean {
        val plane = planes[z]
        val bitIndex = (y * MAX_X) + x
        return plane.get(bitIndex)
    }

    override fun load() {}

    override fun map() {
        regionIDs = readAreaFileRegions(File("$fileBase-regionids.dat"))
        planes = Array(MAX_Z) { plane ->
            val file = File("$fileBase-plane$plane.dat")
            readAreaFile(file)
        }
    }

}