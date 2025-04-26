package com.near_reality.game.util

import com.zenyte.CacheManager
import io.netty.buffer.ByteBuf
import mgi.tools.jagcached.ArchiveType
import mgi.tools.jagcached.cache.Cache

class Huffman(
    val frequencies: ByteArray,
    val masks: IntArray,
    val keys: IntArray
) {

    fun compress(source: String, sourceOffset: Int, length: Int, dest: ByteBuf, destOffset: Int): Int {
        var key = 0
        var pos = destOffset shl 3

        dest.markWriterIndex()
        for (i in sourceOffset..sourceOffset + length - 1) {
            val code = source[i].code and 255
            val encoding = masks[code]

            val frequency = frequencies[code].toInt()
            if (frequency == 0) throw RuntimeException("No codeword for data value $code.")

            var bytePos = pos shr 3
            var remainder = pos and 7
            key = key and ((-remainder) shr 31)

            val comparedBytePos = (remainder + frequency - 1 shr 3) + bytePos
            remainder += 24
            key = key or (encoding ushr remainder)
            dest.ensureSetByte(bytePos, key)
            if (bytePos < comparedBytePos) {
                bytePos++
                remainder -= 8
                key = encoding ushr remainder
                dest.ensureSetByte(bytePos, key)
                if (bytePos < comparedBytePos) {
                    bytePos++
                    remainder -= 8
                    key = encoding ushr remainder
                    dest.ensureSetByte(bytePos, key)
                    if (bytePos < comparedBytePos) {
                        bytePos++
                        remainder -= 8
                        key = encoding ushr remainder
                        dest.ensureSetByte(bytePos, key)
                        if (bytePos < comparedBytePos) {
                            bytePos++
                            remainder -= 8
                            key = encoding shl (-remainder)
                            dest.ensureSetByte(bytePos, key)
                        }
                    }
                }
            }
            pos += frequency
        }
        dest.resetWriterIndex()
        return (pos + 7 shr 3) - destOffset
    }

    private fun ByteBuf.ensureSetByte(bytePos: Int, key: Int) {
        writerIndex(bytePos)
        ensureWritable(1)
        writeByte(key)
    }

    fun decompress(source: ByteBuf, dest: ByteBuf, length: Int): Int {
        if (length == 0) return 0

        var keyIndex = 0
        var written = 0

        while (true) {
            val char = source.readByte().toInt()

            if (char >= 0) keyIndex++ else keyIndex = keys[keyIndex]
            var key = keys[keyIndex]
            if (key < 0) {
                dest.writeByte(key.inv())
                if (++written >= length) break
                keyIndex = 0
            }

            if ((char and 0x40) == 0) keyIndex++ else keyIndex = keys[keyIndex]
            key = keys[keyIndex]
            if (key < 0) {
                dest.writeByte(key.inv())
                if (++written >= length) break
                keyIndex = 0
            }

            if ((char and 0x20) == 0) keyIndex++ else keyIndex = keys[keyIndex]
            key = keys[keyIndex]
            if (key < 0) {
                dest.writeByte(key.inv())
                if (++written >= length) break
                keyIndex = 0
            }

            if ((char and 0x10) == 0) keyIndex++ else keyIndex = keys[keyIndex]
            key = keys[keyIndex]
            if (key < 0) {
                dest.writeByte(key.inv())
                if (++written >= length) break
                keyIndex = 0
            }

            if ((char and 0x8) == 0) keyIndex++ else keyIndex = keys[keyIndex]
            key = keys[keyIndex]
            if (key < 0) {
                dest.writeByte(key.inv())
                if (++written >= length) break
                keyIndex = 0
            }

            if ((char and 0x4) == 0) keyIndex++ else keyIndex = keys[keyIndex]
            key = keys[keyIndex]
            if (key < 0) {
                dest.writeByte(key.inv())
                if (++written >= length) break
                keyIndex = 0
            }

            if ((char and 0x2) == 0) keyIndex++ else keyIndex = keys[keyIndex]
            key = keys[keyIndex]
            if (key < 0) {
                dest.writeByte(key.inv())
                if (++written >= length) break
                keyIndex = 0
            }

            if ((char and 0x1) == 0) keyIndex++ else keyIndex = keys[keyIndex]
            key = keys[keyIndex]
            if (key < 0) {
                dest.writeByte(key.inv())
                if (++written >= length) break
                keyIndex = 0
            }
        }

        return written
    }

    companion object {

        @JvmStatic
        @JvmOverloads
        fun load(cache: Cache = CacheManager.getCache()): Huffman {
            val archive = cache.getArchive(ArchiveType.BINARY)
            val group = archive.findGroupByName("huffman")
            val frequencies = group.findFileByID(0).data.buffer
            return load(frequencies)
        }

        @JvmStatic
        fun load(frequencies: ByteArray): Huffman {
            val masks = IntArray(frequencies.size)
            val frequencyMasks = IntArray(33)
            var keys = IntArray(8)
            var biggestId = 0
            frequencies.forEachIndexed { i, freq ->
                if (freq.toInt() != 0) {
                    val frequencyMask = 1 shl (32 - freq)
                    val mask = frequencyMasks[freq.toInt()]
                    masks[i] = mask
                    val i_9: Int
                    var keyId: Int
                    var i_11: Int
                    var someOtherMask: Int
                    if (mask and frequencyMask != 0) {
                        i_9 = frequencyMasks[freq - 1]
                    } else {
                        i_9 = mask or frequencyMask
                        keyId = freq - 1
                        while (keyId >= 1) {
                            i_11 = frequencyMasks[keyId]
                            if (i_11 != mask) break
                            someOtherMask = 1 shl (32 - keyId)
                            if (i_11 and someOtherMask != 0) {
                                frequencyMasks[keyId] = frequencyMasks[keyId - 1]
                                break
                            }
                            frequencyMasks[keyId] = i_11 or someOtherMask
                            --keyId
                        }
                    }

                    frequencyMasks[freq.toInt()] = i_9

                    keyId = freq + 1
                    while (keyId <= 32) {
                        if (frequencyMasks[keyId] == mask)
                            frequencyMasks[keyId] = i_9
                        keyId++
                    }

                    keyId = 0

                    i_11 = 0
                    while (i_11 < freq) {
                        someOtherMask = Integer.MIN_VALUE.ushr(i_11)
                        if (mask and someOtherMask != 0) {
                            if (keys[keyId] == 0)
                                keys[keyId] = biggestId

                            keyId = keys[keyId]
                        } else {
                            ++keyId
                        }
                        if (keyId >= keys.size) {
                            val ints_13 = IntArray(keys.size * 2)
                            for (i_14 in keys.indices) {
                                ints_13[i_14] = keys[i_14]
                            }
                            keys = ints_13
                        }
                        i_11++
                    }

                    keys[keyId] = i.inv()
                    if (keyId >= biggestId) biggestId = keyId + 1
                }
            }
            return Huffman(frequencies, masks, keys)
        }

    }

}
