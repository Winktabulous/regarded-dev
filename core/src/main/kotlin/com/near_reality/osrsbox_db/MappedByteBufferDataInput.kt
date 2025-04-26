package com.near_reality.osrsbox_db

import java.io.DataInput
import java.nio.MappedByteBuffer

/**
 * @author Jire
 */
class MappedByteBufferDataInput(val source: MappedByteBuffer) : DataInput {

    override fun readFully(b: ByteArray) {
        source.get(b)
    }

    override fun readFully(b: ByteArray, off: Int, len: Int) {
        source.get(b, off, len)
    }

    override fun skipBytes(n: Int): Int {
        source.position(source.position() + n)
        return n
    }

    override fun readBoolean() = source.get().toInt() != 0

    override fun readByte() = source.get()

    override fun readUnsignedByte() = source.get().toInt()

    override fun readShort() = source.short

    override fun readUnsignedShort() = source.short.toInt()

    override fun readChar() = source.char

    override fun readInt() = source.int

    override fun readLong() = source.long

    override fun readFloat() = source.float

    override fun readDouble() = source.double

    override fun readLine(): String =
        throw UnsupportedOperationException("readLine")

    override fun readUTF(): String =
        throw UnsupportedOperationException("readUTF")

}