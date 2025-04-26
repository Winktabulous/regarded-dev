package com.near_reality.network.js5

import com.zenyte.net.js5.packet.inc.JS5FileRequest
import io.netty.buffer.ByteBuf
import io.netty.buffer.Unpooled
import it.unimi.dsi.fastutil.longs.Long2ObjectMap
import it.unimi.dsi.fastutil.longs.Long2ObjectOpenHashMap
import mgi.tools.jagcached.ArchiveType
import mgi.tools.jagcached.cache.Cache
import kotlin.math.pow

/**
 * @author Jire
 */
object JS5Responses {

    private val map: Long2ObjectMap<ByteBuf> = Long2ObjectOpenHashMap(2.toDouble().pow(17).toInt())

    /**
     * Amount of bytes that can be sent after sending metadata
     *
     * @author Tommeh
     */
    private const val BYTES_AFTER_HEADER = 512 - 8

    /**
     * Amount of bytes that can be sent
     *
     * @author Tommeh
     */
    private const val BYTES_AFTER_BLOCK = 512 - 1

    @JvmStatic
    fun hash(indexID: Int, fileID: Int) = fileID.toLong() or (indexID.toLong() shl 24)

    @JvmStatic
    operator fun get(hash: Long): ByteBuf? = map[hash]

    @JvmStatic
    operator fun get(indexID: Int, fileID: Int) = get(hash(indexID, fileID))

    @JvmStatic
    operator fun get(request: JS5FileRequest) = get(request.index, request.file)

    @JvmStatic
    fun preload(cache: Cache) {
        /* build regular responses */
        for (archiveType in ArchiveType.entries) {
            // disable these giant files that are disabled anyways.
            if (archiveType == ArchiveType.WORLDMAPDATA_LEGACY) continue

            val indexID = archiveType.id
            val index = cache.getIndex(indexID)
            for (fileID in 0..index.groupCount()) {
                if (indexID == 255 && fileID == 255) continue
                val data = index.get(fileID)?.buffer ?: continue
                var size = data.size

                if (indexID != 255 && size > 1) {
                    val compression = data[0].usin
                    val compressedLength =
                        (data[1].usin shl 24) or
                                (data[2].usin shl 16) or
                                (data[3].usin shl 8) or
                                data[4].usin
                    val realLength = if (compression == 0) // when uncompressed...
                        compressedLength // ...real length is the same as compressed length
                    else // otherwise, the data also contains the decompressed length so +4 bytes here
                        compressedLength + 4

                    // +5 because encoder writes compression and compressed length from container data
                    size = realLength + 5
                }

                encodeData(indexID, fileID, data, size)
            }
        }

        /* build checksums response */
        val data = cache.generateInformationStoreDescriptor().buffer
        encodeData(255, 255, data)
    }

    private fun encodeData(indexID: Int, fileID: Int, data: ByteBuf): ByteBuf {
        val settings = data.readUnsignedByte().toInt()
        val size = data.readInt()

        var bytes = data.readableBytes()

        val response = data.alloc().directBuffer(8 + bytes + (bytes / BYTES_AFTER_HEADER))
            .writeByte(indexID)
            .writeShort(fileID)
            .writeByte(settings)
            .writeInt(size)

        if (bytes > BYTES_AFTER_HEADER)
            bytes = BYTES_AFTER_HEADER
        response.writeBytes(data, bytes)

        while (true) {
            bytes = data.readableBytes()
            if (bytes < 1) break

            if (bytes > BYTES_AFTER_BLOCK)
                bytes = BYTES_AFTER_BLOCK

            response
                .writeByte(255)
                .writeBytes(data, bytes)
        }

        val hash = hash(indexID, fileID)
        map[hash] = response

        return response
    }

    private fun encodeData(indexID: Int, fileID: Int, data: ByteArray, size: Int = data.size): ByteBuf {
        val buf = Unpooled.wrappedBuffer(data, 0, size)
        try {
            return encodeData(indexID, fileID, buf)
        } finally {
            buf.release()
        }
    }

    private val Byte.usin get() = toInt() and 0xFF

}