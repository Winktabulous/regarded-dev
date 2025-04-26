package com.near_reality.cache_tool.packing

import mgi.utilities.ByteBuffer
import java.io.File

/**
 * @author Jire
 */
internal class Asset(val file: File) {
    constructor(filePath: String) : this(File(filePath))

    val name get() = file.nameWithoutExtension

    val id get() = name.toInt()

    val bytes by lazy(LazyThreadSafetyMode.NONE) { file.readBytes() }

    val mgiBuffer by lazy(LazyThreadSafetyMode.NONE) { ByteBuffer(bytes) }

}

internal fun assets(folder: String, use: Asset.() -> Unit) {
    for (file in File(folder).listFiles() ?: throw Exception("File does not exist \"$folder\"")) {
        if (file.nameWithoutExtension.isBlank())
            continue // ignore system files, like .DS_Store in MacOS
        Asset(file).apply(use)
    }
}
