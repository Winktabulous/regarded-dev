package com.zenyte.net.io

import io.netty.buffer.ByteBuf
import java.nio.ByteBuffer

/**
 * @author Tommeh | 3 okt. 2018 | 20:11:14
 * @see [Rune-Server profile](https://www.rune-server.ee/members/tommeh/)
 */
object ByteBufUtil {

    fun writeString(buf: ByteBuf, string: String): ByteBuf {
        for (c in string.toCharArray()) {
            buf.writeByte(c.code)
        }
        buf.writeByte(0)
        return buf
    }

    fun writeString(buf: ByteBuffer, string: String): ByteBuffer {
        for (c in string.toCharArray()) {
            buf.put(c.code.toByte())
        }
        buf.put(0.toByte())
        return buf
    }

    @JvmStatic
    fun readString(buf: ByteBuf): String {
        check(buf.isReadable) { "Buffer is not readable." }
        val bldr = StringBuilder()
        var read: Byte
        while (buf.isReadable) {
            read = buf.readByte()
            if (read == 0.toByte())
                break
            bldr.append(Char(read.toUShort()))
        }
        return bldr.toString()
    }

    @JvmStatic
    fun readJAGString(buf: ByteBuf): String {
        val bldr = StringBuilder()
        var b: Byte
        buf.readByte()
        while (buf.isReadable) {
            b = buf.readByte()
            if (b == 0.toByte())
                break
            bldr.append(Char(b.toUShort()))
        }
        return bldr.toString()
    }

    fun writeMedium(buf: ByteBuf, value: Int) {
        buf.writeByte(value shr 16)
        buf.writeByte(value shr 8)
        buf.writeByte(value)
    }

    fun writeMedium(buf: ByteBuffer, value: Int) {
        buf.put((value shr 16).toByte())
        buf.put((value shr 8).toByte())
        buf.put(value.toByte())
    }

    @JvmStatic
    fun readMedium(buffer: ByteBuf): Int =
        buffer.readByte() shl 16 or
                (buffer.readByte() shl 8) or
                buffer.readByte().toInt()

    @JvmStatic
    fun readIntME(buffer: ByteBuf): Int =
        buffer.readUnsignedShort() or (buffer.readShort().toInt() shl Short.SIZE_BITS)

    @JvmStatic
    fun readIntIME(buffer: ByteBuf): Int =
        (buffer.readShortLE().toInt() shl Short.SIZE_BITS) or buffer.readUnsignedShortLE()

}