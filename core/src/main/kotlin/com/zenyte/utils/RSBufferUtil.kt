package com.zenyte.utils

import com.zenyte.game.util.ZenyteHuffman
import com.zenyte.net.io.RSBuffer

fun RSBuffer.readString(maximumLength: Int): String = try {
    val length = smart.coerceAtMost(maximumLength)
    val decompressedChars = ByteArray(length)
    val compressedChars = ByteArray(readableBytes().coerceAtMost(maximumLength))
    readBytes(compressedChars)
    readerIndex(ZenyteHuffman.decompress(compressedChars, 0, decompressedChars, 0, length))
    StringUtilities.decodeString(decompressedChars, 0, length)
} catch (exception_0: Exception) {
    exception_0.printStackTrace()
    "Cabbage"
}
