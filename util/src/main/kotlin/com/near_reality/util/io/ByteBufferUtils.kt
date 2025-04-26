package com.near_reality.util.io

import java.io.File
import java.io.RandomAccessFile
import java.nio.MappedByteBuffer
import java.nio.channels.FileChannel

/**
 * @author Jire
 */
object ByteBufferUtils {

    @JvmStatic
    fun load(file: File): MappedByteBuffer = RandomAccessFile(file, "r").use {
        val channel = it.channel
        val buffer = channel.map(FileChannel.MapMode.READ_ONLY, 0, channel.size())
        buffer
    }

}