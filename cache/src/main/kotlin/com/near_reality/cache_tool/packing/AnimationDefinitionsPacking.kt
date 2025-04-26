package com.near_reality.cache_tool.packing

import mgi.types.config.AnimationDefinitions
import mgi.utilities.ByteBuffer
import java.io.File

/**
 * @author Jire
 */
object AnimationDefinitionsPacking {

    fun packAnim(animID: Int, data: ByteBuffer) {
        val def = AnimationDefinitions(animID, data)
        def.pack()
    }

    fun packAnim(animID: Int, data: ByteArray) = packAnim(animID, ByteBuffer(data))

    fun packAnim(animID: Int, dataFile: File) = packAnim(animID, dataFile.readBytes())

    fun packAnim(animID: Int, dataFilePath: String) = packAnim(animID, File(dataFilePath))

}